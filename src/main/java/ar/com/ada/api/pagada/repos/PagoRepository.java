package ar.com.ada.api.pagada.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.pagada.entities.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago,Integer> {
	
	@Query("select p from Pago p where p.servicio.servicioId = :servicioId")
	Pago findPago(Integer servicioId);

}