package org.academico.springcloud.msvc.venta.controllers;

import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;
import org.academico.springcloud.msvc.venta.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleVenta(@PathVariable Long id) {
        Optional<Venta> ventaOp = ventaService.porId(id);
        if (ventaOp.isPresent()) {
            return ResponseEntity.ok(ventaOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Venta venta) {
        Venta ventaDB = ventaService.guardar(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Venta venta, @PathVariable Long id) {
        Optional<Venta> ventaOp = ventaService.porId(id);
        if (ventaOp.isPresent()) {
            Venta ventaDB = ventaOp.get();
            ventaDB.setFecha(venta.getFecha());
            ventaDB.setPrecioVenta(venta.getPrecioVenta());
            ventaDB.setTipoVenta(venta.getTipoVenta());
            ventaDB.setEstado(venta.getEstado());
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.guardar(ventaDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (ventaService.existePorId(id)) {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contar")
    public ResponseEntity<?> contarVentas() {
        return ResponseEntity.ok(ventaService.contarVentas());
    }

    // Métodos para manejar la relación entre Venta y DetalleVenta
    @PostMapping("/{ventaId}/detalles")
    public ResponseEntity<?> agregarDetalle(@PathVariable Long ventaId, @RequestBody DetalleVenta detalleVenta) {
        ventaService.agregarDetalle(ventaId, detalleVenta);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{ventaId}/detalles/{detalleId}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Long ventaId, @PathVariable Long detalleId) {
        ventaService.eliminarDetalle(ventaId, detalleId);
        return ResponseEntity.noContent().build();
    }


    //Metodos remotos para la relacion con Preventa

        @PutMapping("/{ventaId}/asignar-preventa/{preventaId}")
    public ResponseEntity<?> asignarPreventa(@PathVariable Long ventaId, @PathVariable Long preventaId) {
        try {
            Optional<Venta> ventaOp = ventaService.asignarPreventa(ventaId, preventaId);
            if (ventaOp.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(ventaOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping("/{ventaId}/desasignar-preventa")
    public ResponseEntity<?> desasignarPreventa(@PathVariable Long ventaId) {
        try {
            Optional<Venta> ventaOp = ventaService.desasignarPreventa(ventaId);
            if (ventaOp.isPresent()) {
                return ResponseEntity.ok(ventaOp.get());
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = result.getFieldErrors().stream()
                .collect(Collectors.toMap(err -> err.getField(), err -> err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    }

