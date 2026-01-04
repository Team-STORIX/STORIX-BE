package com.storix.storix_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class StorixApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorixApiApplication.class, args);
	}

}
