package org.academico.springcloud.msvc.usuario.controllers;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.academico.springcloud.msvc.usuario.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return usuarios.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(201).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error al crear el usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
            return actualizado != null
                    ? ResponseEntity.ok(actualizado)
                    : ResponseEntity.status(404).body("Usuario con ID " + id + " no existe.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        return eliminado
                ? ResponseEntity.ok("Usuario eliminado exitosamente.")
                : ResponseEntity.status(404).body("Usuario con ID " + id + " no existe.");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.desactivarUsuario(id);
        return usuario != null
                ? ResponseEntity.ok(usuario)
                : ResponseEntity.status(404).body("Usuario no encontrado o ya está desactivado.");
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.activarUsuario(id);
        return usuario != null
                ? ResponseEntity.ok(usuario)
                : ResponseEntity.status(404).body("Usuario no encontrado o ya está activo.");
    }

    @GetMapping("/activos")
    public ResponseEntity<?> obtenerUsuariosActivos() {
        List<Usuario> activos = usuarioService.obtenerUsuariosActivos();
        return activos.isEmpty()
                ? ResponseEntity.status(204).body("No hay usuarios activos.")
                : ResponseEntity.ok(activos);
    }

    @PutMapping("/{id}/asignar-rol")
    public ResponseEntity<?> asignarRol(@PathVariable Long id, @RequestBody TipoUsuario rol) {
        try {
            usuarioService.asignarRol(id, rol); // Usa la instancia inyectada
            return ResponseEntity.ok("Rol asignado exitosamente al usuario con ID " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Error al asignar rol: " + e.getMessage());
        }
    }
}