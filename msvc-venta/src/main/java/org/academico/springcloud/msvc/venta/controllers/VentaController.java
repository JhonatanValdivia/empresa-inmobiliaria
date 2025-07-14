package org.academico.springcloud.msvc.venta.controllers;

import org.academico.springcloud.msvc.venta.models.entities.DetalleVenta;
import org.academico.springcloud.msvc.venta.models.entities.Venta;
import org.academico.springcloud.msvc.venta.models.enums.EstadoVenta;
import org.academico.springcloud.msvc.venta.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            ventaDB.modificarEstado(venta.getEstado());
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

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarVenta(@PathVariable Long id) {
        Optional<Venta> ventaOp = ventaService.porId(id);
        if (ventaOp.isPresent()) {
            Venta venta = ventaOp.get();
            venta.modificarEstado(EstadoVenta.CONFIRMADA);
            ventaService.guardar(venta);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Venta confirmada"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{ventaId}/detalles/{detalleId}/pagar")
    public ResponseEntity<?> pagarDetalle(@PathVariable Long ventaId, @PathVariable Long detalleId) {
        Optional<Venta> ventaOp = ventaService.porId(ventaId);
        if (ventaOp.isPresent()) {
            Venta venta = ventaOp.get();
            for (DetalleVenta d : venta.getDetalleVentaLista()) {
                if (d.getId().equals(detalleId)) {
                    d.marcarComoPagado();
                    ventaService.guardar(venta);
                    return ResponseEntity.ok(Collections.singletonMap("mensaje", "Detalle marcado como pagado"));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "Detalle no encontrado"));
        }
        return ResponseEntity.notFound().build();
    }
}