package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.CLOUDWATCH;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.cloudwatch.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass({CloudWatchAsyncClient.class,  CloudWatchClient.class})
public class AmazonCloudWatchConfiguration extends AbstractAmazonClient {
    public AmazonCloudWatchConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public CloudWatchAsyncClient amazonCloudWatchAsyncLocalStack() {
        return CloudWatchAsyncClient.builder()
                .endpointOverride(getEndpoint(CLOUDWATCH))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public CloudWatchClient amazonCloudWatchLocalStack() {
        return CloudWatchClient.builder()
                .endpointOverride(getEndpoint(CLOUDWATCH))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
