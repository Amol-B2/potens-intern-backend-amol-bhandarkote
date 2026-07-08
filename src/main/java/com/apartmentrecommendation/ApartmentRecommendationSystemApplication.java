package com.apartmentrecommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ApartmentRecommendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentRecommendationSystemApplication.class, args);
	}

}
