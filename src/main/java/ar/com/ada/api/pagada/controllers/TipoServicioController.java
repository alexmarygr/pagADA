package ar.com.ada.api.pagada.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.TipoServicio;
import ar.com.ada.api.pagada.models.request.TipoServicioRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.TipoServicioService;

@RestController
public class TipoServicioController {

    @Autowired
    TipoServicioService tipoServService;

    @GetMapping("/api/tipos-servicios")
    public ResponseEntity<List<TipoServicio>> listarTipoServicios(){
        List<TipoServicio> tipoServicios = tipoServService.listarTipoServicios();
        return ResponseEntity.ok(tipoServicios);
    }

    @PostMapping("/api/tipos-servicios")
    public ResponseEntity<GenericResponse> crearTipoServicio(@RequestBody TipoServicioRequest t){

        GenericResponse respuesta = new GenericResponse();

        TipoServicio tipoS = tipoServService.crearTipoServicio(t.TipoServicioId, t.nombre);

        if(tipoS.getTipoServicioId() != null){

            respuesta.isOk = true;
            respuesta.id = tipoS.getTipoServicioId();
            respuesta.message = "El Tipo Servicio fue creado con éxito";
            return ResponseEntity.ok(respuesta);

        }else{

            respuesta.isOk = false;
            respuesta.message = "No se pudo crear el servicio.";
            return ResponseEntity.badRequest().body(respuesta);

        }
        
    }
}