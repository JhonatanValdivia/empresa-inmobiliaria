package org.academico.springcloud.msvc.comision.models.valueObjects;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MontoComision {
    private BigDecimal montoComision; //representa decimales exactos y permite operaciones +,-,*,/ sin perder precision
    private String moneda;

    public MontoComision(){}

    public MontoComision(BigDecimal montoComision, String moneda) {
        if(montoComision==null || montoComision.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("El valor debe ser mayor que cero");
        }
        if(moneda==null ||moneda.isBlank()){
            throw new IllegalArgumentException("El valor debe ser mayor que cero");
        }

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
        return montoComision +" "+moneda;
    }
}
