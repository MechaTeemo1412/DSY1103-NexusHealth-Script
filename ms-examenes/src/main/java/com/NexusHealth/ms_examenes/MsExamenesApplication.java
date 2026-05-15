package com.NexusHealth.ms_examenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsExamenesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsExamenesApplication.class, args);
	}

}
