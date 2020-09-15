package io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.initializers;

import com.amazonaws.SdkClientException;
import io.github.sivalabs.localstack.LocalStackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SqsClientInitializer {
    private final LocalStackProperties localStackProperties;
    private final SqsClient sqsClient;

    public void init() {
        boolean continueOnQueueCreationError = this.localStackProperties.getSqs().isContinueOnQueueCreationError();
        List<String> queues = this.localStackProperties.getSqs().getQueues();
        for (String queue : queues) {
            try {
                CreateQueueRequest request = CreateQueueRequest.builder().queueName(queue).build();
                sqsClient.createQueue(request);
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
