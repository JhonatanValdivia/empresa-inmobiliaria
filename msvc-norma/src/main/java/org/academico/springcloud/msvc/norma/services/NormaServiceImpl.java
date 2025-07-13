package org.academico.springcloud.msvc.norma.services;

import org.academico.springcloud.msvc.norma.models.entity.Norma;
import org.academico.springcloud.msvc.norma.models.enums.TipoNorma;
import org.academico.springcloud.msvc.norma.repositories.NormaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NormaServiceImpl implements NormaService {

    @Autowired
    private NormaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Norma> listar() {
        return (List<Norma>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Norma> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Norma guardar(Norma norma) {
        return repository.save(norma);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Norma> porTipo(TipoNorma tipo) {
        return repository.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Norma> porEstado(String estado) {
        return repository.findByEstadoNorma(estado);
    }
}