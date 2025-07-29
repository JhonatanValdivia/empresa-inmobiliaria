package org.academico.springcloud.msvc.comision.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MontoComision {
    @JsonProperty("precioVenta") // Mapear "precioVenta" del JSON a este campo
    private BigDecimal montoComision;

    @JsonProperty("moneda")     // Mapear "moneda" del JSON a este campo
    private String moneda;
    protected  MontoComision(){}

    @JsonCreator
    public MontoComision(@JsonProperty("precioVenta") BigDecimal montoComision,
                         @JsonProperty("moneda") String moneda) {
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede ser vacía");
        }
        // No validar montoComision aquí para evitar errores de deserialización
        this.montoComision = montoComision;
        this.moneda = moneda;
    }

    public BigDecimal getMontoComision () {
        return montoComision;
    }
    public String getMoneda() {
        return moneda;
    }

    @Override
    public boolean equals(Object object){
        if(this==object)
            return true;

        if(!(object instanceof MontoComision))
            return false;

        MontoComision monto=(MontoComision) object;
        return montoComision.equals(monto.montoComision) &&
                moneda.equals(monto.moneda);
    }
    @Override
    public int hashCode(){
        return Objects.hash(montoComision,moneda);
    }

    @Override
    public String toString(){
        return montoComision + " " +moneda;
    }
}
