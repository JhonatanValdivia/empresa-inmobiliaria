package org.academico.springcloud.msvc.preventa.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importar esta
import jakarta.persistence.*;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoVisita;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "visitas_programadas")
public class VisitaProgramada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private EstadoVisita estadoVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preventa_id")
    @JsonBackReference // AÑADIDO: Este lado no serializará el objeto Preventa para evitar el bucle
    private Preventa preventa;

    public VisitaProgramada() {
        this.estadoVisita = EstadoVisita.PROGRAMADA;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public EstadoVisita getEstadoVisita() {
        return estadoVisita;
    }
    public void setEstadoVisita(EstadoVisita estadoVisita) {
        this.estadoVisita = estadoVisita;
    }
    public Preventa getPreventa() {
        return preventa;
    }
    public void setPreventa(Preventa preventa) {
        this.preventa = preventa;
    }


    public void reprogramarVisita(LocalDate nuevaFecha) {
        if (this.estadoVisita == EstadoVisita.PROGRAMADA) {
            this.fecha = nuevaFecha;
            System.out.println("Visita reprogramada para: " + nuevaFecha);
        } else {
            throw new IllegalStateException("Solo se pueden reprogramar visitas programadas.");
        }
    }

    public void actualizarEstado(EstadoVisita nuevoEstado) {
        this.estadoVisita = nuevoEstado;
        System.out.println("Estado de visita actualizado a: " + nuevoEstado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitaProgramada that = (VisitaProgramada) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}