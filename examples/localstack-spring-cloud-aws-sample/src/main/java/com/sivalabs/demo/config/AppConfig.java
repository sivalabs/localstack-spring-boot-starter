package com.sivalabs.demo.config;

import io.github.sivalabs.localstack.EnableLocalStack;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Configuration
    @Profile("aws")
    public static class AwsConfig {

    }

    @Configuration
    @Profile("!aws")
    @EnableLocalStack
    public static class LocalStackAwsConfig {

    }

}
