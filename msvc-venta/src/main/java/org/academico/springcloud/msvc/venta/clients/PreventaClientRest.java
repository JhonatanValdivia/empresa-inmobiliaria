package org.academico.springcloud.msvc.venta.clients;

import org.academico.springcloud.msvc.venta.models.dtos.PreventaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-preventa", url = "http://localhost:8080/api/preventas")
public interface PreventaClientRest {

    // Método para obtener los detalles de una Preventa por su ID (para composición de datos)
    @GetMapping("/{id}")
    PreventaDTO getPreventaById(@PathVariable Long id);


}