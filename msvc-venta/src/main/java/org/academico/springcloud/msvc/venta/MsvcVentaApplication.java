package org.academico.springcloud.msvc.venta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcVentaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsvcVentaApplication.class, args);
	}
}
