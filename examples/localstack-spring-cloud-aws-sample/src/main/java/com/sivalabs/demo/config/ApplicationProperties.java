package com.sivalabs.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {
    private String queueName;
}
