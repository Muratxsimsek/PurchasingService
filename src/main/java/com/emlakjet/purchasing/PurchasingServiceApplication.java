package com.emlakjet.purchasing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class PurchasingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchasingServiceApplication.class, args);
	}

}
