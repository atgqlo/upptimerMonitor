package com.example.uptimemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UptimemonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UptimemonitorApplication.class, args);
	}

}
