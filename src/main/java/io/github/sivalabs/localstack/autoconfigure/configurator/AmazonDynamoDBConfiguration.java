package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.dynamodb.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass({DynamoDbAsyncClient.class, DynamoDbClient.class})
public class AmazonDynamoDBConfiguration extends AbstractAmazonClient {

    public AmazonDynamoDBConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public DynamoDbAsyncClient amazonDynamoDBAsyncLocalStack() {
        return DynamoDbAsyncClient.builder()
                .endpointOverride(getEndpoint(DYNAMODB))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public DynamoDbClient amazonDynamoDBLocalStack() {
        return DynamoDbClient.builder()
                .endpointOverride(getEndpoint(DYNAMODB))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
