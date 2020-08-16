package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.entities.Empresa;
import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.services.DeudorService;
import ar.com.ada.api.pagada.services.EmpresaService;
import ar.com.ada.api.pagada.services.PagoService;
import ar.com.ada.api.pagada.services.ServicioService;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.models.response.PagoResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PagoController {

    @Autowired
    ServicioService servicioService;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    DeudorService deudorService;

    @Autowired
    PagoService pagoService;

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
        pagoResponse.infoMedioPago = pagoService.acortarInfoMedioPago(infoMP);
        return ResponseEntity.ok(pagoResponse);
    }

    @GetMapping("/api/pagos")
    public ResponseEntity<?> listarPagosDeEmpresaODeudor(
            @RequestParam(name = "empresa", required = false) Integer empresa,
            @RequestParam(name = "deudor", required = false) Integer deudor){

                List<PagoResponse> pagosReponse = new ArrayList<>();

                if(empresa == null && deudor == null){

                    GenericResponse gr = new GenericResponse();
                    gr.isOk = false;
                    gr.message = "Por favor cargar empresa o deudor.";
                    return ResponseEntity.badRequest().body(gr);

                }else if (empresa != null && deudor == null){

                    Empresa empresaEncontrada = empresaService.buscarEmpresaPorId(empresa);
                    List<Pago> pagos = pagoService.buscarPagosPorEmpresa(empresaEncontrada);
                    pagosReponse = pagoService.convertirPagosAResponse(pagos);
                    return ResponseEntity.ok(pagosReponse);

                }else{
                    Deudor deudorEncontrado = deudorService.buscarDeudorPorId(deudor);
                    List<Pago> pagosDeudor = pagoService.buscarPagosPorDeudor(deudorEncontrado);
                    pagosReponse = pagoService.convertirPagosAResponse(pagosDeudor);
                    return ResponseEntity.ok(pagosReponse);
                }

            }

}