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
    public ResponseEntity<GenericResponse> crearEmpresa(@RequestBody EmpresaRequest r){
        GenericResponse respuesta = new GenericResponse();
        Empresa empresa = empresaService.crearEmpresa(r.paisId,r.tipoIdImpositivo,r.idImpositivo,r.nombre);
        
        if ( empresa.getEmpresaId() != null){

            respuesta.isOk = true;
            respuesta.id = empresa.getEmpresaId();
            respuesta.message = "Empresa creada éxitosamente";
            return ResponseEntity.ok(respuesta);

        } else{

            respuesta.isOk = false;
            respuesta.message = "No se pudo crear la empresa";
            return ResponseEntity.badRequest().body(respuesta);
            
        }
    }



}