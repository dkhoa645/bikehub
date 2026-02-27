package com.group3.bikehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BikehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikehubApplication.class, args);
	}

}
