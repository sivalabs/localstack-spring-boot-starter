package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.KINESIS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.kinesis.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonKinesisAsync.class)
public class AmazonKinesisConfiguration extends AbstractAmazonClient {

    public AmazonKinesisConfiguration(LocalStackContainer localStack) {
        super(localStack);
    }

    @Bean
    @Primary
    public AmazonKinesisAsync amazonKinesisAsyncLocalStack() {
        return AmazonKinesisAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(KINESIS))
                .withCredentials(getCredentialsProvider())
                .build();
    }

}
