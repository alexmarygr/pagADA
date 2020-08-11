package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class TipoServicioService {

    @Autowired
    TipoServicioRepository tServicioRepo;

    public List<TipoServicio> listarTipoServicios() {
		return tServicioRepo.findAll();
    }
    
    public void guardar(TipoServicio tipoServicio){
		tServicioRepo.save(tipoServicio);
	}

	public TipoServicio crearTipoServicio(Integer tipoServicioId, String nombre) {
        TipoServicio nuevoTipoServicio = new TipoServicio();
        nuevoTipoServicio.setTipoServicioId(tipoServicioId);
        nuevoTipoServicio.setNombre(nombre);
        guardar(nuevoTipoServicio);
		return nuevoTipoServicio;
	}



}