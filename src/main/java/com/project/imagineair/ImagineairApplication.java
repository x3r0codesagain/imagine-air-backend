package com.project.imagineair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication(scanBasePackages = "com.project.imagineair")
public class ImagineairApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagineairApplication.class, args);
	}

}
