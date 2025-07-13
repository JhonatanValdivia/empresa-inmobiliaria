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
        this.dia = dia;
        this.mes = mes;
        this.año = año;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
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
        return dia + mes * 31 + año * 31 * 31;
    }
}
