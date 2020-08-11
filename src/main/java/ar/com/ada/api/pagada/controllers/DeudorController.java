package ar.com.ada.api.pagada.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.models.request.DeudorRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.DeudorService;

@RestController
public class DeudorController {

    @Autowired
    DeudorService deudorService;

    @GetMapping("/api/deudores")
    public ResponseEntity<List<Deudor>> listarDeudores(){
        List<Deudor> deudores = deudorService.listarDeudores();
        return ResponseEntity.ok(deudores);
    }

    @PostMapping("/api/deudores")
    public ResponseEntity<GenericResponse> crearDeudor(@RequestBody DeudorRequest d){
        GenericResponse respuesta = new GenericResponse();

        Deudor deudor = deudorService.crearDeudor(d.paisId,d.tipoIdImpositivo,d.idImpositivo,d.nombre);

        if( deudor.getDeudorId() != null){

            respuesta.isOk = true;
            respuesta.id = deudor.getDeudorId();
            respuesta.message = "Deudor cargado con éxito.";
            return ResponseEntity.ok(respuesta);

        }else{
            respuesta.isOk = false;
            respuesta.message = "No se pudo cargar el deudor.";
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

}