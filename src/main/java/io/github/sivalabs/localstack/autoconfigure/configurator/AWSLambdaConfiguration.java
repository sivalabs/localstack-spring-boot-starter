package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.LAMBDA;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.lambda.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AWSLambdaAsync.class)
public class AWSLambdaConfiguration extends AbstractAmazonClient {
    public AWSLambdaConfiguration(LocalStackContainer localStack) {
        super(localStack);
    }

    @Bean
    @Primary
    public AWSLambdaAsync awsLambdaAsyncLocalStack() {
        return AWSLambdaAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(LAMBDA))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
