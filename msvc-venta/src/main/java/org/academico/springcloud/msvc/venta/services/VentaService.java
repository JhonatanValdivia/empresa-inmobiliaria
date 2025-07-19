package org.academico.springcloud.msvc.venta.services;

import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService
{

    List<Venta> listar();
    List<Venta> todosPorId(List<Long> ids);
    Optional<Venta> porId(Long id);
    Venta guardar(Venta venta);
    void eliminar(Long id);
    boolean existePorId(Long id);
    long contarVentas();
    void eliminarVenta(Venta venta);

    //Métodos para manejar la relacion entre Venta y DetalleVenta
    void agregarDetalle(Long ventaId, DetalleVenta detalleVenta);
    void eliminarDetalle(Long ventaId, Long detalleId);

    // Métodos para la relación con Preventa
    Optional<Venta> asignarPreventa(Long ventaId, Long preventaId);
    Optional<Venta> desasignarPreventa(Long ventaId);

}
