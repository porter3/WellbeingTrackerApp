package com.jakeporter.WellbeingTrackerAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableAutoConfiguration
@SpringBootApplication
public class WellbeingTrackerApiApplication extends SpringBootServletInitializer {
   
	public static void main(String[] args) {
		SpringApplication.run(WellbeingTrackerApiApplication.class, args);
	}

}
