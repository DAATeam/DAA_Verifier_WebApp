package com.daa.verifier;

//import com.daa.verifier.Models.VerifierSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.context.annotation.Bean;
//import com.daa.verifier.Repository.VerifierSignatureRepository;

@SpringBootApplication
@EnableJpaRepositories
public class DaaVerifierWebAppApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DaaVerifierWebAppApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DaaVerifierWebAppApplication.class, args);
	}
}
