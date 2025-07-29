package org.academico.springcloud.msvc.preventa.controllers;


import org.academico.springcloud.msvc.preventa.clients.PropiedadClientRest;
import org.academico.springcloud.msvc.preventa.models.PropiedadInmobiliaria;
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
public class  PreventaController {

    @Autowired
    private PreventaService service;

    @Autowired private
    PropiedadClientRest propiedadClientRest;

    // Agregado Raiz:
    @GetMapping
    public ResponseEntity<List<Preventa>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Preventa> preventaOp = service.porId(id);
        if (preventaOp.isPresent()) {
            Preventa preventa = preventaOp.get();
            // Obtener los detalles de la propiedad usando el cliente Feign
            PropiedadInmobiliaria propiedad = propiedadClientRest.detallePropiedad(preventa.getPropiedadId());
            preventa.setPropiedad(propiedad); // Asegúrate de que Preventa tenga un campo 'propiedad' para esto
            return ResponseEntity.ok(preventa);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Preventa preventa, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        try {
            // Validar que la propiedad exista antes de guardar
            PropiedadInmobiliaria propiedad = propiedadClientRest.detallePropiedad(preventa.getPropiedadId());
            if (propiedad == null) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "La propiedad con ID " + preventa.getPropiedadId() + " no existe"));
            }
            Preventa preventaGuardada = service.guardar(preventa, preventa.getPropiedadId());
            return ResponseEntity.status(HttpStatus.CREATED).body(preventaGuardada);
        } catch (feign.FeignException.NotFound e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "La propiedad con ID " + preventa.getPropiedadId() + " no existe"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
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
            // Si se proporciona un nuevo idPropiedad en el JSON, actualizarlo
            if (preventa.getPropiedadId() != null) {
                try {
                    preventaDB = service.guardar(preventaDB, preventa.getPropiedadId());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
                }
            } else {
                preventaDB = service.guardar(preventaDB, preventaDB.getPropiedadId()); // Mantener el mismo idPropiedad
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(preventaDB);
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



    // Métodos de Negocio para el Agregado Preventa
    @PutMapping("/{preventaId}/aprobar")
    public ResponseEntity<?> aprobarPreventa(@PathVariable Long preventaId) {
        try {
            Optional<Preventa> preventaOp = service.aprobarPreventa(preventaId);

            return preventaOp
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    //  CRUD Anidado para ContratoVenta

    @PostMapping("/{preventaId}/contrato")
    public ResponseEntity<?> crearContrato(@PathVariable Long preventaId, @RequestBody ContratoVenta contrato, BindingResult result) {
        if (result.hasErrors()) return validar(result);
        try {
            return service.agregarContratoVenta(preventaId, contrato)
                    .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/{preventaId}/contrato")
    public ResponseEntity<?> obtenerContrato(@PathVariable Long preventaId) {
        return service.obtenerContratoPorPreventa(preventaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{preventaId}/contrato")
    public ResponseEntity<?> editarContrato(@PathVariable Long preventaId, @RequestBody ContratoVenta contrato, BindingResult result) {
        if (result.hasErrors()) return validar(result);
        try {
            return service.actualizarContratoVenta(preventaId, contrato)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{preventaId}/contrato")
    public ResponseEntity<?> eliminarContrato(@PathVariable Long preventaId) {
        try {
            return service.eliminarContratoVenta(preventaId)
                    .map(p -> ResponseEntity.noContent().build())
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping("/{preventaId}/contrato/anular")
    public ResponseEntity<?> anularContrato(@PathVariable Long preventaId) {
        try {
            return service.anularContratoPreventa(preventaId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    //  CRUD Anidado para PropuestaPago

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


    @PutMapping("/{preventaId}/propuestas-pago/{propuestaId}/aceptar")
    public ResponseEntity<?> aceptarPropuesta(@PathVariable Long preventaId, @PathVariable Long propuestaId) {
        Optional<PropuestaPago> propuestaOp = service.aceptarPropuestaPagoPreventa(preventaId, propuestaId);
        if (propuestaOp.isPresent()) {
            return ResponseEntity.ok(propuestaOp.get());
        }
        return ResponseEntity.notFound().build();
    }


    // --- CRUD Anidado para VisitaProgramada ---

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


    @GetMapping("/{preventaId}/visitas-programadas")
    public ResponseEntity<List<VisitaProgramada>> listarVisitasProgramadas(@PathVariable Long preventaId) {
        Optional<List<VisitaProgramada>> visitasOp = service.listarVisitasProgramadasPorPreventa(preventaId);
        if (visitasOp.isPresent()) {
            return ResponseEntity.ok(visitasOp.get());
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

    @PutMapping("/{preventaId}/visitas-programadas/{visitaId}/reprogramar")
    public ResponseEntity<?> reprogramarVisita(@PathVariable Long preventaId, @PathVariable Long visitaId, @RequestBody VisitaProgramada request) {
        try {
            // Se extrae la fecha del objeto request
            Optional<VisitaProgramada> visitaOp = service.reprogramarVisitaPreventa(preventaId, visitaId, request.getFecha());
            if (visitaOp.isPresent()) {
                return ResponseEntity.ok(visitaOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }


    @PutMapping("/{preventaId}/visitas-programadas/{visitaId}/actualizar-estado")
    public ResponseEntity<?> actualizarEstadoVisita(@PathVariable Long preventaId, @PathVariable Long visitaId, @RequestBody VisitaProgramada request) {
        // Se extrae el estado del objeto request
        Optional<VisitaProgramada> visitaOp = service.actualizarEstadoVisitaPreventa(preventaId, visitaId, request.getEstadoVisita());
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

    //Metodo para asociar usuarios a la preventa(agente y cliente)
    @PutMapping("/{idPreventa}/asociar-usuarios")
    public  ResponseEntity<?> ascoarUsuarios(@PathVariable Long idPreventa,@RequestParam Long idAgente, @RequestParam Long idCliente){
        try{
            Preventa preventaUsuarios=service.asociarUsuariosPreventa(idPreventa,idAgente,idCliente);
            return ResponseEntity.ok(preventaUsuarios);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",e.getMessage()));
        }
    }

    @PutMapping("/{idPreventa}/asociar-propiedad")
    public ResponseEntity<?> asociarPropiedad(@PathVariable Long idPreventa, @RequestParam Long idPropiedad) {
        try {
            Preventa preventa = service.asociarPropiedadPreventa(idPreventa, idPropiedad);
            return ResponseEntity.ok(preventa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}