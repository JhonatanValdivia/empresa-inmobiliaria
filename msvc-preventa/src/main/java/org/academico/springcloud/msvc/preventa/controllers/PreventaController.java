package org.academico.springcloud.msvc.preventa.controllers;


import org.academico.springcloud.msvc.preventa.models.entity.ContratoVenta;
import org.academico.springcloud.msvc.preventa.models.entity.Preventa;
import org.academico.springcloud.msvc.preventa.models.entity.PropuestaPago;
import org.academico.springcloud.msvc.preventa.models.entity.VisitaProgramada;
import org.academico.springcloud.msvc.preventa.models.enums.EstadoVisita;
import org.academico.springcloud.msvc.preventa.models.enums.MetodoPago;
import org.academico.springcloud.msvc.preventa.models.enums.TipoContrato;
import org.academico.springcloud.msvc.preventa.services.PreventaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/preventas")
public class PreventaController {

    @Autowired
    private PreventaService service;

    // --- CRUD para Preventa (Agregado Raíz) ---
    @GetMapping
    public ResponseEntity<List<Preventa>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Preventa> preventaOp = service.porId(id);
        if (preventaOp.isPresent()) {
            return ResponseEntity.ok(preventaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Preventa preventa, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(preventa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Preventa preventa, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Preventa> preventaOp = service.porId(id);
        if (preventaOp.isPresent()) {
            Preventa preventaDB = preventaOp.get();
            preventaDB.setFechaInicio(preventa.getFechaInicio());
            preventaDB.setEstado(preventa.getEstado());
            // Las colecciones anidadas (contratos, propuestas, visitas) se manejan
            // a través de sus propios endpoints anidados, no se actualizan directamente aquí.

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(preventaDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Preventa> preventaOp = service.porId(id);
        if (preventaOp.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- Métodos de Negocio para el Agregado Preventa ---
    @PutMapping("/{preventaId}/programar-visita")
    public ResponseEntity<?> programarVisita(@PathVariable Long preventaId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaVisita) {
        Optional<Preventa> preventaOp = service.programarVisitaEnPreventa(preventaId, fechaVisita);
        if (preventaOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(preventaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/registrar-contrato")
    public ResponseEntity<?> registrarContrato(@PathVariable Long preventaId, @RequestParam TipoContrato tipoContrato, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFirma) {
        Optional<Preventa> preventaOp = service.registrarContratoEnPreventa(preventaId, tipoContrato, fechaFirma);
        if (preventaOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(preventaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/registrar-pago")
    public ResponseEntity<?> registrarPago(@PathVariable Long preventaId, @RequestParam BigDecimal monto, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, @RequestParam MetodoPago metodo) {
        Optional<Preventa> preventaOp = service.registrarPagoEnPreventa(preventaId, monto, fecha, metodo);
        if (preventaOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(preventaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{preventaId}/calcular-comision")
    public ResponseEntity<?> calcularComision(@PathVariable Long preventaId) {
        Optional<BigDecimal> comisionOp = service.calcularComisionPreventa(preventaId);
        if (comisionOp.isPresent()) {
            return ResponseEntity.ok(Collections.singletonMap("comision", comisionOp.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/finalizar")
    public ResponseEntity<?> marcarComoFinalizado(@PathVariable Long preventaId) {
        try {
            Optional<Preventa> preventaOp = service.marcarPreventaComoFinalizada(preventaId);
            if (preventaOp.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(preventaOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }


    // --- CRUD Anidado para ContratoVenta ---
    @GetMapping("/{preventaId}/contratos")
    public ResponseEntity<List<ContratoVenta>> listarContratos(@PathVariable Long preventaId) {
        Optional<List<ContratoVenta>> contratosOp = service.listarContratosPorPreventa(preventaId);
        if (contratosOp.isPresent()) {
            return ResponseEntity.ok(contratosOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{preventaId}/contratos/{contratoId}")
    public ResponseEntity<?> detalleContrato(@PathVariable Long preventaId, @PathVariable Long contratoId) {
        Optional<ContratoVenta> contratoOp = service.porIdContratoVenta(preventaId, contratoId);
        if (contratoOp.isPresent()) {
            return ResponseEntity.ok(contratoOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{preventaId}/contratos")
    public ResponseEntity<?> crearContrato(@PathVariable Long preventaId,  @RequestBody ContratoVenta contrato, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Preventa> preventaActualizadaOp = service.agregarContratoVenta(preventaId, contrato);
        if (preventaActualizadaOp.isPresent()) {
            // Retornar el contrato recién creado si se desea, o el agregado actualizado
            ContratoVenta createdContrato = preventaActualizadaOp.get().getContratosVenta().stream()
                    .filter(c -> contrato.getTipoContrato().equals(c.getTipoContrato()) && contrato.getFechaFirma().equals(c.getFechaFirma())) // Esto puede ser riesgoso si hay múltiples contratos idénticos
                    .reduce((first, second) -> second) // Get the last one added (likely the new one)
                    .orElse(null); // Fallback
            return ResponseEntity.status(HttpStatus.CREATED).body(createdContrato != null ? createdContrato : preventaActualizadaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/contratos/{contratoId}")
    public ResponseEntity<?> editarContrato(@PathVariable Long preventaId, @PathVariable Long contratoId,  @RequestBody ContratoVenta contrato, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<ContratoVenta> contratoActualizadoOp = service.actualizarContratoVenta(preventaId, contratoId, contrato);
        if (contratoActualizadoOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(contratoActualizadoOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{preventaId}/contratos/{contratoId}")
    public ResponseEntity<?> eliminarContrato(@PathVariable Long preventaId, @PathVariable Long contratoId) {
        Optional<Preventa> preventaOp = service.eliminarContratoVenta(preventaId, contratoId);
        if (preventaOp.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/contratos/{contratoId}/anular")
    public ResponseEntity<?> anularContrato(@PathVariable Long preventaId, @PathVariable Long contratoId) {
        try {
            Optional<ContratoVenta> contratoOp = service.anularContratoPreventa(preventaId, contratoId);
            if (contratoOp.isPresent()) {
                return ResponseEntity.ok(contratoOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }


    // --- CRUD Anidado para PropuestaPago ---
    @GetMapping("/{preventaId}/propuestas-pago")
    public ResponseEntity<List<PropuestaPago>> listarPropuestasPago(@PathVariable Long preventaId) {
        Optional<List<PropuestaPago>> propuestasOp = service.listarPropuestasPagoPorPreventa(preventaId);
        if (propuestasOp.isPresent()) {
            return ResponseEntity.ok(propuestasOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{preventaId}/propuestas-pago/{propuestaId}")
    public ResponseEntity<?> detallePropuestaPago(@PathVariable Long preventaId, @PathVariable Long propuestaId) {
        Optional<PropuestaPago> propuestaOp = service.porIdPropuestaPago(preventaId, propuestaId);
        if (propuestaOp.isPresent()) {
            return ResponseEntity.ok(propuestaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{preventaId}/propuestas-pago")
    public ResponseEntity<?> crearPropuestaPago(@PathVariable Long preventaId,  @RequestBody PropuestaPago propuesta, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Preventa> preventaActualizadaOp = service.agregarPropuestaPago(preventaId, propuesta);
        if (preventaActualizadaOp.isPresent()) {
            PropuestaPago createdPropuesta = preventaActualizadaOp.get().getPropuestasPago().stream()
                    .filter(p -> propuesta.getMonto().equals(p.getMonto()) && propuesta.getFecha().equals(p.getFecha()))
                    .reduce((first, second) -> second)
                    .orElse(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPropuesta != null ? createdPropuesta : preventaActualizadaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/propuestas-pago/{propuestaId}")
    public ResponseEntity<?> editarPropuestaPago(@PathVariable Long preventaId, @PathVariable Long propuestaId, @RequestBody PropuestaPago propuesta, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<PropuestaPago> propuestaActualizadaOp = service.actualizarPropuestaPago(preventaId, propuestaId, propuesta);
        if (propuestaActualizadaOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(propuestaActualizadaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{preventaId}/propuestas-pago/{propuestaId}")
    public ResponseEntity<?> eliminarPropuestaPago(@PathVariable Long preventaId, @PathVariable Long propuestaId) {
        Optional<Preventa> preventaOp = service.eliminarPropuestaPago(preventaId, propuestaId);
        if (preventaOp.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/propuestas-pago/{propuestaId}/aceptar")
    public ResponseEntity<?> aceptarPropuesta(@PathVariable Long preventaId, @PathVariable Long propuestaId) {
        Optional<PropuestaPago> propuestaOp = service.aceptarPropuestaPagoPreventa(preventaId, propuestaId);
        if (propuestaOp.isPresent()) {
            return ResponseEntity.ok(propuestaOp.get());
        }
        return ResponseEntity.notFound().build();
    }


    // --- CRUD Anidado para VisitaProgramada ---
    @GetMapping("/{preventaId}/visitas-programadas")
    public ResponseEntity<List<VisitaProgramada>> listarVisitasProgramadas(@PathVariable Long preventaId) {
        Optional<List<VisitaProgramada>> visitasOp = service.listarVisitasProgramadasPorPreventa(preventaId);
        if (visitasOp.isPresent()) {
            return ResponseEntity.ok(visitasOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{preventaId}/visitas-programadas/{visitaId}")
    public ResponseEntity<?> detalleVisitaProgramada(@PathVariable Long preventaId, @PathVariable Long visitaId) {
        Optional<VisitaProgramada> visitaOp = service.porIdVisitaProgramada(preventaId, visitaId);
        if (visitaOp.isPresent()) {
            return ResponseEntity.ok(visitaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{preventaId}/visitas-programadas")
    public ResponseEntity<?> crearVisitaProgramada(@PathVariable Long preventaId,@RequestBody VisitaProgramada visita, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Preventa> preventaActualizadaOp = service.agregarVisitaProgramada(preventaId, visita);
        if (preventaActualizadaOp.isPresent()) {
            VisitaProgramada createdVisita = preventaActualizadaOp.get().getVisitasProgramadas().stream()
                    .filter(v -> visita.getFecha().equals(v.getFecha()) && visita.getEstadoVisita().equals(v.getEstadoVisita()))
                    .reduce((first, second) -> second)
                    .orElse(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVisita != null ? createdVisita : preventaActualizadaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/visitas-programadas/{visitaId}")
    public ResponseEntity<?> editarVisitaProgramada(@PathVariable Long preventaId, @PathVariable Long visitaId, @RequestBody VisitaProgramada visita, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<VisitaProgramada> visitaActualizadaOp = service.actualizarVisitaProgramada(preventaId, visitaId, visita);
        if (visitaActualizadaOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(visitaActualizadaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{preventaId}/visitas-programadas/{visitaId}")
    public ResponseEntity<?> eliminarVisitaProgramada(@PathVariable Long preventaId, @PathVariable Long visitaId) {
        Optional<Preventa> preventaOp = service.eliminarVisitaProgramada(preventaId, visitaId);
        if (preventaOp.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{preventaId}/visitas-programadas/{visitaId}/reprogramar")
    public ResponseEntity<?> reprogramarVisita(@PathVariable Long preventaId, @PathVariable Long visitaId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha) {
        try {
            Optional<VisitaProgramada> visitaOp = service.reprogramarVisitaPreventa(preventaId, visitaId, nuevaFecha);
            if (visitaOp.isPresent()) {
                return ResponseEntity.ok(visitaOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }

    @PutMapping("/{preventaId}/visitas-programadas/{visitaId}/actualizar-estado")
    public ResponseEntity<?> actualizarEstadoVisita(@PathVariable Long preventaId, @PathVariable Long visitaId, @RequestParam EstadoVisita nuevoEstado) {
        Optional<VisitaProgramada> visitaOp = service.actualizarEstadoVisitaPreventa(preventaId, visitaId, nuevoEstado);
        if (visitaOp.isPresent()) {
            return ResponseEntity.ok(visitaOp.get());
        }
        return ResponseEntity.notFound().build();
    }


    // Método de utilidad para validación
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = result.getFieldErrors().stream()
                .collect(Collectors.toMap(err -> err.getField(), err -> err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}