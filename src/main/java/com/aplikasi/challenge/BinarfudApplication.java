package com.aplikasi.challenge;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class BinarfudApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinarfudApplication.class, args);
	}

}
