package org.academico.springcloud.msvc.preventa.models.valueObjects;

public class Fecha {
    private Integer dia;
    private Integer mes;
    private Integer anio;

    public Fecha() {}

    public Fecha(Integer dia, Integer mes, Integer anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public Integer getDia() { return dia; }
    public void setDia(Integer dia) { this.dia = dia; }
    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
}