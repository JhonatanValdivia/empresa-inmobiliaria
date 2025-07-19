package org.academico.springcloud.msvc.venta.models.entities;

import jakarta.persistence.*;
import org.academico.springcloud.msvc.venta.models.Preventa;
import org.academico.springcloud.msvc.venta.models.enums.EstadoVenta;
import org.academico.springcloud.msvc.venta.models.enums.TipoVenta;
import org.academico.springcloud.msvc.venta.models.valueObjects.FechaVenta;
import org.academico.springcloud.msvc.venta.models.valueObjects.PrecioVenta;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @Embedded
    private FechaVenta fecha; //OV

    @Embedded
    @AttributeOverride(name = "precioVenta", column = @Column(name = "precio_valor"))
    private PrecioVenta precioVenta; //OV

    //AÑADIDO: Este campo es para almacenar el ID de la Preventa asociada a esta Venta
    @Column(unique = true)
    private Long preventaId;
    @Transient
    private Preventa DetallePreventa; //Este campo es para los datos completos de Preventa obtenidos vía API

    //AÑADIDO: Relación con DetalleVentaa
    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL, orphanRemoval = true)//relacion 1:M---> bidireccional
    private List<DetalleVenta> detalleVentaLista;

    public Venta() {
        detalleVentaLista= new ArrayList<>();
    }

    //[Id_DetalleVenta] relacion entidad (en el mismo agregado)
    //[Id_Preventa] relacion agregado raiz
    //[Id_Cobro]  relacion agregado raiz
    //[Id_Comision]  relacion agregado raiz

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TipoVenta getTipoVenta() {
        return tipoVenta;
    }
    public void setTipoVenta(TipoVenta tipoVenta) {
        this.tipoVenta = tipoVenta;
    }
    public EstadoVenta getEstado() {
        return estado;
    }
    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }
    public FechaVenta getFecha() {
        return fecha;
    }
    public void setFecha(FechaVenta fecha) {
        this.fecha = fecha;
    }
    public PrecioVenta getPrecioVenta() {
        return precioVenta;
    }
    public void setPrecioVenta(PrecioVenta precioVenta) {
        this.precioVenta = precioVenta;
    }
    public List<DetalleVenta> getDetalleVentaLista() {
        return detalleVentaLista;
    }
    public void setDetalleVentaLista(List<DetalleVenta> detalleVentaLista) {this.detalleVentaLista = detalleVentaLista;}
    public Preventa getDetallePreventa() {return DetallePreventa;}
    public void setDetallePreventa(Preventa detallePreventa) {DetallePreventa = detallePreventa;}
    public Long getPreventaId() {return preventaId;}
    public void setPreventaId(Long preventaId) {this.preventaId = preventaId;}


    public void agregarDetalleVenta(DetalleVenta detalleVenta){
        detalleVenta.setVenta(this);
        this.detalleVentaLista.add(detalleVenta);
    }
    public void eliminarDetalleVenta(DetalleVenta detalleVenta){
        detalleVentaLista.remove(detalleVenta);
        detalleVenta.setVenta(null);
    }

}
