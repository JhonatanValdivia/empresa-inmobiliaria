package org.academico.springcloud.msvc.cobro.controllers;

import org.academico.springcloud.msvc.cobro.models.entities.Cobro;
import org.academico.springcloud.msvc.cobro.services.CobroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cobro")
public class CobroController {
    @Autowired
    private CobroService cobroService;

    @GetMapping
    public ResponseEntity<?> listarCobros() {
        List<Cobro> cobros = cobroService.listarCobros();
        if (cobros.isEmpty()) {
            return ResponseEntity.status(204).body("No hay cobros registrados.");
        }
        return ResponseEntity.ok(cobros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCobroPorId(@PathVariable Long id) {
        Optional<Cobro> cobro = cobroService.porId(id);
        if (cobro.isPresent()) {
            return ResponseEntity.ok(cobro.get());
        } else {
            return ResponseEntity.status(404).body("Cobro con ID " + id + " no existe.");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearCobro(@RequestBody Cobro cobro) {
        try {
            if (cobro.getIdVenta() == null || cobro.getFechaCobro() == null || cobro.getMontoCobro() == null) {
                return ResponseEntity.badRequest().body("Id de venta, fecha y monto son obligatorios.");
            }
            // Validación de hora (0-23) y minutos (0-59)
            if (cobro.getFechaCobro().getHora() < 0 || cobro.getFechaCobro().getHora() > 23 ||
                    cobro.getFechaCobro().getMinuto() < 0 || cobro.getFechaCobro().getMinuto() > 59) {
                return ResponseEntity.badRequest().body("Hora debe estar entre 0 y 23, y minutos entre 0 y 59.");
            }
            Cobro cobroCreado = cobroService.crearCobro(cobro);
            return ResponseEntity.status(201).body(cobroCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCobro(@PathVariable Long id, @RequestBody Cobro cobro) {
        try {
            if (cobro.getFechaCobro() == null || cobro.getMontoCobro() == null) {
                return ResponseEntity.badRequest().body("Fecha y monto son obligatorios.");
            }
            // Validación de hora (0-23) y minutos (0-59)
            if (cobro.getFechaCobro().getHora() < 0 || cobro.getFechaCobro().getHora() > 23 ||
                    cobro.getFechaCobro().getMinuto() < 0 || cobro.getFechaCobro().getMinuto() > 59) {
                return ResponseEntity.badRequest().body("Hora debe estar entre 0 y 23, y minutos entre 0 y 59.");
            }
            Cobro cobroActualizado = cobroService.actualizarCobro(id, cobro);
            return cobroActualizado != null ? ResponseEntity.ok(cobroActualizado)
                    : ResponseEntity.status(404).body("Cobro con ID " + id + " no existe.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCobro(@PathVariable Long id) {
        Optional<Cobro> cobro = cobroService.porId(id);
        if (cobro.isPresent()) {
            cobroService.eliminarCobro(id);
            return ResponseEntity.ok("Cobro eliminado exitosamente.");
        }
        return ResponseEntity.status(404).body("Cobro con ID " + id + " no existe.");
    }

    @PutMapping("/{id}/registrar-pago")
    public ResponseEntity<?> registrarPago(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        try {
            Double monto = request.get("monto");
            if (monto == null || monto <= 0) {
                return ResponseEntity.badRequest().body("Monto invalido.");
            }
            Optional<Cobro> optionalCobro = cobroService.porId(id);
            if (optionalCobro.isPresent()) {
                Cobro cobro = optionalCobro.get();
                BigDecimal montoOriginal = cobro.getMontoCobro().getMonto();
                BigDecimal montoPago = BigDecimal.valueOf(monto);
                if (montoPago.compareTo(montoOriginal) != 0) {
                    String mensaje = montoPago.compareTo(montoOriginal) > 0 ?
                            "Estas pagando de mas. El monto debe ser " + montoOriginal + " " + cobro.getMontoCobro().getMoneda() :
                            "Estas pagando de menos. El monto debe ser " + montoOriginal + " " + cobro.getMontoCobro().getMoneda();
                    return ResponseEntity.badRequest().body(mensaje + ". Pago no registrado.");
                }
                Cobro cobroActualizado = cobroService.registrarPago(id, montoPago);
                return ResponseEntity.ok(cobroActualizado);
            }
            return ResponseEntity.status(404).body("Cobro con ID " + id + " no existe.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/generar-comprobante")
    public ResponseEntity<String> generarComprobante(@PathVariable Long id) {
        try {
            String comprobante = cobroService.generarComprobante(id);
            return ResponseEntity.ok(comprobante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}