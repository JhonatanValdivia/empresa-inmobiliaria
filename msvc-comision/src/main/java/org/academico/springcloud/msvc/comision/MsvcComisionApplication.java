package org.academico.springcloud.msvc.comision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients //aperturar el microservicio a un cliente
@SpringBootApplication
public class MsvcComisionApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsvcComisionApplication.class, args);
	}
}