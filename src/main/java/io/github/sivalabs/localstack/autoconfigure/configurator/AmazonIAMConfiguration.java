package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.services.iam.IamAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.iam.IamClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.IAM;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.iam.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(IamAsyncClient.class)
public class AmazonIAMConfiguration extends AbstractAmazonClient {

    public AmazonIAMConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public IamAsyncClient amazonIdentityManagementAsyncLocalStack() {
        return IamAsyncClient.builder()
                .endpointOverride(getEndpoint(IAM))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public IamClient amazonIdentityManagementLocalStack() {
        return IamClient.builder()
                .endpointOverride(getEndpoint(IAM))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

}
