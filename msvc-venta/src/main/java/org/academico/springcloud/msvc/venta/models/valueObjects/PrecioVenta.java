package org.academico.springcloud.msvc.venta.models.valueObjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;


@Embeddable
public class PrecioVenta
{

    private BigDecimal precioVenta;
    private String moneda;

    public PrecioVenta(){}

    public PrecioVenta(BigDecimal valor, String moneda) {
    if(valor==null || valor.compareTo(BigDecimal.ZERO)<=0){
        throw new IllegalArgumentException("El valor debe ser mayor que cero");
    }
    if(moneda==null ||moneda.isBlank()){
        throw new IllegalArgumentException("El valor debe ser mayor que cero");
    }

    this.precioVenta = valor;
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
        return precioVenta +" "+moneda;
    }
}
