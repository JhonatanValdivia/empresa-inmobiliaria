package org.academico.springcloud.msvc.norma.repositories;

import org.academico.springcloud.msvc.norma.models.entity.Norma;
import org.academico.springcloud.msvc.norma.models.enums.TipoNorma;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NormaRepository extends CrudRepository<Norma, Long> {
    List<Norma> findByTipo(TipoNorma tipo);
    List<Norma> findByEstadoNorma(String estadoNorma);
}