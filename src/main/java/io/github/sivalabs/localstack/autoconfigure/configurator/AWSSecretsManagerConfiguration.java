package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.secretsmanager.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(SecretsManagerAsyncClient.class)
public class AWSSecretsManagerConfiguration extends AbstractAmazonClient {
    public AWSSecretsManagerConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public SecretsManagerAsyncClient awsSecretsManagerAsyncLocalStack() {
        return SecretsManagerAsyncClient.builder()
                .endpointOverride(getEndpoint(SECRETSMANAGER))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public SecretsManagerClient awsSecretsManagerLocalStack() {
        return SecretsManagerClient.builder()
                .endpointOverride(getEndpoint(SECRETSMANAGER))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
