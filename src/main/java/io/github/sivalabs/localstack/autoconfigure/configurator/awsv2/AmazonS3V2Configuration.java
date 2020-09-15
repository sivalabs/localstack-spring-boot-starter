package io.github.sivalabs.localstack.autoconfigure.configurator.awsv2;

import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.initializers.S3ClientInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.s3.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(S3AsyncClient.class)
public class AmazonS3V2Configuration extends AbstractAmazonV2Client {
    private final LocalStackProperties localStackProperties;

    public AmazonS3V2Configuration(LocalStackProperties localStackProperties, LocalStackContainer localStackContainer) {
        super(localStackContainer);
        this.localStackProperties = localStackProperties;
    }

    @Bean
    @Primary
    public S3Client s3ClientLocalStack() {
        S3Client s3Client = wrapApiClientV2(S3Client.builder(), S3).build();
        this.initS3(s3Client);
        return s3Client;
    }

    @Bean
    @Primary
    public S3AsyncClient s3AsyncClientLocalStack() {
        return wrapApiAsyncClientV2(S3AsyncClient.builder(), S3).build();
    }

    private void initS3(S3Client s3Client) {
        if (localStackProperties.getInitializer().getVersion() == LocalStackProperties.AwsSdkVersion.V2) {
            S3ClientInitializer s3Initializer = new S3ClientInitializer(localStackProperties, s3Client);
            s3Initializer.init();
        }
    }
}
