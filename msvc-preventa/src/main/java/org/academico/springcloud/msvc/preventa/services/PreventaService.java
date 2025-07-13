package org.academico.springcloud.msvc.preventa.services;

import org.academico.springcloud.msvc.preventa.models.entity.ContratoVenta;
import org.academico.springcloud.msvc.preventa.models.entity.Preventa;
import org.academico.springcloud.msvc.preventa.models.entity.PropuestaPago;
import org.academico.springcloud.msvc.preventa.models.entity.VisitaProgramada;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoVisita;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PreventaService {
    // CRUD para Preventa (Agregado Raíz)
    List<Preventa> listar();
    Optional<Preventa> porId(Long id);
    Preventa guardar(Preventa preventa);
    void eliminar(Long id);

    // Métodos de negocio para el agregado Preventa
    Optional<Preventa> programarVisitaEnPreventa(Long preventaId, LocalDate fechaVisita);
    Optional<Preventa> registrarContratoEnPreventa(Long preventaId, TipoContrato tipoContrato, LocalDate fechaFirma);
    Optional<Preventa> registrarPagoEnPreventa(Long preventaId, BigDecimal monto, LocalDate fecha, MetodoPago metodo);
    Optional<BigDecimal> calcularComisionPreventa(Long preventaId);
    Optional<Preventa> marcarPreventaComoFinalizada(Long preventaId);

    // CRUD Anidado para ContratoVenta (a través de Preventa)
    Optional<List<ContratoVenta>> listarContratosPorPreventa(Long preventaId);
    Optional<ContratoVenta> porIdContratoVenta(Long preventaId, Long contratoId);
    Optional<Preventa> agregarContratoVenta(Long preventaId, ContratoVenta contrato);
    Optional<ContratoVenta> actualizarContratoVenta(Long preventaId, Long contratoId, ContratoVenta contratoActualizado);
    Optional<Preventa> eliminarContratoVenta(Long preventaId, Long contratoId);
    Optional<ContratoVenta> anularContratoPreventa(Long preventaId, Long contratoId); // Método de negocio

    // CRUD Anidado para PropuestaPago (a través de Preventa)
    Optional<List<PropuestaPago>> listarPropuestasPagoPorPreventa(Long preventaId);
    Optional<PropuestaPago> porIdPropuestaPago(Long preventaId, Long propuestaId);
    Optional<Preventa> agregarPropuestaPago(Long preventaId, PropuestaPago propuesta);
    Optional<PropuestaPago> actualizarPropuestaPago(Long preventaId, Long propuestaId, PropuestaPago propuestaActualizada);
    Optional<Preventa> eliminarPropuestaPago(Long preventaId, Long propuestaId);
    Optional<PropuestaPago> aceptarPropuestaPagoPreventa(Long preventaId, Long propuestaId); // Método de negocio

    // CRUD Anidado para VisitaProgramada (a través de Preventa)
    Optional<List<VisitaProgramada>> listarVisitasProgramadasPorPreventa(Long preventaId);
    Optional<VisitaProgramada> porIdVisitaProgramada(Long preventaId, Long visitaId);
    Optional<Preventa> agregarVisitaProgramada(Long preventaId, VisitaProgramada visita);
    Optional<VisitaProgramada> actualizarVisitaProgramada(Long preventaId, Long visitaId, VisitaProgramada visitaActualizada);
    Optional<Preventa> eliminarVisitaProgramada(Long preventaId, Long visitaId);
    Optional<VisitaProgramada> reprogramarVisitaPreventa(Long preventaId, Long visitaId, LocalDate nuevaFecha); // Método de negocio
    Optional<VisitaProgramada> actualizarEstadoVisitaPreventa(Long preventaId, Long visitaId, EstadoVisita nuevoEstado); // Método de negocio
}