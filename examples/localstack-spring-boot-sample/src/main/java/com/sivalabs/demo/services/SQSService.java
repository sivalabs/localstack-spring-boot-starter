package com.sivalabs.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.*;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SQSService {
    private final SqsAsyncClient sqsAsyncClient;

    public CompletableFuture<SetQueueAttributesResponse> createQueue(String queueName) {
        return sqsAsyncClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build())
                .thenCompose(createQueueResponse -> sqsAsyncClient.setQueueAttributes(
                        SetQueueAttributesRequest.builder()
                                .queueUrl(createQueueResponse.queueUrl())
                                .attributes(Map.of(
                                        QueueAttributeName.DELAY_SECONDS, "0",
                                        QueueAttributeName.MESSAGE_RETENTION_PERIOD, "86400"))
                                .build()));
    }

    public CompletableFuture<ListQueuesResponse> listQueues() {
        return sqsAsyncClient.listQueues();
    }

    public CompletableFuture<GetQueueUrlResponse> getQueueUrl(String queueName) {
        return sqsAsyncClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
    }

    public CompletableFuture<GetQueueAttributesResponse> getQueueAttributes(String queueName) {
        return getQueueUrl(queueName).thenCompose(getQueueUrlResponse -> sqsAsyncClient.getQueueAttributes(
                GetQueueAttributesRequest.builder()
                        .queueUrl(getQueueUrlResponse.queueUrl())
                        .attributeNames(QueueAttributeName.QUEUE_ARN, QueueAttributeName.VISIBILITY_TIMEOUT)
                        .build()));
    }

    public CompletableFuture<SendMessageResponse> sendMessage(String queueName, String msg) {
        return getQueueUrl(queueName)
                .thenCompose(getQueueUrlResponse ->
                        sqsAsyncClient.sendMessage(
                                SendMessageRequest.builder()
                                        .queueUrl(getQueueUrlResponse.queueUrl())
                                        .messageBody(msg)
                                        .delaySeconds(0).build()));

    }

    public CompletableFuture<ReceiveMessageResponse> readMessages(String queueName, int maxNumberOfMessages, int waitTimeSecs) {
        return getQueueUrl(queueName)
                .thenCompose(getQueueUrlResponse -> sqsAsyncClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .queueUrl(getQueueUrlResponse.queueUrl())
                                .maxNumberOfMessages(maxNumberOfMessages)
                                .waitTimeSeconds(waitTimeSecs)
                                .build()));
    }

    public CompletableFuture<DeleteMessageResponse> deleteMessage(String queueName, Message message) {
        return getQueueUrl(queueName)
                .thenCompose(getQueueUrlResponse -> sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(getQueueUrlResponse.queueUrl())
                        .receiptHandle(message.receiptHandle())
                        .build()));
    }

    public CompletableFuture<DeleteQueueResponse> deleteQueue(String queueName) {
        return getQueueUrl(queueName)
                .thenCompose(getQueueUrlResponse -> sqsAsyncClient.deleteQueue(DeleteQueueRequest.builder()
                        .queueUrl(getQueueUrlResponse.queueUrl())
                        .build()));
    }

}

