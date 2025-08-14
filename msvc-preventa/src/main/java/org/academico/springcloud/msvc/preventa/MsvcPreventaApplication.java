package org.academico.springcloud.msvc.preventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPreventaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPreventaApplication.class, args);
	}

}