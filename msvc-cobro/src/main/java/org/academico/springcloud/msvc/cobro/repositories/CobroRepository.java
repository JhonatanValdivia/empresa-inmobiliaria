package org.academico.springcloud.msvc.cobro.repositories;

import org.academico.springcloud.msvc.cobro.models.entities.Cobro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobroRepository extends CrudRepository<Cobro, Long> {
}
