package io.github.sivalabs.localstack.autoconfigure.configurator;

import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.time.Duration;
import java.util.concurrent.Executors;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.KINESIS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.kinesis.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(KinesisAsyncClient.class)
public class AmazonKinesisConfiguration extends AbstractAmazonClient {

    public AmazonKinesisConfiguration(LocalStackContainer localStackContainer) {
        super(localStackContainer);
    }

    @Bean
    @Primary
    public KinesisAsyncClient amazonKinesisAsyncLocalStack() {
        // Based on my personal experience, to avoid timeouts in tests we need to override the
        // default timeouts and retry policy.  YMMV
        ClientOverrideConfiguration overrideConfiguration =
                ClientOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofSeconds(120))
                        .apiCallAttemptTimeout(Duration.ofSeconds(100))
                        .retryPolicy(RetryPolicy.builder()
                                .numRetries(5)
                                .retryCondition(RetryCondition.defaultRetryCondition())
                                .build()
                        )
                        .build();
        return KinesisAsyncClient.builder()
                .endpointOverride(getEndpoint(KINESIS))
                .overrideConfiguration(overrideConfiguration)
                .credentialsProvider(getCredentialsProvider())
                .asyncConfiguration(
                        b -> b.advancedOption(SdkAdvancedAsyncClientOption
                                        .FUTURE_COMPLETION_EXECUTOR,
                                Executors.newFixedThreadPool(2)
                        )
                )
                .build();
    }

    @Bean
    @Primary
    public KinesisClient amazonKinesisLocalStack() {
        return KinesisClient.builder()
                .endpointOverride(getEndpoint(KINESIS))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

}
