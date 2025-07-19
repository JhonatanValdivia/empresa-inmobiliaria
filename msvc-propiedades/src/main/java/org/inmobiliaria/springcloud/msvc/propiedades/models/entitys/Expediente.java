package org.inmobiliaria.springcloud.msvc.propiedades.models.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.EstadoExpediente;
import org.inmobiliaria.springcloud.msvc.propiedades.models.enums.TipoExpediente;
import org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects.Fecha;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expedientes")
public class Expediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expediente")
    private Long idExpediente;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoExpediente tipoExpediente;;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoExpediente estadoExpediente;

    @Column(name = "observaciones")
    private String observaciones;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "fecha_dia")),
            @AttributeOverride(name = "mes", column = @Column(name = "fecha_mes")),
            @AttributeOverride(name = "anio", column = @Column(name = "fecha_anio"))
    })
    private Fecha fecha;

    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Plano> planos = new ArrayList<>();


    @OneToOne(mappedBy = "expediente")
    @JsonBackReference
    private PropiedadInmobiliaria propiedad;

    protected Expediente() {}


    public TipoExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TipoExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    public EstadoExpediente getEstadoExpediente() {
        return estadoExpediente;
    }

    public void setEstadoExpediente(EstadoExpediente estadoExpediente) {
        this.estadoExpediente = estadoExpediente;
    }

    public Long getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Long idExpediente) {
        this.idExpediente = idExpediente;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public List<Plano> getPlanos() {
        return planos;
    }

    public void setPlanos(List<Plano> planos) {
        this.planos = planos;
    }

    public PropiedadInmobiliaria getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(PropiedadInmobiliaria propiedad) {
        this.propiedad = propiedad;
    }


    //metodosssssssss

    public void agregarPlano(Plano plano) {
        plano.setExpediente(this);
        this.planos.add(plano);
    }

    public void removerPlano(Long planoId) {
        Plano encontrado = null;
        for (Plano p : planos) {
            if (p.getIdPlano().equals(planoId)) {
                encontrado = p;
                break;
            }
        }
        if (encontrado != null) {
            encontrado.setExpediente(null);
            planos.remove(encontrado);
        }
    }

    public void editarPlano(Plano planoEditado) {


        for (int i = 0; i < planos.size(); i++) {
            Plano actual = planos.get(i);
            if (actual.getIdPlano().equals(planoEditado.getIdPlano())) {
                planoEditado.setExpediente(this);
                planos.set(i, planoEditado); // reemplaza el plano existente
                return;
            }
        }


    }



}

