package ar.com.ada.api.pagada.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.services.ServicioService;
import ar.com.ada.api.pagada.models.response.PagoResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class PagoController {

    @Autowired
    ServicioService servicioService;

    @GetMapping("/api/pagos/{id}")
    public ResponseEntity<PagoResponse> buscarPago(@PathVariable int id) {
        PagoResponse pagoResponse = new PagoResponse();
        Servicio servicio = servicioService.buscarServicioPorId(id);
        pagoResponse.empresa_id = servicio.getEmpresa().getEmpresaId();
        pagoResponse.nombre_empresa = servicio.getEmpresa().getNombre();
        pagoResponse.deudor_id = servicio.getDeudor().getDeudorId();
        pagoResponse.nombre_deudor = servicio.getDeudor().getNombre();
        pagoResponse.comprobanteDePago = servicio.getPago().getPagoId();
        pagoResponse.fecha = servicio.getPago().getFechaPago();
        pagoResponse.importePagado = servicio.getPago().getImportePagado();
        pagoResponse.medioPago = servicio.getPago().getMedioPago();
        String infoMP = servicio.getPago().getInfoMedioPago();
        pagoResponse.infoMedioPago = servicioService.acortarInfoMedioPago(infoMP);
        return ResponseEntity.ok(pagoResponse);
    }

}