package org.academico.springcloud.msvc.comision.models;

import org.academico.springcloud.msvc.comision.models.valueObjects.MontoComision;

import java.math.BigDecimal;
import java.util.List;

public class Venta {
    private Long id;
    private String tipoVenta;
    private String estado;
    private Fecha fecha;
    private MontoComision precioVenta;
    private Long preventaId;
    private List<DetalleVenta> detalleVentaLista;
    private DetallePreventa detallePreventa;

    // Getters y Setters
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public MontoComision getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(MontoComision precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Long getPreventaId() {
        return preventaId;
    }

    public void setPreventaId(Long preventaId) {
        this.preventaId = preventaId;
    }

    public List<DetalleVenta> getDetalleVentaLista() {
        return detalleVentaLista;
    }

    public void setDetalleVentaLista(List<DetalleVenta> detalleVentaLista) {
        this.detalleVentaLista = detalleVentaLista;
    }

    public DetallePreventa getDetallePreventa() {
        return detallePreventa;
    }

    public void setDetallePreventa(DetallePreventa detallePreventa) {
        this.detallePreventa = detallePreventa;
    }


    // Clases auxiliares (ajusta según tu estructura)
    public class Fecha {
        private Integer dia;
        private Integer mes;
        private Integer año;

        public Integer getDia() {return dia;}
        public void setDia(Integer dia) {this.dia = dia;}
        public Integer getMes() {return mes;}
        public void setMes(Integer mes) {this.mes = mes;}
        public Integer getAño() {return año;}
        public void setAño(Integer año) {this.año = año;}
    }

    public class DetalleVenta {
        private Long id;
        private String descripcion;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

    public class DetallePreventa {
        private Long id;
        private String estado;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}