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
    Preventa guardar(Preventa preventa, Long idPropiedad);
    void eliminar(Long id);
    Optional<Preventa> aprobarPreventa(Long preventaId);

    // CRUD Anidado para ContratoVenta (a través de Preventa)
    Optional<ContratoVenta> agregarContratoVenta(Long preventaId, ContratoVenta contrato);
    Optional<ContratoVenta> obtenerContratoPorPreventa(Long preventaId);
    Optional<ContratoVenta> actualizarContratoVenta(Long preventaId, ContratoVenta contratoActualizado);
    Optional<Preventa> eliminarContratoVenta(Long preventaId);
    Optional<ContratoVenta> anularContratoPreventa(Long preventaId);


    // CRUD Anidado para PropuestaPago (a través de Preventa)
    Optional<List<PropuestaPago>> listarPropuestasPagoPorPreventa(Long preventaId);
    Optional<PropuestaPago> porIdPropuestaPago(Long preventaId, Long propuestaId);
    Optional<Preventa> agregarPropuestaPago(Long preventaId, PropuestaPago propuesta);
    Optional<PropuestaPago> actualizarPropuestaPago(Long preventaId, Long propuestaId, PropuestaPago propuestaActualizada);
    Optional<PropuestaPago> aceptarPropuestaPagoPreventa(Long preventaId, Long propuestaId); // Método de negocio

    // CRUD Anidado para VisitaProgramada (a través de Preventa)
    Optional<Preventa> agregarVisitaProgramada(Long preventaId, VisitaProgramada visita);
    Optional<List<VisitaProgramada>> listarVisitasProgramadasPorPreventa(Long preventaId);
    Optional<VisitaProgramada> actualizarVisitaProgramada(Long preventaId, Long visitaId, VisitaProgramada visitaActualizada);
    Optional<VisitaProgramada> reprogramarVisitaPreventa(Long preventaId, Long visitaId, LocalDate fecha); // Método de negocio
    Optional<VisitaProgramada> actualizarEstadoVisitaPreventa(Long preventaId, Long visitaId, EstadoVisita estadoVisita     ); // Método de negocio

    //relacion con el microservicio Usuarios
    Preventa asociarUsuariosPreventa(Long idPreventa, Long idAgente, Long idCliente);

    //Relación con el microservicio Propiedades
    Preventa asociarPropiedadPreventa(Long idPreventa, Long idPropiedad);
}