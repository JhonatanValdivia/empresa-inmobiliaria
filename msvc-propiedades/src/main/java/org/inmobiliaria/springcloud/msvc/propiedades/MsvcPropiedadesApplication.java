package org.inmobiliaria.springcloud.msvc.propiedades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPropiedadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPropiedadesApplication.class, args);
	}

}
