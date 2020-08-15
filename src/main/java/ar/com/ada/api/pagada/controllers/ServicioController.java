package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Servicio;
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
    @RequestParam(name = "historico", required = false) boolean historico){
        
        List<Servicio> servicios = new ArrayList<>();

        if(empresa == null){
            servicios = servicioService.listarServicios();
        }else if(deudor == null){
            servicios = servicioService.listarServiciosPendientesPorEmpresaId(empresa);
        }else if (historico == true){
            servicios = servicioService.historicoPorEmpresaIdYDeudorId(empresa,deudor);
        }else{
            servicios = servicioService.PendientesPorEmpresaIdYDeudorId(empresa,deudor);
        }
        return ResponseEntity.ok(servicios);
    }

}