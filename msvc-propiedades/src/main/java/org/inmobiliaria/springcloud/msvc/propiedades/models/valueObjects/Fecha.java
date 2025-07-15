package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Objects;


@Embeddable
public class Fecha {

    private Integer dia;
    private Integer mes;
    private Integer anio;

    protected Fecha() {}

    public Fecha(Integer dia, Integer mes, Integer anio) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes inválido");
        }
        if (anio < 1900) {
            throw new IllegalArgumentException("Año debe ser mayor a 1900");
        }
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Día inválido");
        }

        try {
            LocalDate.of(anio, mes, dia); // Verifica la combinación fecha
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha inválida");
        }

        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public Integer getDia() {
        return dia;
    }

    public Integer getMes() {
        return mes;
    }

    public Integer getAnio() {
        return anio;
    }

    @Override
    public String toString() {
        return dia + "/" + mes + "/" + anio;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Fecha)) return false;
        Fecha fecha = (Fecha) obj;
        return dia.equals(fecha.dia) && mes.equals(fecha.mes) && anio.equals(fecha.anio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dia, mes, anio);
    }
}
