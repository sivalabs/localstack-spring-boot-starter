package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1;

import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsync;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsyncClientBuilder;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SECRETSMANAGER;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.secretsmanager.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AWSSecretsManagerAsync.class)
public class AWSSecretsManagerConfiguration extends AbstractAmazonClient {
    public AWSSecretsManagerConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AWSSecretsManagerAsync awsSecretsManagerAsyncLocalStack() {
        return AWSSecretsManagerAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(SECRETSMANAGER))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
