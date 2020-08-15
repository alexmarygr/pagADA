package ar.com.ada.api.pagada.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.entities.Pais.TipoIdImpositivoEnum;
import ar.com.ada.api.pagada.repos.DeudorRepository;

@Service
public class DeudorService {

    @Autowired
    DeudorRepository deudorRepo;

	public List<Deudor> listarDeudores() {
		return deudorRepo.findAll();
    }
    
    public void guardar(Deudor deudor){
        deudorRepo.save(deudor);
    }

	public Deudor crearDeudor(Integer paisId, TipoIdImpositivoEnum tipoIdImpositivo, String idImpositivo,
			String nombre) {
        
        Deudor deudor = new Deudor();
        deudor.setPaisId(paisId);
        deudor.setTipoIdImpositivo(tipoIdImpositivo);
        deudor.setIdImpositivo(idImpositivo);
        deudor.setNombre(nombre);
        guardar(deudor);
        return deudor;
    }
    
    public DeudorValidacionEnum validarDeudorInfo(Integer paisId, TipoIdImpositivoEnum tipoIdImpositivo,
            String idImpositivo, String nombre) {
        // Si es nulo, error
        if (idImpositivo == null)
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        // ID impositivo al menos de 11 digitos y maximo 20
        if (!(idImpositivo.length() >= 11 && idImpositivo.length() <= 20))
            return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        
        for (char caracter : idImpositivo.toCharArray()) {
            if (!Character.isDigit(caracter))
                return DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO;

        }

        if (nombre == null)
            return DeudorValidacionEnum.NOMBRE_INVALIDO;

        if (nombre.length() > 100)
            return DeudorValidacionEnum.NOMBRE_INVALIDO;

        // Si llego hassta aqui, es que todo lo de arriba, era valido
        return DeudorValidacionEnum.OK;
    }

    public enum DeudorValidacionEnum {
        OK, // Cuando esta todo validado ok
        NOMBRE_INVALIDO, // Nombre tenga algun problema
        ID_IMPOSITIVO_INVALIDO // ID impositivo tenga un problema
    }

    public Deudor buscarDeudorPorId(Integer deudorId){
        Optional <Deudor> oDeudor = deudorRepo.findById(deudorId);
        if (oDeudor.isPresent()){
            return oDeudor.get();
        }else{
            return null;
        }
    }

}