package io.github.sivalabs.localstack.autoconfigure.configurator.awsv2;

import io.github.sivalabs.localstack.LocalStackProperties;
import io.github.sivalabs.localstack.autoconfigure.configurator.ConditionalOnLocalStackService;
import io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.initializers.SqsClientInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import static io.github.sivalabs.localstack.LocalStackProperties.ENABLE_SERVICE_BY_DEFAULT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@ConditionalOnLocalStackService
@ConditionalOnProperty(name = "localstack.sqs.enabled", havingValue = "true", matchIfMissing = ENABLE_SERVICE_BY_DEFAULT)
@ConditionalOnClass(SqsClient.class)
public class AmazonSQSV2Configuration extends AbstractAmazonV2Client {
    private final LocalStackProperties localStackProperties;

    public AmazonSQSV2Configuration(LocalStackProperties localStackProperties, LocalStackContainer localStackContainer) {
        super(localStackContainer);
        this.localStackProperties = localStackProperties;
    }

    @Bean
    @Primary
    public SqsClient sqsClientLocalStack() {
        SqsClient sqsClient = wrapApiClientV2(SqsClient.builder(), SQS).build();
        this.initSQS(sqsClient);
        return sqsClient;
    }

    @Bean
    @Primary
    public SqsAsyncClient sqsAsyncClientLocalStack() {
        return wrapApiAsyncClientV2(SqsAsyncClient.builder(), SQS).build();
    }

    private void initSQS(SqsClient sqsClient) {
        if (localStackProperties.getInitializer().getVersion() == LocalStackProperties.AwsSdkVersion.V2) {
            SqsClientInitializer sqsInitializer = new SqsClientInitializer(localStackProperties, sqsClient);
            sqsInitializer.init();
        }
    }

}
