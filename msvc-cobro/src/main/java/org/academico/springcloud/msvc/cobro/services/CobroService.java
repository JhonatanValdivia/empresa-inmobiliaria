package org.academico.springcloud.msvc.cobro.services;

import org.academico.springcloud.msvc.cobro.models.entities.Cobro;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CobroService {
    List<Cobro> listarCobros();
    Optional<Cobro> porId(Long id);
    Cobro crearCobro(Cobro cobro);
    Cobro actualizarCobro(Long id, Cobro cobro);
    void eliminarCobro(Long id);
    Cobro registrarPago(Long id, BigDecimal monto);
    String generarComprobante(Long id);
}