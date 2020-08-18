package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.services.ServicioService;
import ar.com.ada.api.pagada.models.response.PagoResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PagoController {

    @Autowired
    ServicioService servicioService;

    @GetMapping("/api/pagos/{id}")
    public ResponseEntity<PagoResponse> buscarPago(@PathVariable Integer id) {
        
        Pago pago = servicioService.buscarPagoPorId(id);

        PagoResponse pagoResponse = new PagoResponse();
        pagoResponse = pago.convertirAPagoResponse();

        return ResponseEntity.ok(pagoResponse);
    }

    @GetMapping("/api/pagos")
    public ResponseEntity<List<PagoResponse>> listarPagosDeEmpresaODeudor(
            @RequestParam(name = "empresa", required = false) Integer empresa,
            @RequestParam(name = "deudor", required = false) Integer deudor){

                List<PagoResponse> pagosReponse = new ArrayList<>();

                List<Pago> pagos;

                if(empresa == null && deudor == null || empresa != null && deudor != null){

                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

                }else if (empresa != null && deudor == null){

                    pagos = servicioService.buscarPagosDeEmpresa(empresa);
                    
                }else{

                    pagos = servicioService.buscarPagosDeDeudor(deudor);

                }
                
                for (Pago pago : pagos) {
                    PagoResponse pagoR = new PagoResponse();
                    pagoR = pago.convertirAPagoResponse();
                    pagosReponse.add(pagoR);
                }

                return ResponseEntity.ok(pagosReponse);

            }

}