package io.github.sivalabs.localstack.autoconfigure.configurator.awsv2;

import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.core.client.builder.SdkAsyncClientBuilder;
import software.amazon.awssdk.core.client.builder.SdkClientBuilder;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.utils.AttributeMap;

import java.net.URI;

@RequiredArgsConstructor
@SuppressWarnings("all")
public abstract class AbstractAmazonV2Client {
    protected final LocalStackContainer localStackContainer;

    public <T extends SdkClientBuilder> T wrapApiClientV2(T builder, LocalStackContainer.Service service) {
        return wrapApiClientV2(builder, localStackContainer.getEndpointOverride(service));
    }

    public <T extends SdkAsyncClientBuilder> T wrapApiAsyncClientV2(T builder, LocalStackContainer.Service service) {
        return wrapApiAsyncClientV2(builder, localStackContainer.getEndpointOverride(service));
    }

    private <T extends SdkClientBuilder> T wrapApiClientV2(T builder, URI endpointURL) {
        try {
            return (T) ((AwsClientBuilder)builder)
                .credentialsProvider(getCredentialsV2())
                .region(Region.of(localStackContainer.getRegion()))
                .endpointOverride(endpointURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends SdkAsyncClientBuilder> T wrapApiAsyncClientV2(T builder, URI endpointURL) {
        try {
            return (T) ((AwsClientBuilder)builder
                .httpClient(NettyNioAsyncHttpClient.builder().buildWithDefaults(
                    AttributeMap.builder().put(
                        SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES, java.lang.Boolean.TRUE).build())))
                .credentialsProvider(getCredentialsV2())
                .region(Region.of(localStackContainer.getRegion()))
                .endpointOverride(endpointURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AwsCredentialsProvider getCredentialsV2() {
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())
        );
    }
}
