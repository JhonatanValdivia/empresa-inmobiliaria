package org.academico.springcloud.msvc.preventa.clients;

import org.academico.springcloud.msvc.preventa.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-preventa", url="localhost:8040/api/usuarios")
public interface UsuarioClientsRest
{
    @GetMapping("/{id}")
    Usuario detalleUsuario(@PathVariable("id") Long id);
}
