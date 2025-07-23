package org.academico.springcloud.msvc.usuario.services;

import org.academico.springcloud.msvc.usuario.models.entities.Usuario;
import org.academico.springcloud.msvc.usuario.models.enums.TipoUsuario;
import org.academico.springcloud.msvc.usuario.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodosPorIds(List<Long> ids) {
        return (List<Usuario>) usuarioRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    @Override
    @Transactional
    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public List<Usuario> listarUsuariosPorTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }
}