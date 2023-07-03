package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

@RequiredArgsConstructor
public abstract class AbstractAmazonClient {
    protected final LocalStackContainer localStack;

    protected EndpointConfiguration getEndpointConfiguration(Service service) {
        return new EndpointConfiguration(
                    localStack.getEndpointOverride(service).toString(),
                    localStack.getRegion()
                );
    }

    protected AWSCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(localStack.getAccessKey(), localStack.getSecretKey())
        );
    }
}
