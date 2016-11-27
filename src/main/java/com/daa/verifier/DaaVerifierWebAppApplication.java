package com.daa.verifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class DaaVerifierWebAppApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DaaVerifierWebAppApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DaaVerifierWebAppApplication.class, args);
	}
}
