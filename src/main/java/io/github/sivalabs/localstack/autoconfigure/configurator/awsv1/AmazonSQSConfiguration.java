package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.initializers.AmazonSQSInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.sqs.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(AmazonSQSAsync.class)
@Slf4j
public class AmazonSQSConfiguration extends AbstractAmazonClient {
    private final LocalStackProperties localStackProperties;

    public AmazonSQSConfiguration(LocalStackProperties localStackProperties, LocalStackContainer localStackContainer) {
        super(localStackContainer);
        this.localStackProperties = localStackProperties;
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsyncLocalStack() {
        AmazonSQSAsync sqsAsync = AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(getEndpointConfiguration(SQS))
            .withCredentials(getCredentialsProvider())
            .build();
        this.initSQS(sqsAsync);
        return sqsAsync;
    }

    private void initSQS(AmazonSQSAsync sqsAsync) {
        if(localStackProperties.getInitializer().getVersion() == LocalStackProperties.AwsSdkVersion.V1) {
            AmazonSQSInitializer sqsInitializer = new AmazonSQSInitializer(localStackProperties, sqsAsync);
            sqsInitializer.init();
        }
    }
}
