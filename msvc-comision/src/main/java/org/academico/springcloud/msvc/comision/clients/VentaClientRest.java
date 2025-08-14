package org.academico.springcloud.msvc.comision.clients;

import org.academico.springcloud.msvc.comision.models.Venta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-venta", url = "localhost:8020/api/ventas")
public interface VentaClientRest {
    @GetMapping("/{id}")
    Venta detalle(@PathVariable Long id);
}