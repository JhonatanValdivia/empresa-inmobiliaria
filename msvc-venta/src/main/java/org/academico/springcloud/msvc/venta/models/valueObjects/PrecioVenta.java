package org.academico.springcloud.msvc.venta.models.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;


@Embeddable
public class PrecioVenta
{

    private BigDecimal precioVenta;
    private  String moneda;

    protected PrecioVenta(){}

    @JsonCreator
    public PrecioVenta(@JsonProperty("precioVenta") BigDecimal precioVenta,
                       @JsonProperty("moneda") String moneda) {

        if (precioVenta == null || precioVenta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor que cero");
        }
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("La moneda no puede ser vacía");
        }

        this.precioVenta = precioVenta;
        this.moneda = moneda;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public String getMoneda() {
        return moneda;
    }

    @Override
    public boolean equals(Object object){
        if(this==object)
            return true;

        if(!(object instanceof PrecioVenta))
            return false;

        PrecioVenta precio=(PrecioVenta) object;
        return precioVenta.equals(precio.precioVenta) &&
                moneda.equals(precio.moneda);
    }
    @Override
    public int hashCode(){
        return Objects.hash(precioVenta,moneda);
    }

    @Override
    public String toString(){
        return precioVenta +" "+moneda;}
}
