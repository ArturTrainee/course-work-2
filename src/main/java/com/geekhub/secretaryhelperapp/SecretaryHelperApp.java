package com.geekhub.secretaryhelperapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class })
public class SecretaryHelperApp {

	public static void main(String[] args) {
		SpringApplication.run(SecretaryHelperApp.class, args);
	}

}
