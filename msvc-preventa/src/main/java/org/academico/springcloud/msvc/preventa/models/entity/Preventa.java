package org.academico.springcloud.msvc.preventa.models.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // Importar esta
import jakarta.persistence.*;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoPreventa;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;

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

    @Enumerated(EnumType.STRING)
    private EstadoPreventa estado; // "EnEvaluacion", "Aprobada", "Cancelada", "Finalizada"

    @OneToOne(mappedBy = "preventa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private ContratoVenta contratoVenta;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las propuestas
    private List<PropuestaPago> propuestasPago;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las visitas
    private List<VisitaProgramada> visitasProgramadas;

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

    public void setPropuestasPago(List<PropuestaPago> propuestasPago) {
        this.propuestasPago = propuestasPago;
    }

    public List<VisitaProgramada> getVisitasProgramadas() {
        return visitasProgramadas;
    }

    public void setVisitasProgramadas(List<VisitaProgramada> visitasProgramadas) {
        this.visitasProgramadas = visitasProgramadas;
    }

    // Métodos de negocio del agregado (Manejan la lógica interna de la Preventa)

    public VisitaProgramada programarVisita(LocalDate fechaVisita) {
        VisitaProgramada visita = new VisitaProgramada();
        visita.setFecha(fechaVisita);
        visita.setPreventa(this); // Asegura la relación bidireccional
        this.visitasProgramadas.add(visita);
        System.out.println("Visita programada para la preventa " + this.id + " en la fecha: " + fechaVisita);
        return visita;
    }

    // --- MÉTODOS DE NEGOCIO MODIFICADOS ---
    public ContratoVenta registrarContrato(TipoContrato tipoContrato, LocalDate fechaFirma) {
        if (this.contratoVenta != null) {
            throw new IllegalStateException("Esta preventa ya tiene un contrato registrado.");
        }
        ContratoVenta contrato = new ContratoVenta();
        contrato.setTipoContrato(tipoContrato);
        contrato.setFechaFirma(fechaFirma);
        contrato.setEstado("Firmado");
        contrato.setPreventa(this); // Asegura la relación bidireccional
        this.contratoVenta = contrato;
        return contrato;
    }

    public PropuestaPago registrarPago(BigDecimal monto, LocalDate fecha, MetodoPago metodo) {
        PropuestaPago pago = new PropuestaPago();
        pago.setMonto(monto);
        pago.setFecha(fecha);
        pago.setMetodoPago(metodo);
        pago.setPreventa(this); // Asegura la relación bidireccional
        this.propuestasPago.add(pago);
        System.out.println("Pago de " + monto + " registrado para la preventa " + this.id + " con método: " + metodo);
        return pago;
    }



    public void marcarComoFinalizado() {
        // La lógica ahora es más simple
        boolean hasSignedContract = this.contratoVenta != null && "Firmado".equals(this.contratoVenta.getEstado());
        if (hasSignedContract && this.estado != EstadoPreventa.CANCELADA) {
            this.estado = EstadoPreventa.FINALIZADA;
        } else {
            throw new IllegalStateException("La preventa no puede ser finalizada sin un contrato firmado.");
        }
    }



    public void addPropuestaPago(PropuestaPago propuesta) {
        this.propuestasPago.add(propuesta);
        propuesta.setPreventa(this);
    }

    public void removePropuestaPago(PropuestaPago propuesta) {
        this.propuestasPago.remove(propuesta);
        propuesta.setPreventa(null);
    }

    public void addVisitaProgramada(VisitaProgramada visita) {
        this.visitasProgramadas.add(visita);
        visita.setPreventa(this);
    }

    public void removeVisitaProgramada(VisitaProgramada visita) {
        this.visitasProgramadas.remove(visita);
        visita.setPreventa(null);
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