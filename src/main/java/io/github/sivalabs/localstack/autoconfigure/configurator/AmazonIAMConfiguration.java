package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsync;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsyncClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.IAM;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.iam.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonIdentityManagementAsync.class)
public class AmazonIAMConfiguration extends AbstractAmazonClient {

    public AmazonIAMConfiguration(LocalStackContainer localStack) {
        super(localStack);
    }

    @Bean
    @Primary
    public AmazonIdentityManagementAsync amazonIdentityManagementAsyncLocalStack() {
        return AmazonIdentityManagementAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration(IAM))
                .withCredentials(getCredentialsProvider())
                .build();
    }

}
