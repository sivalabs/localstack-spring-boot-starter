package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsAsyncClientBuilder;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB_STREAMS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.dynamodbstreams.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonDynamoDBStreamsAsync.class)
public class AmazonDynamoDBStreamsConfiguration extends AbstractAmazonClient {

    public AmazonDynamoDBStreamsConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonDynamoDBStreamsAsync amazonDynamoDBStreamsAsyncLocalStack() {
        return AmazonDynamoDBStreamsAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(DYNAMODB_STREAMS))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
