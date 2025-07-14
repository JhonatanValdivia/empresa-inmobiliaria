package org.academico.springcloud.msvc.venta.models.entities;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)// para que la BD  guarde como texto y no como número
    private TipoVenta tipoVenta;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @Embedded //Marca un campo en una clase de entidad como un objeto incrustado
    private FechaVenta fecha; //OV

    @Embedded
    @AttributeOverride(name = "precioVenta", column = @Column(name = "precio_valor"))
    private PrecioVenta precioVenta; //OV

    //mappedBy: hace referencia a que la relación está mapeada en el lado de DetalleVenta por el atributo llamado venta
    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL, orphanRemoval = true)//relacion 1:M---> bidireccional
    private List<DetalleVenta> detalleVentaLista; //representa la lista de todos los DetalleVenta asociados a una Venta específica.

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

    public void setDetalleVentaLista(List<DetalleVenta> detalleVentaLista) {
        this.detalleVentaLista = detalleVentaLista;
    }


    public void agregarDetalleVenta(DetalleVenta detalleVenta){
        detalleVenta.setVenta(this);
        this.detalleVentaLista.add(detalleVenta);
    }
    public void eliminarDetalleVenta(DetalleVenta detalleVenta){
        detalleVentaLista.remove(detalleVenta);
        detalleVenta.setVenta(null);
    }
    public void registrarVenta() {
        this.estado = EstadoVenta.RESERVADA;
    }

    public void modificarEstado(EstadoVenta nuevoEstado) {
        this.estado = nuevoEstado;
    }

}
