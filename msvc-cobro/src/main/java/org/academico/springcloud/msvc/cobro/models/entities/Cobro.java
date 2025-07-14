package org.academico.springcloud.msvc.cobro.models.entities;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.cobro.models.enums.EstadoCobro;
import org.academico.springcloud.msvc.cobro.models.enums.MedioPago;
import org.academico.springcloud.msvc.cobro.models.valueObjects.FechaCobro;
import org.academico.springcloud.msvc.cobro.models.valueObjects.MontoCobro;
import java.math.BigDecimal;

@Entity
@Table(name = "Cobros")
public class Cobro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCobro;

    @Column(name = "venta_id", nullable = false)
    private Long idVenta;

    @Embedded
    private FechaCobro fechaCobro;

    @Embedded
    private MontoCobro montoCobro;

    @Enumerated(EnumType.STRING)
    private EstadoCobro estadoCobro = EstadoCobro.PENDIENTE;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    // Constructores
    public Cobro() {}

    public Cobro(Long idVenta, FechaCobro fechaCobro, MontoCobro montoCobro, EstadoCobro estadoCobro, MedioPago medioPago) {
        this.idVenta = idVenta;
        this.fechaCobro = fechaCobro;
        this.montoCobro = montoCobro;
        this.estadoCobro = estadoCobro;
        this.medioPago = medioPago;
    }

    // Métodos del agregado
    public void registrarPago(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que cero.");
        }
        if (!EstadoCobro.PENDIENTE.equals(this.estadoCobro)) {
            throw new IllegalStateException("El cobro ya ha sido procesado.");
        }
        this.montoCobro.setMonto(monto); // Actualiza el monto con el pago
        this.estadoCobro = EstadoCobro.PAGADO;
    }

    public String generarComprobante() {
        return String.format("Comprobante #%d\nFecha: %d/%d/%d %02d:%02d\nMonto: %s %s\nEstado: %s\nMedio: %s",
                idCobro, fechaCobro.getDia(), fechaCobro.getMes(), fechaCobro.getAnio(),
                fechaCobro.getHora(), fechaCobro.getMinuto(),
                montoCobro.getMonto(), montoCobro.getMoneda(),
                estadoCobro, medioPago);
    }

    // Getters y setters
    public Long getIdCobro() { return idCobro; }
    public void setIdCobro(Long idCobro) { this.idCobro = idCobro; }
    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }
    public FechaCobro getFechaCobro() { return fechaCobro; }
    public void setFechaCobro(FechaCobro fechaCobro) { this.fechaCobro = fechaCobro; }
    public MontoCobro getMontoCobro() { return montoCobro; }
    public void setMontoCobro(MontoCobro montoCobro) { this.montoCobro = montoCobro; }
    public EstadoCobro getEstadoCobro() { return estadoCobro; }
    public void setEstadoCobro(EstadoCobro estadoCobro) { this.estadoCobro = estadoCobro; }
    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }
}