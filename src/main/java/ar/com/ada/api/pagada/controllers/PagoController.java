package ar.com.ada.api.pagada.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.services.ServicioService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class PagoController {

    @Autowired
    ServicioService servicioService;

    @GetMapping("/api/pagos/{id}")
    public ResponseEntity<Pago> buscarPago(@PathVariable int id) {
        Servicio servicio = servicioService.buscarServicioPorId(id);
        Pago pago = servicio.getPago();
        return ResponseEntity.ok(pago);
    }
    

}