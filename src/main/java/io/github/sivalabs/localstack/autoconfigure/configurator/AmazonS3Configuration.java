package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.s3.S3Configuration;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.s3.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass({S3Client.class, S3AsyncClient.class})
public class AmazonS3Configuration extends AbstractAmazonClient {

    public AmazonS3Configuration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public S3Client amazonS3LocalStack() {
        System.out.println("Tring to create S3Client");
        S3Configuration config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();
        return S3Client.builder()
                .serviceConfiguration(config)
                .endpointOverride(getEndpoint(S3))
                .credentialsProvider(getCredentialsProvider())
                .region(Region.of(localStackContainer.getRegion()))
                .build();
    }

    @Bean
    @Primary
    public S3AsyncClient amazonS3AsyncLocalStack() {
        S3Configuration config = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();
        return S3AsyncClient.builder()
                .serviceConfiguration(config)
                .endpointOverride(getEndpoint(S3))
                .credentialsProvider(getCredentialsProvider())
                .region(Region.of(localStackContainer.getRegion()))
                .build();
    }

}
