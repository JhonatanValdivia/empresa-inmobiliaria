package org.academico.springcloud.msvc.usuario.repositories;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
}
