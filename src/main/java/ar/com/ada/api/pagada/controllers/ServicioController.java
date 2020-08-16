package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.models.request.PayloadReuqest;
import ar.com.ada.api.pagada.models.request.ServicioRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.DeudorService;
import ar.com.ada.api.pagada.services.EmpresaService;
import ar.com.ada.api.pagada.services.ServicioService;
import ar.com.ada.api.pagada.services.ServicioService.ServicioValidacionEnum;

@RestController
public class ServicioController {

    @Autowired
    ServicioService servicioService;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    DeudorService deudorService;

    @PostMapping("/api/servicios")
    public ResponseEntity<GenericResponse> crearServicio(@RequestBody ServicioRequest sr){

        GenericResponse gr = new GenericResponse();

        Servicio servicio = new Servicio();
        servicio.setEmpresa(empresaService.buscarEmpresaPorId(sr.empresaId));
        servicio.setDeudor(deudorService.buscarDeudorPorId(sr.deudorId));
        servicio.setTipoServicio(servicioService.buscarTipoServicioPorId(sr.tipoServicioId));
        servicio.setTipoComprobante(sr.tipoComprobanteId);
        servicio.setNumero(sr.numero);
        servicio.setFechaEmision(sr.fechaEmision);
        servicio.setFechaVencimiento(sr.fechaVencimiento);
        servicio.setImporte(sr.importe);
        servicio.setMoneda(sr.moneda);
        servicio.setCodigoBarras(sr.codigoBarras);
        servicio.setEstadoId(sr.estadoId);

        ServicioValidacionEnum validacionResultado = servicioService.validarServicio(servicio);
        if(validacionResultado != ServicioValidacionEnum.OK){
            gr.isOk = false;
            gr.message = "Hubo un error en la validacion del servicio"  + validacionResultado;
            return ResponseEntity.badRequest().body(gr);
        }

        servicioService.crearServicio(servicio);
        //Quiero ver si en la base de datos se creó el id
        if (servicio.getServicioId() == null){

            gr.isOk = false;
            gr.message = "No se pudo crear el servicio";
            return ResponseEntity.badRequest().body(gr);

        } else{

            gr.isOk = true;
            gr.id = servicio.getServicioId();
            gr.message = "Se creó el servicio éxitosamente.";
            return ResponseEntity.ok(gr);

        }
        
    }


    @GetMapping("/api/servicios")
    public ResponseEntity<List<Servicio>> listarServicios( @RequestParam(name = "empresa", required = false) Integer empresa, 
    @RequestParam(name = "deudor", required = false) Integer deudor,
    @RequestParam(name = "historico", required = false) boolean historico,
    @RequestParam(name = "codigo", required = false) String codigo){
        
        List<Servicio> servicios = new ArrayList<>();

        if(codigo != null){
            servicios = servicioService.listarPorCodigoBarras(codigo);
        }else if(empresa != null && deudor == null){
            servicios = servicioService.listarServiciosPendientesPorEmpresaId(empresa);
        }else if(empresa != null && deudor != null && historico == false){
            servicios = servicioService.PendientesPorEmpresaIdYDeudorId(empresa,deudor);
        }else if (empresa != null && deudor != null && historico == true){
            servicios = servicioService.historicoPorEmpresaIdYDeudorId(empresa,deudor);
        }else{
            servicios = servicioService.listarServicios();
        }
        return ResponseEntity.ok(servicios);
    }

    @PostMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> pagarServicioPorId(@PathVariable int id, @RequestBody PayloadReuqest pr) {
        GenericResponse gr = new GenericResponse();
        Servicio servicioPagado = servicioService.buscarServicioPorId(id);
        Pago pago = new Pago();
        pago.setImportePagado(pr.importePagado);
        pago.setFechaPago(pr.fechaPago);
        pago.setMedioPago(pr.medioPago);
        pago.setInfoMedioPago(pr.infoMedioPago);
        pago.setMoneda(pr.moneda);
        pago.setServicio(servicioPagado);
        servicioService.pagarServicio(servicioPagado,pago);
        if(pago.getPagoId() == null){
            gr.isOk = false;
            gr.message = "No se pudo cargar el pago.";
            return ResponseEntity.badRequest().body(gr);
        }else{
            gr.isOk = true;
            gr.id = pago.getPagoId();
            gr.message = "Pago cargado éxitosamente.";
            return ResponseEntity.ok(gr);
        }
    }
    

    

}