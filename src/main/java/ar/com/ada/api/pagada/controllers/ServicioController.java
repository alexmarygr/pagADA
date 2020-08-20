package ar.com.ada.api.pagada.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.com.ada.api.pagada.entities.*;
import ar.com.ada.api.pagada.entities.Servicio.EstadoEnum;
import ar.com.ada.api.pagada.models.request.ActualizarServicioRequest;
import ar.com.ada.api.pagada.models.request.InfoPagoRequest;
import ar.com.ada.api.pagada.models.request.ServicioRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.DeudorService;
import ar.com.ada.api.pagada.services.EmpresaService;
import ar.com.ada.api.pagada.services.ServicioService;
import ar.com.ada.api.pagada.services.ServicioService.ServicioValidacionEnum;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class ServicioController {

    @Autowired
    ServicioService servicioService;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    DeudorService deudorService;

    @PostMapping("/api/servicios")
    public ResponseEntity<GenericResponse> crearServicio(@RequestBody ServicioRequest sr) {

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
        if (validacionResultado != ServicioValidacionEnum.OK) {
            gr.isOk = false;
            gr.message = "Hubo un error en la validacion del servicio" + validacionResultado;
            return ResponseEntity.badRequest().body(gr);
        }

        servicioService.crearServicio(servicio);
        // Quiero ver si en la base de datos se creó el id
        if (servicio.getServicioId() == null) {

            gr.isOk = false;
            gr.message = "No se pudo crear el servicio";
            return ResponseEntity.badRequest().body(gr);

        } else {

            gr.isOk = true;
            gr.id = servicio.getServicioId();
            gr.message = "Se creó el servicio éxitosamente.";
            return ResponseEntity.ok(gr);

        }

    }

    @GetMapping("/api/servicios")
    public ResponseEntity<List<Servicio>> listarServicios(
            @RequestParam(name = "empresa", required = false) Integer empresa,
            @RequestParam(name = "deudor", required = false) Integer deudor,
            @RequestParam(name = "historico", required = false) boolean historico,
            @RequestParam(name = "codigo", required = false) String codigo) {

        List<Servicio> servicios = new ArrayList<>();

        if (codigo != null) {
            servicios = servicioService.listarPorCodigoBarras(codigo);
        } else if (empresa != null && deudor == null) {
            servicios = servicioService.listarServiciosPendientesPorEmpresaId(empresa);
        } else if (empresa != null && deudor != null && !historico) {
            servicios = servicioService.PendientesPorEmpresaIdYDeudorId(empresa, deudor);
        } else if (empresa != null && deudor != null && historico) {
            servicios = servicioService.historicoPorEmpresaIdYDeudorId(empresa, deudor);
        } else {
            servicios = servicioService.listarServicios();
        }
        return ResponseEntity.ok(servicios);
    }

    @PostMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> pagarServicioPorId(@PathVariable Integer id,
            @RequestBody InfoPagoRequest pr) {
        GenericResponse r = new GenericResponse();
        OperacionPago pagoResult = servicioService.realizarPago(id, pr.importePagado, pr.fechaPago, pr.medioPago,
                pr.infoMedioPago, pr.moneda);

        switch (pagoResult.getResultado()) {
            case RECHAZADO_NO_ACEPTA_PAGO_PARCIAL:
                r.isOk = false;
                r.message = "No acepta pago parcial";

                return ResponseEntity.badRequest().body(r);

            case RECHAZADO_SERVICIO_INEXISTENTE:

                r.isOk = false;
                r.message = "Servicio inexistente";

                return ResponseEntity.badRequest().body(r);

            case RECHAZADO_SERVICIO_YA_PAGO:

                r.isOk = false;
                r.message = "Servicio ya pago";

                return ResponseEntity.badRequest().body(r);

            case ERROR_INESPERADO:

                r.isOk = false;
                r.message = "Error inesperado";

                return ResponseEntity.badRequest().body(r);

            case REALIZADO:

                r.isOk = true;
                r.id = pagoResult.getPago().getPagoId();
                r.message = "se realizo el pago con exito";
                return ResponseEntity.ok(r);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> actualizarServicio(@PathVariable Integer id,
            @RequestBody ActualizarServicioRequest mr) {

        GenericResponse gr = new GenericResponse();

        Servicio servicio = servicioService.buscarServicioPorId(id);

        if (servicio == null){
            gr.isOk = false;
            gr.message = "Servicio no encontrado.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gr);
            

        }

        servicio.setImporte(mr.importe);
        servicio.setFechaVencimiento(mr.vencimiento);

        ServicioValidacionEnum resultado = servicioService.validarServicio(servicio);

        if (resultado != ServicioValidacionEnum.OK) {
            gr.isOk = false;
            gr.message = "Hubo un error en la validacion del servicio "+ resultado;
            return ResponseEntity.badRequest().body(gr);

        } else {
            servicioService.grabar(servicio);
            gr.isOk = true;
            gr.id = servicio.getServicioId();
            gr.message = "Servicio actualizado correctamente.";
            return ResponseEntity.ok(gr);
        }

    }

    @DeleteMapping("/api/servicios/{id}")
    public ResponseEntity<GenericResponse> anularServicio(@PathVariable Integer id) {

        GenericResponse gr = new GenericResponse();

        Servicio servicio = servicioService.buscarServicioPorId(id);

        if (servicio == null){
            gr.isOk = false;
            gr.message = "Servicio no encontrado.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gr);// Error http 404
            

        }

        if (servicio.getEstadoId() == EstadoEnum.PAGADO) {
            gr.isOk = false;
            gr.id = servicio.getServicioId();
            gr.message = "No se puede anular un servicio pago";

            return ResponseEntity.badRequest().body(gr); // Error http 400
        }

        servicioService.anularServicio(servicio);

        if (servicio.getEstadoId() != EstadoEnum.ANULADO) {
            gr.isOk = false;
            gr.message = "Hubo un error al borrar el servicio.";
            return ResponseEntity.badRequest().body(gr);
        } else {
            gr.isOk = true;
            gr.id = servicio.getServicioId();
            gr.message = "Servicio anulado correctamente.";
            return ResponseEntity.ok(gr);
        }

    }

}