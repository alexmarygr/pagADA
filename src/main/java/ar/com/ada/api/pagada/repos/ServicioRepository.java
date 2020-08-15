package ar.com.ada.api.pagada.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.pagada.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio,Integer>{
    @Query("select s from Servicio s where s.empresa.empresaId = :empresaId")
    public List<Servicio> findAllByEmpresaId(Integer empresaId);

    @Query("select s from Servicio s where s.empresa.empresaId = :empresaId and s.estadoId = 0")
    public List<Servicio> findAllPendientesByEmpresaId(Integer empresaId);

    @Query("select s from Servicio s where s.empresa.empresaId = :empresaId and s.deudor.deudorId = :deudorId and s.estadoId = 0")
	public List<Servicio> findAllPendientesByEmpresaIdYDeudorId(Integer empresaId, Integer deudorId);

    @Query("select s from Servicio s where s.empresa.empresaId = :empresaId and s.deudor.deudorId = :deudorId")
	public List<Servicio> findAllEmpresaIdYDeudorId(Integer empresaId, Integer deudorId);

}