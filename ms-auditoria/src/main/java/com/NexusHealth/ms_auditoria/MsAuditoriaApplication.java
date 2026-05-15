package com.NexusHealth.ms_auditoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsAuditoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuditoriaApplication.class, args);
	}

}
