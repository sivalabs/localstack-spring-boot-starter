package io.github.sivalabs.localstack.autoconfigure;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonDynamoDBConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSNSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSQSConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalStackAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    LocalStackAutoConfiguration.class,
                    AmazonS3Configuration.class,
                    AmazonSQSConfiguration.class,
                    AmazonSNSConfiguration.class,
                    AmazonDynamoDBConfiguration.class
                    ));

    @Test
    public void shouldAutoConfigureS3AndSQSBeans() {
        this.contextRunner
                .withPropertyValues(
                    "localstack.services=SQS,S3,SNS,DYNAMODB",
                    "localstack.s3.enabled=true",
                    "localstack.sqs.enabled=true",
                    "localstack.sns.enabled=true",
                    "localstack.dynamodb.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).hasSingleBean(AmazonSQSAsync.class);
                    assertThat(context).hasSingleBean(AmazonSNSAsync.class);
                    assertThat(context).hasSingleBean(AmazonDynamoDBAsync.class);
                    LocalStackProperties properties = context.getBean(LocalStackProperties.class);
                    assertThat(properties.isEnabled()).isTrue();
                });
    }

    @Test
    public void shouldAutoConfigureOnlyEnabledServices() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.services=SQS,S3",
                        "localstack.s3.enabled=true",
                        "localstack.sqs.enabled=false"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).doesNotHaveBean(AmazonSQSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonSNSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonDynamoDBAsync.class);
                });
    }

    @Test
    public void shouldNotAutoConfigureS3AndSQSBeansWhenLocalStackInNotEnabled() {
        this.contextRunner
                .withPropertyValues(
                        "localstack.enabled=false",
                        "localstack.services=SQS,S3",
                        "localstack.s3.enabled=true",
                        "localstack.sqs.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).doesNotHaveBean(LocalStackProperties.class);
                    assertThat(context).doesNotHaveBean(AmazonS3.class);
                    assertThat(context).doesNotHaveBean(AmazonSQSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonSNSAsync.class);
                    assertThat(context).doesNotHaveBean(AmazonDynamoDBAsync.class);
                });
    }
}
