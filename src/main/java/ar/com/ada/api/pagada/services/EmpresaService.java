package ar.com.ada.api.pagada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Empresa;
import ar.com.ada.api.pagada.entities.Pais.TipoIdImpositivoEnum;
import ar.com.ada.api.pagada.repos.EmpresaRepository;

@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepo;

	public List<Empresa> listarEmpresas() {

		return empresaRepo.findAll();
	}

	public void guardar(Empresa empresa){
		empresaRepo.save(empresa);
	}

	public Empresa crearEmpresa(Integer paisId, TipoIdImpositivoEnum tipoIdImpositivo, String idImpositivo,
			String nombre) {
		Empresa empresa =  new Empresa();
		empresa.setPaisId(paisId);
		empresa.setTipoIdImpositivo(tipoIdImpositivo);
		empresa.setIdImpositivo(idImpositivo);
		empresa.setNombre(nombre);
		guardar(empresa);

		return empresa;
	}

}