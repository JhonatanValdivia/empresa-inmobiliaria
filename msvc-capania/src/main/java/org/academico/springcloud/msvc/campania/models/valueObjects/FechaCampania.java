package org.academico.springcloud.msvc.campania.models.valueObjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class FechaCampania {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public FechaCampania() {
        this.fechaInicio = LocalDate.now();
        this.fechaFin = LocalDate.now().plusDays(30);
    }

    public FechaCampania(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
}