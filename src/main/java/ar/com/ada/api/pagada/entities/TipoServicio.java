package ar.com.ada.api.pagada.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {
    @Id
    @Column(name = "tipo_servicio_id")
    private Integer TipoServicioId;
    private String nombre;
    @JsonIgnore
    @OneToMany(mappedBy = "tipoServicio", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Servicio> serviciosEmitidos = new ArrayList<>();

    public Integer getTipoServicioId() {
        return TipoServicioId;
    }

    public void setTipoServicioId(Integer tipoServicioId) {
        TipoServicioId = tipoServicioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Servicio> getServiciosEmitidos() {
        return serviciosEmitidos;
    }

    public void setServiciosEmitidos(List<Servicio> serviciosEmitidos) {
        this.serviciosEmitidos = serviciosEmitidos;
    }

}