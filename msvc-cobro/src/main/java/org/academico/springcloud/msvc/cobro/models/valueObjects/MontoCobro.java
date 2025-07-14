package org.academico.springcloud.msvc.cobro.models.valueObjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MontoCobro {
    private BigDecimal monto;
    private String moneda;

    public MontoCobro() {}

    public MontoCobro(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        if (moneda == null || moneda.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda es obligatoria.");
        }
        this.monto = monto;
        this.moneda = moneda.trim();
    }

    @Override
    public String toString() {
        return String.format("%s %s", monto, moneda);
    }

    public BigDecimal getMonto() { return monto; }
    public String getMoneda() { return moneda; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
}