package org.academico.springcloud.msvc.preventa.models.valueObjects;

import java.math.BigDecimal;

public class Precio {
    private BigDecimal monto;
    private String moneda;

    public Precio() {}

    public Precio(BigDecimal monto, String moneda) {
        this.monto = monto;
        this.moneda = moneda;
    }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
}
