package com.jakeporter.WellbeingTrackerAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class WellbeingTrackerApiApplication {
    
    // this should appear in the new AJAX branch

	public static void main(String[] args) {
		SpringApplication.run(WellbeingTrackerApiApplication.class, args);
	}

}
