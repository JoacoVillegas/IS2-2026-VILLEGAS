package com.futbolista;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FutbolistaApiApplication {

	private static final Logger log = LoggerFactory.getLogger(FutbolistaApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FutbolistaApiApplication.class, args);
		log.info("Carga completa, sigamos trabajando!!");
	}

}
