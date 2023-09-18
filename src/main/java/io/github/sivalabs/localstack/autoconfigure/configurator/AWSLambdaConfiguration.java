package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.lambda.LambdaClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.LAMBDA;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.lambda.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(LambdaAsyncClient.class)
public class AWSLambdaConfiguration extends AbstractAmazonClient {
    public AWSLambdaConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public LambdaAsyncClient awsLambdaAsyncLocalStack() {
        return LambdaAsyncClient.builder()
                .endpointOverride(getEndpoint(LAMBDA))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public LambdaClient awsLambdaLocalStack() {
        return LambdaClient.builder()
                .endpointOverride(getEndpoint(LAMBDA))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
