package org.academico.springcloud.msvc.comision.controllers;


import org.academico.springcloud.msvc.comision.models.entities.Comision;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.academico.springcloud.msvc.comision.services.ComisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/comision")
public class ComisionController {

    @Autowired
    private ComisionService comisionService;

    @GetMapping
    public List<Comision> listar() {
        return comisionService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comision> detalle(@PathVariable Long id) {
        Optional<Comision> comision = comisionService.porIdConUsuario(id);
        return comision.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Comision comision) {
        try {
            Comision comisionDB = comisionService.guardar(comision);
            return ResponseEntity.status(HttpStatus.CREATED).body(comisionDB);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Ocurrió un error inesperado. Por favor, verifica los datos y vuelve a intentarlo."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Comision comision, @PathVariable Long id) {
        Optional<Comision> comisionOp = comisionService.porId(id);
        if (comisionOp.isPresent()) {
            Comision comisionDB = comisionOp.get();
            comisionDB.setEstadoComision(comision.getEstadoComision());
            comisionDB.setMontoComision(comision.getMontoComision());
            comisionDB.setTipoComision(comision.getTipoComision());
            comisionDB.setFechaPagoComision(comision.getFechaPagoComision());
            return ResponseEntity.status(HttpStatus.CREATED).body(comisionService.guardar(comisionDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        if (comisionService.existePorId(id)) {
            comisionService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contar")
    public ResponseEntity<?> contarComisiones() {
        return ResponseEntity.ok(comisionService.contarComisiones());
    }

    @PutMapping("/{comisionId}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long comisionId, @RequestParam EstadoComision estado) {
        comisionService.cambiarEstadoComision(comisionId, estado);
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Estado actualizado"));
    }

    @GetMapping("/activas")
    public ResponseEntity<?> listarActivas() {
        return ResponseEntity.ok(comisionService.listarActivas());
    }

    @PutMapping("/{comisionId}/pagar")
    public ResponseEntity<?> pagarComision(@PathVariable Long comisionId) {
        Optional<Comision> comisionOp = comisionService.porId(comisionId);
        if (comisionOp.isPresent()) {
            Comision comision = comisionOp.get();
            comision.pagarComision();
            comisionService.guardar(comision);
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Comisión marcada como pagada"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{comisionId}/verificar-pago")
    public ResponseEntity<?> verificarPago(@PathVariable Long comisionId) {
        Optional<Comision> comisionOp = comisionService.porId(comisionId);
        if (comisionOp.isPresent()) {
            Comision comision = comisionOp.get();
            boolean pagada = comision.verificarPago();
            return ResponseEntity.ok(Collections.singletonMap("pagada", pagada));
        }
        return ResponseEntity.notFound().build();
    }
}