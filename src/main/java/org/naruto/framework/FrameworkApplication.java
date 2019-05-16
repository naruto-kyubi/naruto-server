package org.naruto.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FrameworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameworkApplication.class, args);
	}
}
