package ar.com.ada.api.pagada.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.entities.Servicio.EstadoEnum;
import ar.com.ada.api.pagada.repos.PagoRepository;
import ar.com.ada.api.pagada.repos.ServicioRepository;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tSRepository;

    @Autowired
    ServicioRepository servicioRepo;

    @Autowired
    PagoRepository pagoRepo;

    public List<TipoServicio> listarTipoServicios() {

        return tSRepository.findAll();

    }

    public boolean crearTipoServicio(TipoServicio tipo) {

        if (tSRepository.existsById(tipo.getTipoServicioId()))
            return false;

        tSRepository.save(tipo);

        return true;
    }

    public TipoServicio buscarTipoServicioPorId(Integer tipoServicioId) {
        Optional<TipoServicio> oTipoServicio = tSRepository.findById(tipoServicioId);
        if (oTipoServicio.isPresent()) {
            return oTipoServicio.get();
        } else {
            return null;
        }
    }

    public ServicioValidacionEnum validarServicio(Servicio servicio) {

        if (servicio.getImporte().compareTo(new BigDecimal(0)) <= 0) {
            return ServicioValidacionEnum.IMPORTE_INVALIDO;

        }

        return ServicioValidacionEnum.OK;

    }

    public enum ServicioValidacionEnum {
        OK, IMPORTE_INVALIDO
    }

    public Servicio crearServicio(Servicio servicio) {

        // Si agreggo validacion justo antes de la creacion
        if (this.validarServicio(servicio) != ServicioValidacionEnum.OK)
            return servicio;

        return servicioRepo.save(servicio);

    }

    public Servicio buscarServicioPorId(int servicioId) {
        return servicioRepo.findByServicioId(servicioId);
    }

    public List<Servicio> listarServicios() {
        return servicioRepo.findAll();
    }

    public List<Servicio> listarServiciosPorEmpresaId(Integer empresaId) {
        return servicioRepo.findAllByEmpresaId(empresaId);
    }

    public List<Servicio> listarServiciosPendientesPorEmpresaId(Integer empresaId) {
        return servicioRepo.findAllPendientesByEmpresaId(empresaId);
    }

    public List<Servicio> PendientesPorEmpresaIdYDeudorId(Integer empresa, Integer deudor) {
        return servicioRepo.findAllPendientesByEmpresaIdYDeudorId(empresa, deudor);
    }

    public List<Servicio> historicoPorEmpresaIdYDeudorId(Integer empresaId, Integer deudorId) {
        return servicioRepo.findAllEmpresaIdYDeudorId(empresaId, deudorId);
    }

    public List<Servicio> listarPorCodigoBarras(String codigoBarras) {
        return servicioRepo.findAllByCodigoBarras(codigoBarras);
    }

    public Pago pagarServicio(Servicio servicio, Pago pago) {
        servicio.setPago(pago);
        servicio.setEstadoId(EstadoEnum.PAGADO);
        return pagoRepo.save(pago);

    }

    public Servicio anularServicio(Servicio servicio) {
        servicio.setEstadoId(EstadoEnum.ANULADO);
        return servicioRepo.save(servicio);
    }

	public String acortarInfoMedioPago(String infoMP) {
        String newInfo = new String();
        newInfo = "*";
        for (int i = (infoMP.length()-1) ; i > infoMP.length()-4 ; i--) {
            newInfo = newInfo + infoMP.charAt(i);
        }
		return newInfo;
	}

}