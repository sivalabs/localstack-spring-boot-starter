package io.github.sivalabs.localstack.autoconfigure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSqsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalStackAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    LocalStackAutoConfiguration.class,
                    AmazonS3Configuration.class,
                    AmazonSqsConfiguration.class
                    ));

    @Test
    public void shouldAutoConfigureS3AndSQSBeans() {
        this.contextRunner
                .withPropertyValues(
                    "localstack.services=SQS,S3",
                    "localstack.s3.enabled=true",
                    "localstack.sqs.enabled=true"
                )
                .run((context) -> {
                    assertThat(context).hasSingleBean(LocalStackProperties.class);
                    assertThat(context).hasSingleBean(AmazonS3.class);
                    assertThat(context).hasSingleBean(AmazonSQSAsync.class);
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
                });
    }
}
