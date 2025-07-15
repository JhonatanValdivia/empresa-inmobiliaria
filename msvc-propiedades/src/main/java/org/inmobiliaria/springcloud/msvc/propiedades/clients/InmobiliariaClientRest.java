package org.inmobiliaria.springcloud.msvc.propiedades.clients;

import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-norma", url="http://localhost:8070")
public interface InmobiliariaClientRest {

    @GetMapping("/api/norma/{id}")
    Norma detalle(@PathVariable("id") Long id);

    @PostMapping("/api/norma")
    Norma crear(@RequestBody Norma norma);
}
