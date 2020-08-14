package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.repos.TipoServicioRepository;

@Service
public class ServicioService {

    @Autowired
    TipoServicioRepository tSRepository;

    public List<TipoServicio> listarTipoServicios() {

        return tSRepository.findAll();

    }

    public boolean crearTipoServicio(TipoServicio tipo) {

        if (tSRepository.existsById(tipo.getTipoServicioId()))
            return false;

        tSRepository.save(tipo);

        return true;
    }
}