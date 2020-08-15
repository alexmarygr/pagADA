package ar.com.ada.api.pagada.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.ServicioRepository;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tSRepository;

    @Autowired
    ServicioRepository servicioRepo;

    public List<TipoServicio> listarTipoServicios() {

        return tSRepository.findAll();

    }

    public boolean crearTipoServicio(TipoServicio tipo) {

        if (tSRepository.existsById(tipo.getTipoServicioId()))
            return false;

        tSRepository.save(tipo);

        return true;
    }

    public TipoServicio buscarTipoServicioPorId(Integer tipoServicioId){
        Optional<TipoServicio> oTipoServicio = tSRepository.findById(tipoServicioId);
        if(oTipoServicio.isPresent()){
            return oTipoServicio.get();
        }else{
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

    public List<Servicio> listarServicios(){
        return servicioRepo.findAll();
    }

	public List<Servicio> listarServiciosPorEmpresaId(Integer empresaId) {
		return servicioRepo.findAllByEmpresaId(empresaId);
    }
    
    public List<Servicio> listarServiciosPendientesPorEmpresaId(Integer empresaId) {
		return servicioRepo.findAllPendientesByEmpresaId(empresaId);
	}

	public List<Servicio> PendientesPorEmpresaIdYDeudorId(Integer empresa, Integer deudor) {
		return servicioRepo.findAllPendientesByEmpresaIdYDeudorId(empresa,deudor);
	}

}