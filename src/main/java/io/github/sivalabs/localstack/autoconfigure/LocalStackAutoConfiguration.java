package io.github.sivalabs.localstack.autoconfigure;

import io.github.sivalabs.localstack.autoconfigure.configurator.AWSLambdaConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AWSSecretsManagerConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonCloudWatchConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonDynamoDBConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonDynamoDBStreamsConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonIAMConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonKinesisConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSNSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSQSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.LocalStackContainerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import({
        LocalStackContainerConfiguration.class,
        AmazonS3Configuration.class,
        AmazonSQSConfiguration.class,
        AmazonSNSConfiguration.class,
        AmazonDynamoDBConfiguration.class,
        AmazonDynamoDBStreamsConfiguration.class,
        AmazonCloudWatchConfiguration.class,
        AWSLambdaConfiguration.class,
        AWSSecretsManagerConfiguration.class,
        AmazonKinesisConfiguration.class,
        AmazonIAMConfiguration.class
})
public class LocalStackAutoConfiguration {

}
