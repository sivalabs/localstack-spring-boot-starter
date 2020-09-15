package com.sivalabs.demo.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.github.sivalabs.localstack.EnableLocalStack;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        @Bean
        @Primary
        public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
            SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
            factory.setAmazonSqs(amazonSQSAsync);
            factory.setWaitTimeOut(1); // For quicker shutdown of listener during tests
            return factory;
        }
    }

}
