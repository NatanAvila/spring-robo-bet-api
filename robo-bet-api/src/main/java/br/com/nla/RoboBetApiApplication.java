package br.com.nla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com.nla")
public class RoboBetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoboBetApiApplication.class, args);
	}

}
