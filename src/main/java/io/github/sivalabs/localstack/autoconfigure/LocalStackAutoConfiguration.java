package io.github.sivalabs.localstack.autoconfigure;

import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonS3Configuration;
import io.github.sivalabs.localstack.autoconfigure.configurator.AmazonSqsConfiguration;
import io.github.sivalabs.localstack.autoconfigure.configurator.LocalStackContainerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
@Import({
    LocalStackContainerConfiguration.class,
    AmazonS3Configuration.class,
    AmazonSqsConfiguration.class
})
public class LocalStackAutoConfiguration {

}
