package org.academico.springcloud.msvc.comision.models.entities;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.comision.models.Venta;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.academico.springcloud.msvc.comision.models.enums.TipoComision;
import org.academico.springcloud.msvc.comision.models.valueObjects.FechaPagoComision;
import org.academico.springcloud.msvc.comision.models.valueObjects.MontoComision;

import java.math.BigDecimal;

@Entity
@Table(name = "comisiones")
public class Comision
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoComision estadoComision;

    @Enumerated(EnumType.STRING)
    private TipoComision tipoComision;

    @Embedded
    @AttributeOverride(name = "montoComision", column = @Column(name = "monto_comision"))
    private MontoComision montoComision;//OV

    @Embedded
    private FechaPagoComision fechaPagoComision; //OV

    @Column(name = "venta_id", unique = true)//
    private Long ventaId;

    @Transient
    private Venta venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoComision getEstadoComision() {
        return estadoComision;
    }

    public void setEstadoComision(EstadoComision estadoComision) {
        this.estadoComision = estadoComision;
    }

    public TipoComision getTipoComision() {
        return tipoComision;
    }

    public void setTipoComision(TipoComision tipoComision) {
        this.tipoComision = tipoComision;
    }

    public MontoComision getMontoComision() {
        return montoComision;
    }

    public void setMontoComision(MontoComision montoComision) {
        this.montoComision = montoComision;
    }

    public FechaPagoComision getFechaPagoComision() {
        return fechaPagoComision;
    }

    public void setFechaPagoComision(FechaPagoComision fechaPagoComision) {
        this.fechaPagoComision = fechaPagoComision;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }
    public boolean esValida() {
        return ventaId != null && montoComision != null && tipoComision != null;
    }


    public void pagarComision() {
        this.estadoComision = EstadoComision.PAGADA;
    }

    public void actualizarEstado(EstadoComision nuevoEstado) {
        this.estadoComision = nuevoEstado;
    }

    public boolean verificarPago() {
        return EstadoComision.PAGADA.equals(this.estadoComision);
    }
}
