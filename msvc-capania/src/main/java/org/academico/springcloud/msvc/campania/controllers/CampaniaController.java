package org.academico.springcloud.msvc.campania.controllers;

import feign.FeignException;
import org.academico.springcloud.msvc.campania.models.Propiedad;
import org.academico.springcloud.msvc.campania.models.entities.Campania;
import org.academico.springcloud.msvc.campania.models.entities.ProveedorPublicidad;
import org.academico.springcloud.msvc.campania.services.CampaniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/campania")
public class CampaniaController {
    @Autowired
    private CampaniaService service;

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Campania> campanias = service.listar();

        if (campanias.isEmpty()) {
            return ResponseEntity.status(204).body("No hay campañas registradas.");
        }

        return ResponseEntity.ok(campanias);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Long id) {
        try {
            Optional<Campania> campaniaOpt = service.porId(id);
            if (campaniaOpt.isPresent()) {
                return ResponseEntity.ok(campaniaOpt.get());
            } else {
                return ResponseEntity.status(404).body("La campaña con ID " + id + " no existe.");
            }
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "Error al conectar con msvc-propiedades: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Campania campania) {
        // Validar que al menos un proveedor exista
        if (campania.getProveedores() == null || campania.getProveedores().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe proporcionar al menos un proveedor para crear la campaña.");
        }

        // Validar cada proveedor
        for (ProveedorPublicidad proveedor : campania.getProveedores()) {
            if (proveedor.getIdProveedor() == null) {
                try {
                    proveedor.agregarProveedor();
                } catch (IllegalStateException e) {
                    return ResponseEntity.badRequest().body("Error al agregar proveedor: " + e.getMessage());
                }
            } else {
                boolean proveedorExiste = service.listar().stream()
                        .flatMap(c -> c.getProveedores().stream())
                        .anyMatch(p -> p.getIdProveedor() != null && p.getIdProveedor().equals(proveedor.getIdProveedor()));
                if (!proveedorExiste) {
                    return ResponseEntity.badRequest().body("El proveedor con ID " + proveedor.getIdProveedor() + " no existe.");
                }
            }
        }

        // Validar que el monto sea obligatorio y al menos 5
        if (campania.getMonto() == null) {
            return ResponseEntity.badRequest().body("El monto es obligatorio para crear la campaña.");
        }
        if (campania.getMonto().compareTo(BigDecimal.valueOf(5)) < 0) {
            return ResponseEntity.badRequest().body("El monto debe ser al menos 5.");
        }

        // Validar que la propiedad exista
        if (campania.getIdPropiedad() != null) {
            try {
                Propiedad propiedad = service.getPropiedadClient().detalle(campania.getIdPropiedad());
                if (propiedad == null) {
                    return ResponseEntity.status(404).body("La propiedad con ID " + campania.getIdPropiedad() + " no existe.");
                }
            } catch (FeignException e) {
                if (e.status() == 404) {
                    return ResponseEntity.status(404).body("La propiedad con ID " + campania.getIdPropiedad() + " no existe.");
                } else {
                    return ResponseEntity.status(503).body("Error de conexión con msvc-propiedades: " + e.getMessage());
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error interno al validar la propiedad: " + e.getMessage());
            }
        }
        try {
            campania.crearCampania();
            Campania nuevaCampania = service.guardar(campania);
            return ResponseEntity.status(201).body(nuevaCampania);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Error al crear la campaña: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/aprobar-monto")
    public ResponseEntity<?> aprobarMonto(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        Optional<Campania> campaniaOpt = service.porId(id);
        if (campaniaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("La campaña con ID " + id + " no existe.");
        }
        try {
            BigDecimal nuevoMonto = request.get("monto");
            if (nuevoMonto == null || nuevoMonto.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("El monto debe ser mayor que cero.");
            }
            Campania campania = campaniaOpt.get();
            campania.aprobarMonto(nuevoMonto);
            return ResponseEntity.ok(service.guardar(campania));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Campania> campaniaOpt = service.porId(id);

        if (campaniaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("La campaña con ID " + id + " no existe.");
        }
        service.eliminar(id);
        return ResponseEntity.ok("Campaña eliminada exitosamente.");
    }

    @PutMapping("/{idCampania}/proveedor/{idProveedor}")
    public ResponseEntity<?> editarProveedor(
            @PathVariable Long idCampania,
            @PathVariable Long idProveedor,
            @RequestBody Map<String, String> body) {

        Optional<Campania> campaniaOpt = service.porId(idCampania);
        if (campaniaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Campaña no encontrada");
        }

        Campania campania = campaniaOpt.get();
        ProveedorPublicidad proveedor = campania.getProveedores().stream()
                .filter(p -> p.getIdProveedor().equals(idProveedor))
                .findFirst()
                .orElse(null);

        if (proveedor == null) {
            return ResponseEntity.status(404).body("Proveedor no encontrado en la campaña");
        }

        try {
            // Lógica de validación que tú mismo definiste
            proveedor.editarProveedor(
                    body.getOrDefault("nombre", proveedor.getNombre()),
                    body.getOrDefault("cuentaPublicitaria", proveedor.getCuentaPublicitaria())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        // Guarda la campaña con el proveedor actualizado
        return ResponseEntity.ok(service.guardar(campania));
    }

    @DeleteMapping("/{idCampania}/proveedor/{idProveedor}")
    public ResponseEntity<?> eliminarProveedor(
            @PathVariable Long idCampania,
            @PathVariable Long idProveedor) {

        Optional<Campania> campaniaOpt = service.porId(idCampania);
        if (campaniaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Campaña no encontrada");
        }

        Campania campania = campaniaOpt.get();

        if (campania.getProveedores().size() <= 1) {
            return ResponseEntity.badRequest().body("No se puede eliminar el último proveedor de la campaña");
        }

        ProveedorPublicidad proveedor = campania.getProveedores().stream()
                .filter(p -> p.getIdProveedor().equals(idProveedor))
                .findFirst()
                .orElse(null);

        if (proveedor == null) {
            return ResponseEntity.status(404).body("Proveedor no encontrado en la campaña");
        }

        try {
            proveedor.eliminarProveedor();
            campania.getProveedores().remove(proveedor);
            service.guardar(campania);
            return ResponseEntity.ok("Proveedor eliminado exitosamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}