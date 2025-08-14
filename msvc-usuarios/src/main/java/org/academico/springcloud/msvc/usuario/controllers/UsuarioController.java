package org.academico.springcloud.msvc.usuario.controllers;

import jakarta.validation.Valid;
import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.academico.springcloud.msvc.usuario.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOp = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOp.isPresent()) {
            return ResponseEntity.ok(usuarioOp.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        Map<String, String> errores = new HashMap<>();

        // Validar campos obligatorios a nivel de objeto
        if (usuario.getNombreCompleto() == null) {
            errores.put("nombreCompleto", "Falta el nombre completo.");
        }
        if (usuario.getTipoUsuario() == null) {
            errores.put("tipoUsuario", "Falta el tipo de usuario.");
        }
        if (usuario.getTelefono() == null) {
            errores.put("telefono", "Falta el teléfono.");
        }
        if (usuario.getCorreoElectronico() == null) {
            errores.put("correoElectronico", "Falta el correo electrónico.");
        }
        if (usuario.getDireccion() == null) {
            errores.put("direccion", "Falta la dirección.");
        }

        // Si hay errores de validación internos (BindingResult)
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errores.put(error.getField(), error.getDefaultMessage());
            }
        }

        // Procesar errores
        if (!errores.isEmpty()) {
            if (errores.size() == 1) {
                return ResponseEntity.badRequest().body(errores.values().iterator().next());
            } else {
                return ResponseEntity.badRequest().body("Faltan los siguientes objetos: " + String.join(", ", errores.keySet()));
            }
        }

        Usuario usuarioDB = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioOP = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOP.isPresent()) {
            Usuario usuarioDB = usuarioOP.get();
            usuarioDB.setNombreCompleto(usuario.getNombreCompleto());
            usuarioDB.setTipoUsuario(usuario.getTipoUsuario());
            usuarioDB.setCorreoElectronico(usuario.getCorreoElectronico());
            usuarioDB.setDireccion(usuario.getDireccion());
            usuarioDB.setTelefono(usuario.getTelefono());

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardarUsuario(usuarioDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (usuarioService.existePorId(id)) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contar")
    public ResponseEntity<?> contarUsuarios() {
        return ResponseEntity.ok(usuarioService.contarUsuarios());
    }
}