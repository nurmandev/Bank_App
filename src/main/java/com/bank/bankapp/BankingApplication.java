package com.bank.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Banking Application APIs",
		description = "Bankend Rest APIs for Banking application",
		version = "v1.0",
		contact = @Contact(
			name = "MUSTAPHA NURUDEEN",
			email = "adebayour66265@gmail.com",
			url = "https://github.com/Adebayour66265/Bank_App"
		),
		license = @License(
			name = "Banking Application",
			url = "https://github.com/Adebayour66265/Bank_App"
		)
	),
	externalDocs = @ExternalDocumentation(
		description = "Complete Backend APIs documentation",
		url = "https://github.com/Adebayour66265/Bank_App"
	)
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		System.out.println("Banking Application Server is Running");
	}

}
