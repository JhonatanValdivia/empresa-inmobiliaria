package org.academico.springcloud.msvc.preventa.repositories;

import org.academico.springcloud.msvc.preventa.models.entity.Preventa;
import org.springframework.data.repository.CrudRepository;

public interface PreventaRepository extends CrudRepository<Preventa, Long> {
}