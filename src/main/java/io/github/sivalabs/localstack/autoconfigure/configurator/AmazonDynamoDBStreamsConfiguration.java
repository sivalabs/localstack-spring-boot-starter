package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB_STREAMS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.dynamodbstreams.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass({DynamoDbStreamsAsyncClient.class, DynamoDbStreamsClient.class})
public class AmazonDynamoDBStreamsConfiguration extends AbstractAmazonClient {

    public AmazonDynamoDBStreamsConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public DynamoDbStreamsAsyncClient amazonDynamoDBStreamsAsyncLocalStack() {
        return DynamoDbStreamsAsyncClient.builder()
                .endpointOverride(getEndpoint(DYNAMODB_STREAMS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
    @Bean
    @Primary
    public DynamoDbStreamsClient amazonDynamoDBStreamsLocalStack() {
        return DynamoDbStreamsClient.builder()
                .endpointOverride(getEndpoint(DYNAMODB_STREAMS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
