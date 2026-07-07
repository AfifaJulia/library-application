package com.collabera.library_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		System.out.println("Starting Library Management Application...");
		SpringApplication.run(LibraryApplication.class, args);
	}

}
