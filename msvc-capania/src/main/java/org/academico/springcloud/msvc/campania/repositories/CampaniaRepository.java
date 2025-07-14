package org.academico.springcloud.msvc.campania.repositories;

import org.academico.springcloud.msvc.campania.models.entities.Campania;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaniaRepository extends CrudRepository<Campania, Long> {
}
