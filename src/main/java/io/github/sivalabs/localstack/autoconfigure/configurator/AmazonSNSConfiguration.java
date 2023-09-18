package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.sns.SnsAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.sns.SnsClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.sns.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(SnsAsyncClient.class)
public class AmazonSNSConfiguration extends AbstractAmazonClient {

    public AmazonSNSConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Primary
    @Bean
    public SnsAsyncClient amazonSNSAsyncLocalStack() {
        return SnsAsyncClient.builder()
                .endpointOverride(getEndpoint(SNS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Primary
    @Bean
    public SnsClient amazonSNSLocalStack() {
        return SnsClient.builder()
                .endpointOverride(getEndpoint(SNS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
