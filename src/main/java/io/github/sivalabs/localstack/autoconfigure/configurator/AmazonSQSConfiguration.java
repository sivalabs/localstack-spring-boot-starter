package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.sqs.SqsClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.sqs.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass({SqsAsyncClient.class, SqsClient.class})
public class AmazonSQSConfiguration extends AbstractAmazonClient {

    public AmazonSQSConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public SqsAsyncClient amazonSQSAsyncLocalStack() {
        return SqsAsyncClient.builder()
                .endpointOverride(getEndpoint(SQS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public SqsClient amazonSQSLocalStack() {
        return SqsClient.builder()
                .endpointOverride(getEndpoint(SQS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

}
