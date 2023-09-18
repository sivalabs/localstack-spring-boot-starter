package io.github.sivalabs.localstack.autoconfigure;


import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsClient;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient;
import software.amazon.awssdk.services.iam.IamAsyncClient;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;

import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.AWSLambdaConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AWSSecretsManagerConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonCloudWatchConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonDynamoDBConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonIAMConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonKinesisConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSNSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSQSConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalStackAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    LocalStackAutoConfiguration.class,
                    AmazonS3Configuration.class,
                    AmazonSQSConfiguration.class,
                    AmazonSNSConfiguration.class,
                    AmazonDynamoDBConfiguration.class,
                    AmazonCloudWatchConfiguration.class,
                    AmazonIAMConfiguration.class,
                    AmazonKinesisConfiguration.class,
                    AWSLambdaConfiguration.class,
                    AWSSecretsManagerConfiguration.class
                    ));

    @Test
    public void shouldAutoConfigureAWSServiceClientsBasedOnDefaultConfiguration() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.services=SQS,S3,SNS,DYNAMODB,DYNAMODBSTREAMS,KINESIS,IAM,LAMBDA,CLOUDWATCH,SECRETSMANAGER"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    LocalStackProperties properties = context.getBean(LocalStackProperties.class);
                    assertThat(properties.isEnabled()).isTrue();

                    assertThat(context).hasSingleBean(S3Client.class);
                    assertThat(context).hasSingleBean(S3AsyncClient.class);
                    assertThat(context).hasSingleBean(SqsAsyncClient.class);
                    assertThat(context).hasSingleBean(SqsClient.class);
                    assertThat(context).hasSingleBean(SnsAsyncClient.class);
                    assertThat(context).hasSingleBean(SnsClient.class);
                    assertThat(context).hasSingleBean(DynamoDbAsyncClient.class);
                    assertThat(context).hasSingleBean(DynamoDbClient.class);
                    assertThat(context).hasSingleBean(DynamoDbStreamsAsyncClient.class);
                    assertThat(context).hasSingleBean(DynamoDbStreamsClient.class);
                    assertThat(context).hasSingleBean(CloudWatchAsyncClient.class);
                    assertThat(context).hasSingleBean(CloudWatchClient.class);
                    assertThat(context).hasSingleBean(LambdaAsyncClient.class);
                    assertThat(context).hasSingleBean(LambdaClient.class);
                    assertThat(context).hasSingleBean(SecretsManagerAsyncClient.class);
                    assertThat(context).hasSingleBean(SecretsManagerClient.class);
                    assertThat(context).hasSingleBean(KinesisAsyncClient.class);
                    assertThat(context).hasSingleBean(KinesisClient.class);
                    assertThat(context).hasSingleBean(IamAsyncClient.class);
                    assertThat(context).hasSingleBean(IamClient.class);
                });
    }

    @Test
    public void shouldAutoConfigureAllEnabledAWSServiceClients() {
        this.contextRunner
                .withPropertyValues(
                    "localstack.services=SQS,S3,SNS,DYNAMODB,DYNAMODBSTREAMS,KINESIS,IAM,LAMBDA,CLOUDWATCH,SECRETSMANAGER",
                    "localstack.s3.enabled=true",
                    "localstack.sqs.enabled=true",
                    "localstack.sns.enabled=true",
                    "localstack.dynamodb.enabled=true",
                    "localstack.dynamodbstreams.enabled=true",
                    "localstack.kinesis.enabled=true",
                    "localstack.iam.enabled=true",
                    "localstack.lambda.enabled=true",
                    "localstack.cloudwatch.enabled=true",
                    "localstack.secretsmanager.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    LocalStackProperties properties = context.getBean(LocalStackProperties.class);
                    assertThat(properties.isEnabled()).isTrue();

                    assertThat(context).hasSingleBean(S3Client.class);
                    assertThat(context).hasSingleBean(SqsAsyncClient.class);
                    assertThat(context).hasSingleBean(SnsAsyncClient.class);
                    assertThat(context).hasSingleBean(DynamoDbAsyncClient.class);
                    assertThat(context).hasSingleBean(DynamoDbStreamsAsyncClient.class);
                    assertThat(context).hasSingleBean(CloudWatchAsyncClient.class);
                    assertThat(context).hasSingleBean(LambdaAsyncClient.class);
                    assertThat(context).hasSingleBean(SecretsManagerAsyncClient.class);
                    assertThat(context).hasSingleBean(KinesisAsyncClient.class);
                    assertThat(context).hasSingleBean(IamAsyncClient.class);
                });
    }

    @Test
    public void shouldAutoConfigureOnlyEnabledAWSServiceClients() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.services=SQS,S3",
                        "localstack.s3.enabled=true",
                        "localstack.sqs.enabled=true",
                        "localstack.sns.enabled=true",
                        "localstack.dynamodb.enabled=false",
                        "localstack.dynamodbstreams.enabled=false",
                        "localstack.kinesis.enabled=false",
                        "localstack.iam.enabled=false",
                        "localstack.lambda.enabled=false",
                        "localstack.cloudwatch.enabled=false",
                        "localstack.secretsmanager.enabled=false"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    assertThat(context).hasSingleBean(S3Client.class);
                    assertThat(context).hasSingleBean(SqsAsyncClient.class);
                    assertThat(context).hasSingleBean(SnsAsyncClient.class);
                    assertThat(context).doesNotHaveBean(DynamoDbAsyncClient.class);
                    assertThat(context).doesNotHaveBean(DynamoDbStreamsAsyncClient.class);
                    assertThat(context).doesNotHaveBean(CloudWatchAsyncClient.class);
                    assertThat(context).doesNotHaveBean(LambdaAsyncClient.class);
                    assertThat(context).doesNotHaveBean(SecretsManagerAsyncClient.class);
                    assertThat(context).doesNotHaveBean(KinesisAsyncClient.class);
                    assertThat(context).doesNotHaveBean(IamAsyncClient.class);

                });
    }

    @Test
    public void shouldNotAutoConfigureAWSServiceClientsWhenLocalStackInNotEnabled() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.enabled=false",
                        "localstack.services=SQS,S3,SNS,DYNAMODB,DYNAMODBSTREAMS,KINESIS,IAM,LAMBDA,CLOUDWATCH,SECRETSMANAGER",
                        "localstack.s3.enabled=true",
                        "localstack.sqs.enabled=true",
                        "localstack.sns.enabled=true",
                        "localstack.dynamodb.enabled=true",
                        "localstack.dynamodbstreams.enabled=true",
                        "localstack.kinesis.enabled=true",
                        "localstack.iam.enabled=true",
                        "localstack.lambda.enabled=true",
                        "localstack.cloudwatch.enabled=true",
                        "localstack.secretsmanager.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).doesNotHaveBean(LocalStackProperties.class);
                    assertThat(context).doesNotHaveBean(S3Client.class);
                    assertThat(context).doesNotHaveBean(SqsAsyncClient.class);
                    assertThat(context).doesNotHaveBean(SnsAsyncClient.class);
                    assertThat(context).doesNotHaveBean(DynamoDbAsyncClient.class);
                    assertThat(context).doesNotHaveBean(DynamoDbStreamsAsyncClient.class);
                    assertThat(context).doesNotHaveBean(CloudWatchAsyncClient.class);
                    assertThat(context).doesNotHaveBean(LambdaAsyncClient.class);
                    assertThat(context).doesNotHaveBean(SecretsManagerAsyncClient.class);
                    assertThat(context).doesNotHaveBean(KinesisAsyncClient.class);
                    assertThat(context).doesNotHaveBean(IamAsyncClient.class);

                });
    }
}
