package org.academico.springcloud.msvc.campania.models.valueObjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Embeddable
public class FechaCampania {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public FechaCampania() {
        this.fechaInicio = LocalDate.now();
        this.fechaFin = LocalDate.now().plusDays(30);
    }

    public FechaCampania(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate today = LocalDate.now();
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (fechaInicio.isBefore(today)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        long daysBetween = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        if (daysBetween > 365) {
            throw new IllegalArgumentException("La duración de la campaña no puede exceder 365 días.");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FechaCampania)) return false;
        FechaCampania that = (FechaCampania) o;
        return Objects.equals(fechaInicio, that.fechaInicio) &&
                Objects.equals(fechaFin, that.fechaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaInicio, fechaFin);
    }
}