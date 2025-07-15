package org.academico.springcloud.msvc.campania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcCapaniaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsvcCapaniaApplication.class, args);
	}
}
