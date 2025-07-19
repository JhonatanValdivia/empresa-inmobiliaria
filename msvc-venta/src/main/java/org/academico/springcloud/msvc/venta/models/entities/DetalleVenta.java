package org.academico.springcloud.msvc.venta.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.academico.springcloud.msvc.venta.models.enums.EstadoDetalle;
import org.academico.springcloud.msvc.venta.models.enums.MetodoPago;
import org.academico.springcloud.msvc.venta.models.valueObjects.CronogramaPago;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CronogramaPago cronogramaPago; //OV

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    private EstadoDetalle estadoDetalle;

    @ManyToOne(fetch = FetchType.LAZY)//N:1
    @JoinColumn(name ="venta_id")//FK que apunta al id de venta
    @JsonIgnore
    private  Venta venta; //representa la referencia a la Venta a la que pertenece ese DetalleVenta

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public CronogramaPago getCronogramaPago() {
        return cronogramaPago;
    }
    public void setCronogramaPago(CronogramaPago cronogramaPago) {
        this.cronogramaPago = cronogramaPago;
    }
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
    public EstadoDetalle getEstadoDetalle() {
        return estadoDetalle;
    }
    public void setEstadoDetalle(EstadoDetalle estadoDetalle) {
        this.estadoDetalle = estadoDetalle;
    }
    public Venta getVenta() {return venta;}
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
