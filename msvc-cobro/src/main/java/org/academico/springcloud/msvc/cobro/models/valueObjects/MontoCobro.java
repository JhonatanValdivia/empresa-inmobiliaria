package org.academico.springcloud.msvc.cobro.models.valueObjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MontoCobro {
    private BigDecimal monto;
    private String moneda;

    public MontoCobro() {}

    public MontoCobro(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0 || monto.compareTo(new BigDecimal("1000000")) > 0) {
            throw new IllegalArgumentException("El monto debe estar entre 0.01 y 1,000,000.");
        }
        if (moneda == null || moneda.trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda es obligatoria.");
        }
        // Validación básica de códigos ISO 4217 (puedes expandir esta lista)
        String[] validCurrencies = {"PEN", "USD", "EUR"};
        boolean isValidCurrency = false;
        for (String curr : validCurrencies) {
            if (curr.equals(moneda.trim().toUpperCase())) {
                isValidCurrency = true;
                break;
            }
        }
        if (!isValidCurrency) {
            throw new IllegalArgumentException("Moneda no válida. Use PEN, USD o EUR.");
        }
        this.monto = monto;
        this.moneda = moneda.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("%s %s", monto, moneda);
    }

    // Getters y setters
    public BigDecimal getMonto() { return monto; }
    public String getMoneda() { return moneda; }
    public void setMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0 || monto.compareTo(new BigDecimal("1000000")) > 0) {
            throw new IllegalArgumentException("El monto debe estar entre 0.01 y 1,000,000.");
        }
        this.monto = monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MontoCobro)) return false;
        MontoCobro that = (MontoCobro) o;
        return Objects.equals(monto, that.monto) &&
                Objects.equals(moneda, that.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto, moneda);
    }
}