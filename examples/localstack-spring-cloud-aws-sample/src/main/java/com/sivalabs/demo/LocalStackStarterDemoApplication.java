
package com.sivalabs.demo;

import com.sivalabs.demo.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableScheduling
public class LocalStackStarterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalStackStarterDemoApplication.class, args);
	}

}
