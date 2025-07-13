package org.academico.springcloud.msvc.norma.models.entity;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.norma.models.enums.TipoNorma;
import org.academico.springcloud.msvc.norma.models.valueobjects.Fecha;

@Entity
@Table(name = "normas")
public class Norma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNorma;

    @Embedded
    private Fecha fecha;

    @Enumerated(EnumType.STRING)
    private TipoNorma tipo;

    private String estadoNorma;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    public Norma() {}

    public Norma(Fecha fecha, TipoNorma tipo, String estadoNorma, String descripcion) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.estadoNorma = estadoNorma;
        this.descripcion = descripcion;
    }

    public Long getIdNorma() {
        return idNorma;
    }

    public void setIdNorma(Long idNorma) {
        this.idNorma = idNorma;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public TipoNorma getTipo() {
        return tipo;
    }

    public void setTipo(TipoNorma tipo) {
        this.tipo = tipo;
    }

    public String getEstadoNorma() {
        return estadoNorma;
    }

    public void setEstadoNorma(String estadoNorma) {
        this.estadoNorma = estadoNorma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}