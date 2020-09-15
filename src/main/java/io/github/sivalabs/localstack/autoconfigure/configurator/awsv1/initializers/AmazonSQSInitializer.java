package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.initializers;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.sqs.AmazonSQS;
import io.github.sivalabs.localstack.LocalStackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AmazonSQSInitializer  {
    private final LocalStackProperties localStackProperties;
    private final AmazonSQS amazonSQS;

    public void init() {
        boolean continueOnQueueCreationError = this.localStackProperties.getSqs().isContinueOnQueueCreationError();
        List<String> queues = this.localStackProperties.getSqs().getQueues();
        for (String queue : queues) {
            try {
                amazonSQS.createQueue(queue);
                log.debug("Created SQS queue {}", queue);
            } catch (SdkClientException e) {
                log.error("Failed to create SQS queue: {}, Error: {}", queue, e);
                if (!continueOnQueueCreationError) {
                    throw e;
                }
            }
        }
    }

}
