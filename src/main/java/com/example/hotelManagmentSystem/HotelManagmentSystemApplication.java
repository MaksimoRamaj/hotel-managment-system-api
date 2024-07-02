package com.example.hotelManagmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@SpringBootApplication
public class HotelManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagmentSystemApplication.class, args);
	}

}
