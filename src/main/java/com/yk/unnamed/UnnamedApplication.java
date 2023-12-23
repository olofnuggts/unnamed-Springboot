package com.yk.unnamed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class UnnamedApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnnamedApplication.class, args);
	}

}
