package org.academico.springcloud.msvc.norma.services;

import org.academico.springcloud.msvc.norma.models.entity.Norma;
import org.academico.springcloud.msvc.norma.models.enums.TipoNorma;

import java.util.List;
import java.util.Optional;

public interface NormaService {
    List<Norma> listar();
    Optional<Norma> porId(Long id);
    Norma guardar(Norma norma);
    void eliminar(Long id);

    // Métodos específicos del dominio
    List<Norma> porTipo(TipoNorma tipo);
    List<Norma> porEstado(String estado);
}