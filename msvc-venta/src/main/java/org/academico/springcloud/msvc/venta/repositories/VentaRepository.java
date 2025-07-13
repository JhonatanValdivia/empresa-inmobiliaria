package org.academico.springcloud.msvc.venta.repositories;

import org.academico.springcloud.msvc.venta.models.entities.Venta;
import org.springframework.data.repository.CrudRepository;

public interface VentaRepository extends CrudRepository<Venta,Long> {
}
