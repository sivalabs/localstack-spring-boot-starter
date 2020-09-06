package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.CLOUDWATCH;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.cloudwatch.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonCloudWatchAsync.class)
public class AmazonCloudWatchConfiguration extends AbstractAmazonClient {
    public AmazonCloudWatchConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonCloudWatchAsync amazonCloudWatchAsyncLocalStack() {
        return AmazonCloudWatchAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(CLOUDWATCH))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
