package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DsSmainprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsSmainprojectApplication.class, args);
		
		
	}

}
