package com.redhat.jdgspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JDGSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JDGSpringBootApplication.class, args);
		//init caches, create remotely
	}
	

}
