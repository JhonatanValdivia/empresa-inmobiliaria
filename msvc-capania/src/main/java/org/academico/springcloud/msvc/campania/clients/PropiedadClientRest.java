package org.academico.springcloud.msvc.campania.clients;

import org.academico.springcloud.msvc.campania.models.Propiedad;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-propiedades", url = "localhost:8010/api/propiedades") // Ajusta la URL según tu configuración
public interface PropiedadClientRest {
    @GetMapping("/{id}")
    Propiedad detalle(@PathVariable Long id);
}