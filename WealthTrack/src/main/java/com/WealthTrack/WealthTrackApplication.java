package com.WealthTrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class WealthTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(WealthTrackApplication.class, args);
		System.out.println("Hello");
	}

}
