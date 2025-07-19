package org.academico.springcloud.msvc.venta.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.academico.springcloud.msvc.venta.models.enums.FrecuenciaPago;

import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CronogramaPago
{
    private LocalDate fechaInicio;
    private int numeroCuotas;

    @Enumerated(EnumType.STRING)
    private FrecuenciaPago frecuencia;

    protected CronogramaPago(){}
    @JsonCreator
    public CronogramaPago(@JsonProperty("fechaInicio") LocalDate fechaInicio,
                          @JsonProperty("numeroCuotas") int numeroCuotas,
                          @JsonProperty("frecuencia") FrecuenciaPago frecuencia) {

        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser pasada");
        }
        if (numeroCuotas <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser mayor que cero");
        }
        if (frecuencia == null) {
            throw new IllegalArgumentException("La frecuencia no puede ser nula o vacía");
        }

        this.fechaInicio = fechaInicio;
        this.numeroCuotas = numeroCuotas;
        this.frecuencia = frecuencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public FrecuenciaPago getFrecuencia() {
        return frecuencia;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof PrecioVenta))
            return false;

        CronogramaPago cronogramaPago = (CronogramaPago) object;
        return fechaInicio.equals(cronogramaPago.fechaInicio) &&
                numeroCuotas==cronogramaPago.numeroCuotas &&
                frecuencia.equals(cronogramaPago.frecuencia);
    }
    @Override
    public int hashCode(){
        return Objects.hash(fechaInicio,numeroCuotas,frecuencia);
    }

    @Override
    public String toString(){
        return "Inicio de pago: "+fechaInicio+ ",cuotas"+numeroCuotas+",frecuencia:"+frecuencia;
    }
}
