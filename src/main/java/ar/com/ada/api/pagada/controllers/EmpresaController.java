package ar.com.ada.api.pagada.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.pagada.entities.Empresa;
import ar.com.ada.api.pagada.models.request.EmpresaRequest;
import ar.com.ada.api.pagada.models.response.GenericResponse;
import ar.com.ada.api.pagada.services.EmpresaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ar.com.ada.api.pagada.services.EmpresaService.EmpresaValidacionEnum;

@RestController
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;

    @GetMapping("/api/empresas")
    public ResponseEntity<List<Empresa>> listarEmpresas() {

        List<Empresa> empresas = empresaService.listarEmpresas();

        return ResponseEntity.ok(empresas);
    }

    @PostMapping("/api/empresas")
    public ResponseEntity<GenericResponse> crearEmpresa(@RequestBody EmpresaRequest empR) {
        GenericResponse gr = new GenericResponse();

        // to do: hacer validaciones y crear la empresa a traves del service

        Empresa emp = new Empresa();
        emp.setPaisId(empR.paisId);
        emp.setTipoIdImpositivo(empR.tipoIdImpositivo);
        emp.setIdImpositivo(empR.idImpositivo);
        emp.setNombre(empR.nombre);

        // Este metodo no es tan extensible que el de abajo
        // porque depeden de los parametros
        // empresaService.validarNombreEIdImpositivo(empR.nombre, empR.idImpositivo);

        EmpresaValidacionEnum resultadoValidacion = empresaService.validarEmpresa(emp);
        if (resultadoValidacion != EmpresaValidacionEnum.OK) {
            gr.isOk = false;
            gr.message = "No se pudo validar la empresa " + resultadoValidacion.toString();

            return ResponseEntity.badRequest().body(gr); // http 400
        }

        empresaService.crearEmpresa(emp);

        // O haciendo esto
        // Empresa empresa = empresaService.crearEmpresa(empR.paisId,
        // empR.tipoIdImpositivo, empR.idImpositivo, empR.nombre);

        if (emp.getEmpresaId() != null) {
            gr.isOk = true;
            gr.id = emp.getEmpresaId();
            gr.message = "Empresa creada con exito";
            return ResponseEntity.ok(gr);
        }

        gr.isOk = false;
        gr.message = "No se pudo crear la empresa";

        return ResponseEntity.badRequest().body(gr); // http 400

    }



}