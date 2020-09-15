package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.dynamodb.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonDynamoDBAsync.class)
public class AmazonDynamoDBConfiguration extends AbstractAmazonClient {

    public AmazonDynamoDBConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonDynamoDBAsync amazonDynamoDBAsyncLocalStack() {
        return AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(DYNAMODB))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
