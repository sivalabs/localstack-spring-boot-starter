package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.s3.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonS3.class)
public class AmazonS3Configuration extends AbstractAmazonClient {

    public AmazonS3Configuration(LocalStackContainer localStack) {
        super(localStack);
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3LocalStack() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(S3))
                .withCredentials(getCredentialsProvider())
                .enablePathStyleAccess()
                .build();
    }

}
