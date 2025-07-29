package org.academico.springcloud.msvc.preventa.models.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Importar esta
import jakarta.persistence.*;
import org.academico.springcloud.msvc.preventa.models.PropiedadInmobiliaria;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoPreventa;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "preventas")
public class Preventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;

    // AÑADIDO: Estado de la preventa
    @Enumerated(EnumType.STRING)
    private EstadoPreventa estado; // "EnEvaluacion", "Aprobada", "Cancelada", "Finalizada"

    // AÑADIDO: Relación con ContratoVenta
    @OneToOne(mappedBy = "preventa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private ContratoVenta contratoVenta;

    // AÑADIDO: Relación con PropuestaPago
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las propuestas
    private List<PropuestaPago> propuestasPago;

    // AÑADIDO: Relación con VisitaProgramada
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las visitas
    private List<VisitaProgramada> visitasProgramadas;

    //relación con Usuario(agente y cliente)
    @Column(name="agente_id",unique = true)
    private Long usuarioAgenteId;

    @Column(name = "cliente_id",unique=true)
    private Long usuarioClienteId;

    // Campo para la propiedad
    @Column(name = "propiedad_id")
    @NotNull
    private Long propiedadId;

    @Transient
    private PropiedadInmobiliaria propiedad;

    public Preventa() {
        this.estado = EstadoPreventa.EN_EVALUACION; // Estado inicial
        this.fechaInicio = LocalDate.now();
        this.contratoVenta = null; // Inicialmente no hay contrato
        this.propuestasPago = new ArrayList<>();
        this.visitasProgramadas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public EstadoPreventa getEstado() {
        return estado;
    }
    public void setEstado(EstadoPreventa estado) {
        this.estado = estado;
    }
    public ContratoVenta getContratoVenta() {
        return contratoVenta;
    }
    public void setContratoVenta(ContratoVenta contratoVenta) {
        this.contratoVenta = contratoVenta;
    }
    public List<PropuestaPago> getPropuestasPago() {
        return propuestasPago;
    }
    public void setPropuestasPago(List<PropuestaPago> propuestasPago) {this.propuestasPago = propuestasPago;}
    public List<VisitaProgramada> getVisitasProgramadas() {
        return visitasProgramadas;
    }
    public void setVisitasProgramadas(List<VisitaProgramada> visitasProgramadas) {this.visitasProgramadas = visitasProgramadas;}
    public Long getUsuarioAgenteId() {
        return usuarioAgenteId;
    }
    public void setUsuarioAgenteId(Long usuarioAgenteId) {
        this.usuarioAgenteId = usuarioAgenteId;
    }
    public Long getUsuarioClienteId() {
        return usuarioClienteId;
    }
    public void setUsuarioClienteId(Long usuarioClienteId) {
        this.usuarioClienteId = usuarioClienteId;
    }
    public Long getPropiedadId() {return propiedadId;}
    public void setPropiedadId(Long propiedadId) {this.propiedadId = propiedadId;}
    public PropiedadInmobiliaria getPropiedad() {return propiedad;}
    public void setPropiedad(PropiedadInmobiliaria propiedad) {this.propiedad = propiedad;}

    public void aprobarPreventa() {
        if (this.estado != EstadoPreventa.EN_EVALUACION) {
            throw new IllegalStateException("La preventa solo puede ser aprobada si está EN EVALUACION. Estado actual: " + this.estado);
        }
        this.estado = EstadoPreventa.APROBADA;
        System.out.println("Preventa " + this.id + " marcada como APROBADA.");
    }

    public void addPropuestaPago(PropuestaPago propuesta) {
        this.propuestasPago.add(propuesta);
        propuesta.setPreventa(this);
    }

    public void addVisitaProgramada(VisitaProgramada visita) {
        this.visitasProgramadas.add(visita);
        visita.setPreventa(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preventa preventa = (Preventa) o;
        return Objects.equals(id, preventa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Optional<PropuestaPago> findPropuestaPagoById(Long propuestaId) {
        return this.propuestasPago.stream()
                .filter(p -> Objects.equals(p.getId(), propuestaId))
                .findFirst();
    }

    public Optional<VisitaProgramada> findVisitaProgramadaById(Long visitaId) {
        return this.visitasProgramadas.stream()
                .filter(v -> Objects.equals(v.getId(), visitaId))
                .findFirst();
    }
}