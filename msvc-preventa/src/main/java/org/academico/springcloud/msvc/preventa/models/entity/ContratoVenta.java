package org.academico.springcloud.msvc.preventa.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importar esta
import jakarta.persistence.*;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "contratos_venta")
public class ContratoVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    private LocalDate fechaFirma;

    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preventa_id",unique = true)
    @JsonBackReference // AÑADIDO: Este lado no serializará el objeto Preventa para evitar el bucle
    private Preventa preventa;

    //Getters y Setters
    public ContratoVenta() {
        this.estado = "Pendiente";
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }
    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
    public LocalDate getFechaFirma() {
        return fechaFirma;
    }
    public void setFechaFirma(LocalDate fechaFirma) {
        this.fechaFirma = fechaFirma;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Preventa getPreventa() {
        return preventa;
    }
    public void setPreventa(Preventa preventa) {
        this.preventa = preventa;
    }


    public void anularContrato() {
        if ("Firmado".equals(this.estado)) {
            this.estado = "Anulado";
            System.out.println("Contrato anulado.");
        } else {
            throw new IllegalStateException("Solo se pueden anular contratos firmados.");
        }
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("Estado del contrato cambiado a: " + nuevoEstado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContratoVenta that = (ContratoVenta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}