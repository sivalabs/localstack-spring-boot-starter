package com.sivalabs.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SNSService {

    private final SnsAsyncClient snsAsyncClient;

    public CompletableFuture<CreateTopicResponse> createTopic(String topicName) {
        return snsAsyncClient.createTopic(CreateTopicRequest.builder().name(topicName).build());
    }

    public CompletableFuture<DeleteTopicResponse> deleteTopic(String topicArn) {
        return snsAsyncClient.deleteTopic(DeleteTopicRequest.builder().topicArn(topicArn).build());
    }

    public CompletableFuture<PublishResponse> sendMessage(String topicName, String msg) {
        return createTopic(topicName).thenCompose(createTopicResponse -> {
            String topicArn = createTopicResponse.topicArn();
            System.out.println("The topic arn for topic "+ topicName + " is: " + topicArn);
            return sendMessageByTopicArn(topicArn, msg);
        });
    }

    public CompletableFuture<PublishResponse> sendMessageByTopicArn(String topicArn, String msg) {
        return snsAsyncClient.publish(PublishRequest.builder().topicArn(topicArn).message(msg).build());
    }
}
