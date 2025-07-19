package org.academico.springcloud.msvc.preventa.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importar esta
import jakarta.persistence.*;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "propuestas_pago")
public class PropuestaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal monto;
    private LocalDate fecha;
    private Integer cuotas;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preventa_id")
    @JsonBackReference // AÑADIDO: Este lado no serializará el objeto Preventa para evitar el bucle
    private Preventa preventa;

    public PropuestaPago() {
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public BigDecimal getMonto() {return monto;}
    public void setMonto(BigDecimal monto) {this.monto = monto;}
    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}
    public Integer getCuotas() {return cuotas;}
    public void setCuotas(Integer cuotas) {this.cuotas = cuotas;}
    public MetodoPago getMetodoPago() {return metodoPago;}
    public void setMetodoPago(MetodoPago metodoPago) {this.metodoPago = metodoPago;}
    public Preventa getPreventa() {return preventa;}
    public void setPreventa(Preventa preventa) {this.preventa = preventa;}



    public void aceptarPropuesta() {
        System.out.println("Propuesta de pago aceptada.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropuestaPago that = (PropuestaPago) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}