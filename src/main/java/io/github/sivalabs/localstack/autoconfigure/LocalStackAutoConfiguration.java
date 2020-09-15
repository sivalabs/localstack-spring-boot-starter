package io.github.sivalabs.localstack.autoconfigure;

import io.github.sivalabs.localstack.autoconfigure.configurator.LocalStackContainerConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AWSLambdaConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AWSSecretsManagerConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonCloudWatchConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonDynamoDBConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonDynamoDBStreamsConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonIAMConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonKinesisConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonSNSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.AmazonSQSConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.AmazonS3V2Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.AmazonSQSV2Configuration;
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
    AmazonIAMConfiguration.class,

    AmazonS3V2Configuration.class,
    AmazonSQSV2Configuration.class,

})
public class LocalStackAutoConfiguration {

}
