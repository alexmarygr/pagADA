package ar.com.ada.api.pagada.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.pagada.entities.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago,Integer>  {

    Pago findByPagoId(Integer pagoId);
    
    @Query("select p from Pago p where p.servicio.empresa.empresaId = :empresaId")
    List<Pago> findPagosByEmpresaId(Integer empresaId);
    
    @Query("select p from Pago p where p.servicio.deudor.deudorId = :deudorId")
	List<Pago> findPagosByDeudorId(Integer deudorId);

}