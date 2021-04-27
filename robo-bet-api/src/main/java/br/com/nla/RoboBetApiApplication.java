package br.com.nla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("br.com.nla")
@EnableJpaRepositories
public class RoboBetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoboBetApiApplication.class, args);
	}

}
