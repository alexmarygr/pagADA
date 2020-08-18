package ar.com.ada.api.pagada.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.OperacionPago;
import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.entities.OperacionPago.OperacionPagoEnum;
import ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;
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

    public Servicio buscarServicioPorId(Integer servicioId) {
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

    public Servicio pagarServicio(Servicio servicio, Pago pago) {
        servicio.setPago(pago);
        servicio.setEstadoId(EstadoEnum.PAGADO);
        return servicioRepo.save(servicio);

    }

    public Servicio anularServicio(Servicio servicio) {
        servicio.setEstadoId(EstadoEnum.ANULADO);
        return servicioRepo.save(servicio);
    }

	public OperacionPago realizarPago(Integer servicioId, BigDecimal importePagado, Date fechaPago, MedioPagoEnum medioPago,
			String infoMedioPago, String moneda) {

                OperacionPago opePago = new OperacionPago();

                Servicio servicio = buscarServicioPorId(servicioId);

                if(servicio == null){
                    opePago.setResultado(OperacionPagoEnum.RECHAZADO_SERVICIO_INEXISTENTE);
                    return opePago;
                }

                if(servicio.getEstadoId() != EstadoEnum.PENDIENTE){
                    opePago.setResultado(OperacionPagoEnum.RECHAZADO_SERVICIO_YA_PAGO);
                    return opePago;
                }
                // NO ACEPTAMOS PAGOS DIFERENTES AL TOTAL
                if(servicio.getImporte().compareTo(importePagado) != 0){
                    opePago.setResultado(OperacionPagoEnum.RECHAZADO_NO_ACEPTA_PAGO_PARCIAL);
                    return opePago;
                }

                // INSTANCIAMOS EL PAGO
                Pago pago = new Pago();
                pago.setImportePagado(importePagado);
                pago.setFechaPago(fechaPago);
                pago.setMedioPago(medioPago);
                pago.setInfoMedioPago(infoMedioPago);
                pago.setMoneda(moneda);
                 // AGREGAMOs el pago al servicio
                servicio.setPago(pago);
                // Cambiamos el estado de Pendiente a Pagado del Servicio
                servicio.setEstadoId(EstadoEnum.PAGADO);
                // Grabamos el servicio, porque en CASCADA, va a grabar el PAGO
                servicioRepo.save(servicio);
                // Devolvemos la estructura OperacionPago con la info Ok
                opePago.setPago(servicio.getPago());
                opePago.setResultado(OperacionPagoEnum.REALIZADO);
		        return opePago;
	}

	public Pago buscarPagoPorId(Integer pagoId) {
		return pagoRepo.findByPagoId(pagoId);
	}

	public List<Pago> buscarPagosDeEmpresa(Integer empresaId) {
		return pagoRepo.findPagosByEmpresaId(empresaId);
	}

	public List<Pago> buscarPagosDeDeudor(Integer deudorId) {
		return pagoRepo.findPagosByDeudorId(deudorId);
	}

}