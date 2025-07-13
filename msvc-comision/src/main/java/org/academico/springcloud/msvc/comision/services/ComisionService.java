package org.academico.springcloud.msvc.comision.services;

import org.academico.springcloud.msvc.comision.models.Venta;
import org.academico.springcloud.msvc.comision.models.entities.Comision;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.academico.springcloud.msvc.comision.models.enums.TipoComision;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ComisionService
{
    List<Comision> listar();// findAll
    List<Comision> todosPorId(List<Long> ids);//findAllById
    Optional<Comision> porId(Long id); //findById
    Comision guardar(Comision comision);//save
    void eliminar(Long id);//deleteById
    boolean existePorId(Long id);//existsById
    long contarComisiones();
    void eliminarComision(Comision comision);

    //métodos remotos
    Optional<Venta> obtenerVenta(Long ventaId);
    // Crear comisión automática para una venta específica
   Optional <Comision> crearComisionParaVenta(Long ventaId, TipoComision tipoComision);

    // Recalcular comisión si la venta cambió
    void recalcularComision(Long comisionId);

    // Anular comisión si la venta fue anulada
    void anularComisionesPorVenta(Long ventaId);
    List<Comision> listarPorVenta(Long ventaId);

    void cambiarEstadoComision(Long comisionId, EstadoComision nuevoEstado);

    BigDecimal totalComisionesPorVenta(Long ventaId);

    List<Comision> listarActivas();
}
