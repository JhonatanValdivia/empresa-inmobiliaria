package org.academico.springcloud.msvc.cobro.models.valueObjects;

import jakarta.persistence.Embeddable;

@Embeddable
public class FechaCobro {
    private int dia;
    private int mes;
    private int anio;
    private int hora;
    private int minuto;

    // Constructor sin argumentos (requerido por Hibernate)
    public FechaCobro() {}

    public FechaCobro(int dia, int mes, int anio, int hora, int minuto) {
        if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || anio < 2000 || anio > 2100 ||
                hora < 0 || hora > 23 || minuto < 0 || minuto > 59) {
            throw new IllegalArgumentException("Fecha o hora invalida.");
        }
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.hora = hora;
        this.minuto = minuto;
    }

    public FechaCobro(java.time.LocalDateTime fecha) {
        this.dia = fecha.getDayOfMonth();
        this.mes = fecha.getMonthValue();
        this.anio = fecha.getYear();
        this.hora = fecha.getHour();
        this.minuto = fecha.getMinute();
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%d %02d:%02d", dia, mes, anio, hora, minuto);
    }

    public int getDia() { return dia; }
    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }
    public void setDia(int dia) { this.dia = dia; }
    public void setMes(int mes) { this.mes = mes; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setHora(int hora) { this.hora = hora; }
    public void setMinuto(int minuto) { this.minuto = minuto; }
}