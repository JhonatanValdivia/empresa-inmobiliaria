package org.academico.springcloud.msvc.usuario.controllers;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.academico.springcloud.msvc.usuario.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalleUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOp = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOp.isPresent()){
            return ResponseEntity.ok(usuarioOp.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario){
          Usuario UsuarioDB=usuarioService.guardarUsuario(usuario);
          return  ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Usuario usuario){
        Optional<Usuario> usuarioOP= usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOP.isPresent()){
            Usuario usuarioDB=usuarioOP.get();
            usuarioDB.setNombreCompleto(usuario.getNombreCompleto());
            usuarioDB.setTipoUsuario(usuario.getTipoUsuario());
            usuarioDB.setCorreoElectronico(usuario.getCorreoElectronico());
            usuarioDB.setDireccion(usuario.getDireccion());
            usuarioDB.setTelefono(usuario.getTelefono());

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardarUsuario(usuarioDB));
        }
        return  ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        if (usuarioService.existePorId(id)){
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }
            return  ResponseEntity.notFound().build();

    }
    @GetMapping("/contar")
    public  ResponseEntity<?> contarUsuarios(){
        return ResponseEntity.ok(usuarioService.contarUsuarios());

    }}