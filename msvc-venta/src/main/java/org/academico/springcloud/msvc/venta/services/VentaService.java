package org.academico.springcloud.msvc.venta.services;

import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService
{

    List<Venta> listar();// findAll
    List<Venta> todosPorId(List<Long> ids);//findAllById
    Optional<Venta> porId(Long id); //findById
    Venta guardar(Venta venta);//save
    void eliminar(Long id);//deleteById
    boolean existePorId(Long id);//existsById
    long contarVentas();
    void eliminarVenta(Venta venta);

    //Metodos para manejar la relacion entre Venta y DetalleVents
    void agregarDetalle(Long ventaId, DetalleVenta detalleVenta);
    void eliminarDetalle(Long ventaId, Long detalleId);

    // Métodos para la relación con Preventa
    Optional<Venta> asignarPreventa(Long ventaId, Long preventaId);
    Optional<Venta> desasignarPreventa(Long ventaId);

}
