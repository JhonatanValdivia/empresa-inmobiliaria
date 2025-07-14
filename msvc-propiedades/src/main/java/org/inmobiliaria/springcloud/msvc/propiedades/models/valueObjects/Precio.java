package org.inmobiliaria.springcloud.msvc.propiedades.models.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Precio {

    private BigDecimal monto;
    private String moneda;

    protected Precio() {}

    public Precio(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a cero");
        }
        if (moneda == null || moneda.isEmpty()) {
            throw new IllegalArgumentException("Moneda no puede ser vacía");
        }

        this.monto = monto;
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMoneda() {
        return moneda;
    }

    @Override
    public String toString() {
        return "Precio{" +
                "monto=" + monto +
                ", moneda='" + moneda + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Precio)) return false;
        Precio other = (Precio) obj;
        return monto.equals(other.monto) && moneda.equals(other.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto, moneda);
    }
}
