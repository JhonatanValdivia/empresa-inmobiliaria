package org.academico.springcloud.msvc.usuario.services;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsuarioService {
    List<Usuario> obtenerTodosLosUsuarios();
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Usuario crearUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    boolean eliminarUsuario(Long id);
    Usuario desactivarUsuario(Long id);
    Usuario activarUsuario(Long id);
    List<Usuario> obtenerUsuariosActivos();
    void asignarRol(Long id, TipoUsuario rol);}