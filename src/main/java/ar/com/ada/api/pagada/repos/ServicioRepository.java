package ar.com.ada.api.pagada.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.pagada.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio,Integer>{

}