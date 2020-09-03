package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
@ConditionalOnExpression("${localstack.enabled:true}")
@ConditionalOnProperty(prefix = "localstack", name = "s3.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(AmazonS3.class)
@AutoConfigureAfter(name = BEAN_NAME_LOCALSTACK)
public class AmazonS3Configuration extends AbstractAmazonClient {

    public AmazonS3Configuration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3LocalStack() {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.enablePathStyleAccess();
        builder.withEndpointConfiguration(getEndpointConfiguration(S3));
        return builder.build();
    }

}
