package com.github.theprogmatheus.zonadelivery.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZonaDeliveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZonaDeliveryServerApplication.class, args);
	}
}
