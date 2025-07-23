package org.academico.springcloud.msvc.usuario.services;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsuarioService {
    List<Usuario> listar();

    List<Usuario> listarTodosPorIds(List<Long> ids);

    Optional<Usuario> obtenerUsuarioPorId(Long id);

    Usuario guardarUsuario(Usuario usuario);

    void eliminarUsuario(Long id);

    long contarUsuarios();

    boolean existePorId(Long id);

    List<Usuario> listarUsuariosPorTipo(TipoUsuario tipoUsuario);
}