package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;

@Configuration
@ConditionalOnExpression("${localstack.enabled:true}")
@ConditionalOnProperty(prefix = "localstack", name = "sns.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(AmazonSNS.class)
@AutoConfigureAfter(name = BEAN_NAME_LOCALSTACK)
public class AmazonSNSConfiguration extends AbstractAmazonClient {

    public AmazonSNSConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Primary
    @Bean
    public AmazonSNS amazonSNSLocalStack() {
        AmazonSNSClientBuilder builder = AmazonSNSClientBuilder.standard();
        builder.withEndpointConfiguration(getEndpointConfiguration(SNS));
        return builder.build();
    }

    @Primary
    @Bean
    public AmazonSNSAsync amazonSNSAsyncLocalStack() {
        AmazonSNSAsyncClientBuilder builder = AmazonSNSAsyncClientBuilder.standard();
        builder.withEndpointConfiguration(getEndpointConfiguration(SNS));
        return builder.build();
    }
}
