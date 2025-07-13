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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de los contratos
    private List<ContratoVenta> contratosVenta;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las propuestas
    private List<PropuestaPago> propuestasPago;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "preventa")
    @JsonManagedReference // AÑADIDO: Este lado gestiona la serialización de las visitas
    private List<VisitaProgramada> visitasProgramadas;

    public Preventa() {
            this.estado = EstadoPreventa.EN_EVALUACION; // Estado inicial
        this.fechaInicio = LocalDate.now();
        this.contratosVenta = new ArrayList<>();
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

    public List<ContratoVenta> getContratosVenta() {
        return contratosVenta;
    }

    public void setContratosVenta(List<ContratoVenta> contratosVenta) {
        this.contratosVenta = contratosVenta;
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

    public ContratoVenta registrarContrato(TipoContrato tipoContrato, LocalDate fechaFirma) {
        ContratoVenta contrato = new ContratoVenta();
        contrato.setTipoContrato(tipoContrato);
        contrato.setFechaFirma(fechaFirma);
        contrato.setEstado("Firmado"); // Estado inicial al registrar el contrato
        contrato.setPreventa(this); // Asegura la relación bidireccional
        this.contratosVenta.add(contrato);
        System.out.println("Contrato de tipo " + tipoContrato + " firmado para la preventa " + this.id);
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

    public BigDecimal calcularComision() {
        BigDecimal totalContratos = this.contratosVenta.stream()
                .filter(c -> "Firmado".equals(c.getEstado()))
                .map(c -> BigDecimal.valueOf(100000)) // Ejemplo de monto fijo
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal comision = totalContratos.multiply(BigDecimal.valueOf(0.01));
        System.out.println("Comisión calculada para la preventa " + this.id + ": " + comision);
        return comision;
    }

    public void marcarComoFinalizado() {
        boolean hasSignedContract = this.contratosVenta.stream()
                .anyMatch(c -> "Firmado".equals(c.getEstado()));
        if (hasSignedContract && this.estado != EstadoPreventa.CANCELADA) {
            this.estado = EstadoPreventa.FINALIZADA;
            System.out.println("Preventa " + this.id + " marcada como finalizada.");
        } else {
            throw new IllegalStateException("La preventa " + this.id + " no puede ser finalizada en su estado actual o sin un contrato firmado.");
        }
    }

    public void addContratoVenta(ContratoVenta contrato) {
        this.contratosVenta.add(contrato);
        contrato.setPreventa(this);
    }

    public void removeContratoVenta(ContratoVenta contrato) {
        this.contratosVenta.remove(contrato);
        contrato.setPreventa(null);
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

    public Optional<ContratoVenta> findContratoById(Long contratoId) {
        return this.contratosVenta.stream()
                .filter(c -> Objects.equals(c.getId(), contratoId))
                .findFirst();
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