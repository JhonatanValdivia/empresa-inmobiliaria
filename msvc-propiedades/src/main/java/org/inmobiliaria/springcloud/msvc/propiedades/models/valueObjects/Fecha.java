package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Fecha {

    private int dia;
    private int mes;
    private int año;

    protected Fecha() {}

    public Fecha(Integer dia, Integer mes, Integer anio) {
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

    public Integer getDia() {
        return dia;
    }

    public Integer getMes() {
        return mes;
    }

    public Integer getAño() {
        return año;
    }

    public void setAnio(Integer anio) {
        this.año = anio;
    }
}
