package org.academico.springcloud.msvc.venta.clients;


import org.academico.springcloud.msvc.venta.models.Preventa;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-preventa", url = "http://localhost:8080/api/preventas")
public interface PreventaClientRest {

    @GetMapping("/{id}")
    Preventa getPreventaById(@PathVariable Long id);
}
