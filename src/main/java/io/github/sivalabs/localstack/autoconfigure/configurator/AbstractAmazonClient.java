package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import java.net.URI;

@RequiredArgsConstructor
public abstract class AbstractAmazonClient {
    protected final LocalStackContainer localStackContainer;

//    protected EndpointConfiguration getEndpointConfiguration(Service service) {
//        return localStackContainer.getEndpointConfiguration(service);
//    }

    protected URI getEndpoint(Service service) { return localStackContainer.getEndpointOverride(service); }

    protected AwsCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey()));
    }
}
