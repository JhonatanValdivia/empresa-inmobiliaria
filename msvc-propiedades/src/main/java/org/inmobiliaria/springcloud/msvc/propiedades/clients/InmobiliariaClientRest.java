package org.inmobiliaria.springcloud.msvc.propiedades.clients;

import org.inmobiliaria.springcloud.msvc.propiedades.models.Norma;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-norma",url="localhost:8070/api/norma")
public interface InmobiliariaClientRest {

    @GetMapping("/{Id}")
    Norma detalle(@PathVariable Long id);
    @PostMapping
    Norma crear(@RequestBody Norma norma);



}
