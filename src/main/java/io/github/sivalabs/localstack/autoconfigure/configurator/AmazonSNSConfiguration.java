package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.sns.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonSNSAsync.class)
public class AmazonSNSConfiguration extends AbstractAmazonClient {

    public AmazonSNSConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Primary
    @Bean
    public AmazonSNSAsync amazonSNSAsyncLocalStack() {
        return AmazonSNSAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(SNS))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
