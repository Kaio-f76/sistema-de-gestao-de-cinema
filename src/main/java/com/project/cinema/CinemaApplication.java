package com.project.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaApplication {

	public static void main(String[] args) {
		System.out.println("Backend rodando !");
		SpringApplication.run(CinemaApplication.class, args);
	}

}
