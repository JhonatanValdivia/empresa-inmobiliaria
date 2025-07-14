package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Precio {

    @Column(name = "monto", nullable = false)
    private java.math.BigDecimal monto;

    @Column(name = "moneda", nullable = false)
    private String moneda;

    protected Precio() {}

    public Precio(java.math.BigDecimal monto, String moneda) {
        this.monto = monto;
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
