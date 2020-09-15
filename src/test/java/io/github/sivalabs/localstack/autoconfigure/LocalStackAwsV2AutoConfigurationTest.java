package io.github.sivalabs.localstack.autoconfigure;

import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.AmazonS3V2Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.AmazonSQSV2Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.assertj.core.api.Assertions.assertThat;

class LocalStackAwsV2AutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            LocalStackAutoConfiguration.class,
            AmazonS3V2Configuration.class,
            AmazonSQSV2Configuration.class
        ));

    @Test
    void shouldAutoConfigureAWSServiceClientsBasedOnDefaultConfiguration() {
        this.contextRunner
            .withPropertyValues(
                "localstack.services=S3,SQS",
                "localstack.s3.buckets=bucket1,bucket2",
                "localstack.sqs.queues=queue1,queue2"
            )
            .run((context) -> {
                assertThat(context).hasSingleBean(LocalStackProperties.class);
                LocalStackProperties properties = context.getBean(LocalStackProperties.class);
                assertThat(properties.isEnabled()).isTrue();

                assertThat(context).hasSingleBean(S3Client.class);
                assertThat(context).hasSingleBean(S3AsyncClient.class);
                assertThat(context).hasSingleBean(SqsClient.class);
                assertThat(context).hasSingleBean(SqsAsyncClient.class);
            });
    }
}
