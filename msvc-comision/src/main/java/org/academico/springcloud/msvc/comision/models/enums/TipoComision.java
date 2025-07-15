package org.academico.springcloud.msvc.comision.models.enums;

import java.math.BigDecimal;

public enum TipoComision {
    FIJA(new BigDecimal("2000.00")) {
        @Override
        public BigDecimal calcular(BigDecimal montoBase) {
            return valor;
        }
    },
    PORCENTAJE(new BigDecimal("0.03")) {
        @Override
        public BigDecimal calcular(BigDecimal montoBase) {
            if (montoBase == null || montoBase.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto base debe ser mayor que cero");
            }
            return montoBase.multiply(valor);
        }
    };

    protected final BigDecimal valor;

    TipoComision(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public abstract BigDecimal calcular(BigDecimal montoBase);
}
