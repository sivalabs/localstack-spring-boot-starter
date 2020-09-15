package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.initializers.AmazonS3Initializer;
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
    private final LocalStackProperties localStackProperties;

    public AmazonS3Configuration(LocalStackProperties localStackProperties, LocalStackContainer localStackContainer) {
        super(localStackContainer);
        this.localStackProperties = localStackProperties;
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3LocalStack() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(getEndpointConfiguration(S3))
            .withCredentials(getCredentialsProvider())
            .enablePathStyleAccess()
            .build();
        this.initS3(amazonS3);
        return amazonS3;
    }

    private void initS3(AmazonS3 amazonS3) {
        if(localStackProperties.getInitializer().getVersion() == LocalStackProperties.AwsSdkVersion.V1) {
            AmazonS3Initializer s3Initializer = new AmazonS3Initializer(localStackProperties, amazonS3);
            s3Initializer.init();
        }
    }

}
