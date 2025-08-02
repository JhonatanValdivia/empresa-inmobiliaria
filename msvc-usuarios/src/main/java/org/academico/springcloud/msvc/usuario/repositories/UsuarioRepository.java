package org.academico.springcloud.msvc.usuario.repositories;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    List<Usuario> findByEstado(String estado);
}