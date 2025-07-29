package org.academico.springcloud.msvc.preventa.clients;

import org.academico.springcloud.msvc.preventa.models.PropiedadInmobiliaria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-propiedades", url = "localhost:8010/api/propiedades")
public interface PropiedadClientRest {
    @GetMapping("/{id}")
    PropiedadInmobiliaria detallePropiedad(@PathVariable("id") Long id);
}