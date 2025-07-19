package org.academico.springcloud.msvc.campania.models.entities;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.campania.models.Propiedad;
import org.academico.springcloud.msvc.campania.models.enums.EstadoCampania;
import org.academico.springcloud.msvc.campania.models.enums.MedioPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Campañas")
public class Campania {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampania;

    private Long idPropiedad;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idCampania")
    private List<ProveedorPublicidad> proveedores = new ArrayList<>();
    private String nombre;

    @Enumerated(EnumType.STRING)
    private EstadoCampania estado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    @Transient // No se mapea a la base de datos, solo en memoria
    private Propiedad propiedad;

    public Campania() {}

    // Métodos
    public void crearCampania() {
        if (this.proveedores == null || this.proveedores.isEmpty()) {
            throw new IllegalStateException("No se puede crear la campaña sin al menos un proveedor.");
        }
        this.estado = EstadoCampania.ACTIVA;
    }

    public void aprobarMonto(BigDecimal nuevoMonto) {
        if (nuevoMonto != null && nuevoMonto.compareTo(BigDecimal.ZERO) > 0) {
            this.monto = nuevoMonto;
        } else {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }

    // Getters y setters
    public Long getIdCampania() { return idCampania; }
    public void setIdCampania(Long idCampania) { this.idCampania = idCampania; }
    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }
    public List<ProveedorPublicidad> getProveedores() { return proveedores; }
    public void setProveedores(List<ProveedorPublicidad> proveedores) { this.proveedores = proveedores; }
    public void agregarProveedor(ProveedorPublicidad proveedor) { this.proveedores.add(proveedor); }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public EstadoCampania getEstado() { return estado; }
    public void setEstado(EstadoCampania estado) { this.estado = estado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
}