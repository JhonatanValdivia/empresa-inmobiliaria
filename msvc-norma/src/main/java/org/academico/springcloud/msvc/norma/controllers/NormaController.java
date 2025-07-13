package org.academico.springcloud.msvc.norma.controllers;

import org.academico.springcloud.msvc.norma.models.entity.Norma;
import org.academico.springcloud.msvc.norma.models.enums.TipoNorma;
import org.academico.springcloud.msvc.norma.services.NormaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/norma")
public class NormaController {

    @Autowired
    private NormaService service;

    // Listar todas las normas
    @GetMapping
    public List<Norma> listar() {
        return service.listar();
    }

    // Ver detalle de una norma por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Norma> normaOp = service.porId(id);
        if (normaOp.isPresent())
            return ResponseEntity.ok(normaOp.get());

        return ResponseEntity.notFound().build();
    }

    // Crear nueva norma
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Norma norma) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(norma));
    }

    // Actualizar norma existente
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Norma norma, @PathVariable Long id) {
        Optional<Norma> normaOp = service.porId(id);
        if (normaOp.isPresent()) {
            Norma normaBD = normaOp.get();
            normaBD.setFecha(norma.getFecha());
            normaBD.setTipo(norma.getTipo());
            normaBD.setEstadoNorma(norma.getEstadoNorma());
            normaBD.setDescripcion(norma.getDescripcion());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(normaBD));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar norma
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Norma> normaOp = service.porId(id);
        if (normaOp.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar normas por tipo
    @GetMapping("/tipo/{tipo}")
    public List<Norma> porTipo(@PathVariable TipoNorma tipo) {
        return service.porTipo(tipo);
    }

    // Buscar normas por estado
    @GetMapping("/estado/{estado}")
    public List<Norma> porEstado(@PathVariable String estado) {
        return service.porEstado(estado);
    }
}