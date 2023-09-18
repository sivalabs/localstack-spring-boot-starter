package com.sivalabs.demo;

import io.github.sivalabs.localstack.LocalStackProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient;
import software.amazon.awssdk.services.iam.IamAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringbootLocalStackDemoApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldAutoConfigureLocalStackServices() {
        assertThat(context.getBean(LocalStackProperties.class)).isNotNull();
        assertThat(context.getBean(S3Client.class)).isNotNull();
        assertThat(context.getBean(SqsAsyncClient.class)).isNotNull();
        assertThat(context.getBean(SnsAsyncClient.class)).isNotNull();
        assertThat(context.getBean(DynamoDbAsyncClient.class)).isNotNull();
        assertThat(context.getBean(DynamoDbStreamsAsyncClient.class)).isNotNull();
        assertThat(context.getBean(CloudWatchAsyncClient.class)).isNotNull();
        assertThat(context.getBean(LambdaAsyncClient.class)).isNotNull();
        assertThat(context.getBean(SecretsManagerAsyncClient.class)).isNotNull();
        assertThat(context.getBean(KinesisAsyncClient.class)).isNotNull();
        assertThat(context.getBean(IamAsyncClient.class)).isNotNull();
    }
}
