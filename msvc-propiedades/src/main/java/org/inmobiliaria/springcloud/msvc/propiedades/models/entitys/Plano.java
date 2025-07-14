package org.inmobiliaria.springcloud.msvc.propiedades.models.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.EstadoPlano;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.TipoPlano;

@Entity
@Table(name = "planos")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private Long idPlano;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoPlano tipoPlano;

    @Column(name = "archivo_ruta", nullable = false)
    private String archivoRuta;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPlano estadoPlano;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "version", nullable = false)
    private String version;

    @ManyToOne
    @JoinColumn(name = "id_expediente", nullable = false)
    @JsonBackReference
    private Expediente expediente;

    protected Plano() {}

    public TipoPlano getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(TipoPlano tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public EstadoPlano getEstadoPlano() {
        return estadoPlano;
    }

    public void setEstadoPlano(EstadoPlano estadoPlano) {
        this.estadoPlano = estadoPlano;
    }

    public String getArchivoRuta() {
        return archivoRuta;
    }

    public void setArchivoRuta(String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public Long getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(Long idPlano) {
        this.idPlano = idPlano;
    }
}
