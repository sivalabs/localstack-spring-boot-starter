package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

@Configuration
@ConditionalOnExpression("${localstack.enabled:true}")
@ConditionalOnProperty(prefix = "localstack", name = "dynamodb.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(AmazonDynamoDB.class)
@AutoConfigureAfter(name = BEAN_NAME_LOCALSTACK)
public class AmazonDynamoDBConfiguration extends AbstractAmazonClient {

    public AmazonDynamoDBConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonDynamoDB amazonDynamoDBLocalStack() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
        builder.withEndpointConfiguration(getEndpointConfiguration(DYNAMODB));
        return builder.build();
    }

    @Bean
    @Primary
    public AmazonDynamoDBAsync amazonDynamoDBAsyncLocalStack() {
        AmazonDynamoDBAsyncClientBuilder builder = AmazonDynamoDBAsyncClientBuilder.standard();
        builder.withEndpointConfiguration(getEndpointConfiguration(DYNAMODB));
        return builder.build();
    }
}
