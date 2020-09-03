
package com.sivalabs.demo;

import io.github.sivalabs.localstack.EnableLocalStack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLocalStack
public class LocalStackStarterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalStackStarterDemoApplication.class, args);
	}

}
