package com.NexusHealth.ms_agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsAgendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAgendaApplication.class, args);
	}

}
