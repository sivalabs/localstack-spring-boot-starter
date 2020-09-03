package io.github.sivalabs.localstack.autoconfigure.configurator;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

@RequiredArgsConstructor
public abstract class AbstractAmazonClient {
    protected final LocalStackContainer localStackContainer;

    protected EndpointConfiguration getEndpointConfiguration(Service service) {
        return localStackContainer.getEndpointConfiguration(service);
    }
}
