package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@Configuration
@ConditionalOnExpression("${localstack.enabled:true}")
@ConditionalOnProperty(prefix = "localstack", name = "sqs.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(AmazonSQS.class)
@AutoConfigureAfter(name = BEAN_NAME_LOCALSTACK)
public class AmazonSQSConfiguration extends AbstractAmazonClient {

    public AmazonSQSConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsyncLocalStack() {
        AmazonSQSAsyncClientBuilder builder = AmazonSQSAsyncClientBuilder.standard();
        builder.withEndpointConfiguration(getEndpointConfiguration(SQS));
        return builder.build();
    }

}
