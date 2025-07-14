package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Fecha {

    @Column(name = "dia")
    private Integer dia;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "anio")
    private Integer anio;

    protected Fecha() {}

    public Fecha(Integer dia, Integer mes, Integer anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}
