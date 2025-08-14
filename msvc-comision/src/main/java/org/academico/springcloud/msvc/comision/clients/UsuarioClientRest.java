package org.academico.springcloud.msvc.comision.clients;

import org.academico.springcloud.msvc.comision.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "localhost:8040/api/usuarios")
public interface UsuarioClientRest {
    @GetMapping("/{id}")
    Usuario detalleUsuario(@PathVariable Long id);
}