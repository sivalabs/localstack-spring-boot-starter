package io.github.sivalabs.localstack.autoconfigure;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsAsync;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsync;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AWSLambdaConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AWSSecretsManagerConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonCloudWatchConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonDynamoDBConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonIAMConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonKinesisConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonSNSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonSQSConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class LocalStackAutoConfigurationTest {

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
    void shouldAutoConfigureAWSServiceClientsBasedOnDefaultConfiguration() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.services=SQS,S3,SNS,DYNAMODB,DYNAMODBSTREAMS,KINESIS,IAM,LAMBDA,CLOUDWATCH,SECRETSMANAGER"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    LocalStackProperties properties = context.getBean(LocalStackProperties.class);
                    assertThat(properties.isEnabled()).isTrue();

                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).hasSingleBean(AmazonSQSAsync.class);
                    assertThat(context).hasSingleBean(AmazonSNSAsync.class);
                    assertThat(context).hasSingleBean(AmazonDynamoDBAsync.class);
                    assertThat(context).hasSingleBean(AmazonDynamoDBStreamsAsync.class);
                    assertThat(context).hasSingleBean(AmazonKinesisAsync.class);
                    assertThat(context).hasSingleBean(AmazonIdentityManagementAsync.class);
                    assertThat(context).hasSingleBean(AWSSecretsManagerAsync.class);
                    assertThat(context).hasSingleBean(AmazonCloudWatchAsync.class);
                    assertThat(context).hasSingleBean(AWSLambdaAsync.class);
                });
    }

    @Test
    void shouldAutoConfigureAllEnabledAWSServiceClients() {
        this.contextRunner
                .withPropertyValues(
                    "localstack.services=SQS,S3,SNS,DYNAMODB,DYNAMODBSTREAMS,KINESIS,IAM,LAMBDA,CLOUDWATCH,SECRETSMANAGER",
                    "localstack.s3.enabled=true",
                    "localstack.s3.buckets=bucket1,bucket2",
                    "localstack.sqs.enabled=true",
                    "localstack.sqs.queues=queue1,queue2",
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

                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).hasSingleBean(AmazonSQSAsync.class);
                    assertThat(context).hasSingleBean(AmazonSNSAsync.class);
                    assertThat(context).hasSingleBean(AmazonDynamoDBAsync.class);
                    assertThat(context).hasSingleBean(AmazonDynamoDBStreamsAsync.class);
                    assertThat(context).hasSingleBean(AmazonKinesisAsync.class);
                    assertThat(context).hasSingleBean(AmazonIdentityManagementAsync.class);
                    assertThat(context).hasSingleBean(AWSSecretsManagerAsync.class);
                    assertThat(context).hasSingleBean(AmazonCloudWatchAsync.class);
                    assertThat(context).hasSingleBean(AWSLambdaAsync.class);
                });
    }

    @Test
    void shouldAutoConfigureOnlyEnabledAWSServiceClients() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.services=SQS,S3",
                        "localstack.s3.enabled=true",
                        "localstack.s3.buckets=bucket1,bucket2",
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
                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).hasSingleBean(AmazonSQSAsync.class);
                    assertThat(context).hasSingleBean(AmazonSNSAsync.class);

                    assertThat(context).doesNotHaveBean(AmazonDynamoDBAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonDynamoDBStreamsAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonKinesisAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonIdentityManagementAsync.class);
                    assertThat(context).doesNotHaveBean(AWSSecretsManagerAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonCloudWatchAsync.class);
                    assertThat(context).doesNotHaveBean(AWSLambdaAsync.class);
                });
    }

    @Test
    void shouldNotAutoConfigureAWSServiceClientsWhenLocalStackInNotEnabled() {
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
                    assertThat(context).doesNotHaveBean(AmazonS3.class);
                    assertThat(context).doesNotHaveBean(AmazonSQSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonSNSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonDynamoDBAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonDynamoDBStreamsAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonKinesisAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonIdentityManagementAsync.class);
                    assertThat(context).doesNotHaveBean(AWSSecretsManagerAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonCloudWatchAsync.class);
                    assertThat(context).doesNotHaveBean(AWSLambdaAsync.class);
                });
    }
}
