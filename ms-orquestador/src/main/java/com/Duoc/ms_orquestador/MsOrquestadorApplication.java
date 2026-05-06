package com.Duoc.ms_orquestador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MsOrquestadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrquestadorApplication.class, args);
	}

}
