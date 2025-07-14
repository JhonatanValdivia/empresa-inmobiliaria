package org.academico.springcloud.msvc.venta.models.valueObjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable // indica que la clase se puede integrar en una entidad y no tiene su propia identidad independiente.
public class FechaVenta
{
    private int dia;
    private int mes;
    private int año;

    public FechaVenta() {
    }

    public FechaVenta(int dia, int mes, int año) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes inválido: debe estar entre 1 y 12");
        }
        if (año < 1900) {
            throw new IllegalArgumentException("Año inválido: debe ser mayor a 1900");
        }
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Día inválido: debe estar entre 1 y 31");
        }

        // detecta si el día es válido para ese mes/año
        try {
            LocalDate.of(año, mes, dia);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida: combinación día-mes-año incorrecta");
        }

        // no permitir fechas pasadas
        if (LocalDate.of(año, mes, dia).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha inválida: no puede ser en el pasado");
        }

        this.dia = dia;
        this.mes = mes;
        this.año = año;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAño() {
        return año;
    }

    @Override
    public String toString() {
        return dia + "/" + mes + "/" + año;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FechaVenta)) return false;
        FechaVenta fecha = (FechaVenta) obj;
        return dia == fecha.dia && mes == fecha.mes && año == fecha.año;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dia, mes, año);
    }
}
