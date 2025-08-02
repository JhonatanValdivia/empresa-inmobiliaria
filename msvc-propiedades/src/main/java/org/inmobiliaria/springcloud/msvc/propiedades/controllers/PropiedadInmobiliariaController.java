package org.inmobiliaria.springcloud.msvc.propiedades.controllers;

import feign.FeignException;
import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.inmobiliaria.springcloud.msvc.propiedades.models.entitys.*;
import org.inmobiliaria.springcloud.msvc.propiedades.services.PropiedadInmobiliariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/propiedades")
public class PropiedadInmobiliariaController {
    @Autowired
    private  PropiedadInmobiliariaService service;


    @GetMapping
    public ResponseEntity<List<PropiedadInmobiliaria>> getAll() {
        List<PropiedadInmobiliaria> list = service.buscarTodas();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadInmobiliaria> getById(@PathVariable Long id) {
        Optional<PropiedadInmobiliaria> op = service.buscarPorId(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PropiedadInmobiliaria> create(@RequestBody PropiedadInmobiliaria propiedad) {
        PropiedadInmobiliaria created = service.guardar(propiedad);
        URI uri = URI.create("/api/propiedades/" + created.getIdPropiedad());
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadInmobiliaria> update(
            @PathVariable Long id,
            @RequestBody PropiedadInmobiliaria propiedad
    ) {
        propiedad.setIdPropiedad(id);
        PropiedadInmobiliaria updated = service.guardar(propiedad);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Documentos
    @PostMapping("/{id}/documentos")
    public ResponseEntity<PropiedadInmobiliaria> addDocumento(
            @PathVariable Long id,
            @RequestBody DocumentoLegal documento
    ) {
        Optional<PropiedadInmobiliaria> op = service.agregarDocumento(id, documento);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/documentos/{docId}")
    public ResponseEntity<PropiedadInmobiliaria> editDocumento(
            @PathVariable Long id,
            @PathVariable Long docId,
            @RequestBody DocumentoLegal documento
    ) {
        documento.setIdDocumentoLegal(docId);
        Optional<PropiedadInmobiliaria> op = service.editarDocumento(id, documento);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}/documentos/{docId}")
    public ResponseEntity<PropiedadInmobiliaria> removeDocumento(
            @PathVariable Long id,
            @PathVariable Long docId
    ) {
        Optional<PropiedadInmobiliaria> op = service.removerDocumento(id, docId);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Servicios
    @PostMapping("/{id}/servicios")
    public ResponseEntity<PropiedadInmobiliaria> addServicio(
            @PathVariable Long id,
            @RequestBody Servicio servicio
    ) {
        Optional<PropiedadInmobiliaria> op = service.agregarServicio(id, servicio);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/servicios/{servicioId}")
    public ResponseEntity<PropiedadInmobiliaria> removeServicio(
            @PathVariable Long id,
            @PathVariable Long servicioId
    ) {
        Optional<PropiedadInmobiliaria> op = service.removerServicio(id, servicioId);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/servicios/{servicioId}")
    public ResponseEntity<PropiedadInmobiliaria> editServicio(
            @PathVariable Long id,
            @PathVariable Long servicioId,
            @RequestBody Servicio servicio
    ) {
        servicio.setIdServicio(servicioId);
        Optional<PropiedadInmobiliaria> op = service.editarServicio(id, servicio);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Expediente
    @PostMapping("/{id}/expediente")
    public ResponseEntity<PropiedadInmobiliaria> assignExpediente(
            @PathVariable Long id,
            @RequestBody Expediente expediente
    ) {
        Optional<PropiedadInmobiliaria> op = service.asignarExpediente(id, expediente);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/expediente")
    public ResponseEntity<PropiedadInmobiliaria> removeExpediente(
            @PathVariable Long id
    ) {
        Optional<PropiedadInmobiliaria> op = service.quitarExpediente(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/expediente/{expedienteId}")
    public ResponseEntity<PropiedadInmobiliaria> editExpediente(
            @PathVariable Long id,
            @PathVariable Long expedienteId,
            @RequestBody Expediente expediente
    ) {
        expediente.setIdExpediente(expedienteId);
        Optional<PropiedadInmobiliaria> op = service.editarExpediente(id, expediente);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Planos via expediente
    @PostMapping("/{id}/expediente/planos")
    public ResponseEntity<PropiedadInmobiliaria> addPlano(
            @PathVariable Long id,
            @RequestBody Plano plano
    ) {
        Optional<PropiedadInmobiliaria> op = service.agregarPlano(id, plano);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/expediente/planos/{planoId}")
    public ResponseEntity<PropiedadInmobiliaria> removePlano(
            @PathVariable Long id,
            @PathVariable Long planoId
    ) {
        Optional<PropiedadInmobiliaria> op = service.removerPlano(id, planoId);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/expediente/planos/{planoId}")
    public ResponseEntity<PropiedadInmobiliaria> editPlano(
            @PathVariable Long id,
            @PathVariable Long planoId,
            @RequestBody Plano plano
    ) {
        plano.setIdPlano(planoId); // aseguramos que el ID esté bien seteado
        Optional<PropiedadInmobiliaria> op = service.editarPlano(id, plano);

        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    //metodos con agregao norma

    @PutMapping("/asignar-norma/{propiedadId}")
    public ResponseEntity<?> asignarNorma(@RequestBody Norma norma, @PathVariable Long propiedadId) {
        Optional<Norma> normaOptional;
        try {
            normaOptional = service.asignarNorma(norma, propiedadId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "No se pudo asignar la norma, error en la comunicación: " + e.getMessage()));
        }

        if (normaOptional.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(normaOptional.get());

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-norma/{propiedadId}")
    public ResponseEntity<?> eliminarNorma(@RequestBody Norma norma, @PathVariable Long propiedadId) {
        Optional<Norma> normaOptional;
        try {
            normaOptional = service.eliminarNorma(norma, propiedadId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje", "No se pudo eliminar la norma, error en la comunicación: " + e.getMessage()));
        }

        if (normaOptional.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(normaOptional.get());

        return ResponseEntity.notFound().build();
    }



    @GetMapping("/listar-normas")
    public ResponseEntity<?> listarNormas() {
        List<PropiedadInmobiliaria> propiedades;
        try {
            propiedades = service.listarNormas();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("Mensaje", "Error al listar normas, error en la comunicación: " + e.getMessage()));
        }

        if (!propiedades.isEmpty()) {
            return ResponseEntity.ok(propiedades);
        }

        return ResponseEntity.notFound().build();
    }
}
