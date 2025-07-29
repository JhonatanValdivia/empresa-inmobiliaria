package org.academico.springcloud.msvc.comision.services;

import org.academico.springcloud.msvc.comision.models.Venta;
import org.academico.springcloud.msvc.comision.models.entities.Comision;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.academico.springcloud.msvc.comision.models.enums.TipoComision;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ComisionService {
    List<Comision> listar();
    List<Comision> todosPorId(List<Long> ids);
    Optional<Comision> porId(Long id);
    Comision guardar(Comision comision);
    void eliminar(Long id);
    boolean existePorId(Long id);
    long contarComisiones();
    void eliminarComision(Comision comision);
    BigDecimal calcularComision(BigDecimal montoBase, TipoComision tipoComision);
    void cambiarEstadoComision(Long comisionId, EstadoComision nuevoEstado);
    List<Comision> listarActivas();
    Optional<Comision> porIdConUsuario(Long id);
}