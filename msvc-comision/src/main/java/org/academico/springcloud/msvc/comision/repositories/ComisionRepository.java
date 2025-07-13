package org.academico.springcloud.msvc.comision.repositories;

import org.academico.springcloud.msvc.comision.models.entities.Comision;
import org.academico.springcloud.msvc.comision.models.enums.EstadoComision;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComisionRepository extends CrudRepository<Comision, Long> {
    //consultas
    List<Comision> findByVentaId(Long ventaId);

    List<Comision> findByEstadoComisionIn(List<EstadoComision> estados);
}
