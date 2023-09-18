package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.KINESIS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.kinesis.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(KinesisAsyncClient.class)
public class AmazonKinesisConfiguration extends AbstractAmazonClient {

    public AmazonKinesisConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public KinesisAsyncClient amazonKinesisAsyncLocalStack() {
        return KinesisAsyncClient.builder()
                .endpointOverride(getEndpoint(KINESIS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public KinesisClient amazonKinesisLocalStack() {
        return KinesisClient.builder()
                .endpointOverride(getEndpoint(KINESIS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

}
