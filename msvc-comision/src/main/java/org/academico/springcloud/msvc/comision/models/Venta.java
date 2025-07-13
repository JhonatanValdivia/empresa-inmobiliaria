package org.academico.springcloud.msvc.comision.models;

import java.math.BigDecimal;
import java.time.LocalDate;

//pojo
public class Venta {
    private Long id;
    private String tipoVenta;
    private String estadoVenta;
    private FechaVenta fechaVenta; //OV
    private PrecioVenta precioVenta; //OV

    public static class FechaVenta {
        private LocalDate fechaVenta;

        public LocalDate getFechaVenta() {
            return fechaVenta;
        }

        public void setFechaVenta(LocalDate fechaVenta) {
            this.fechaVenta = fechaVenta;
        }
    }
    public static class PrecioVenta{
        private BigDecimal precioVenta;
        private String moneda;

        public BigDecimal getPrecioVenta() {
            return precioVenta;
        }

        public void setPrecioVenta(BigDecimal precioVenta) {
            this.precioVenta = precioVenta;
        }

        public String getMoneda() {
            return moneda;
        }

        public void setMoneda(String moneda) {
            this.moneda = moneda;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public FechaVenta getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(FechaVenta fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public PrecioVenta getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(PrecioVenta precioVenta) {
        this.precioVenta = precioVenta;
    }
}
