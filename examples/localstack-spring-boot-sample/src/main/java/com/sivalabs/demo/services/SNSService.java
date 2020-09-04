package com.sivalabs.demo.services;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SNSService {

    private final AmazonSNSAsync amazonSNSAsync;

    public CreateTopicResult createTopic(String topicName) {
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
        CreateTopicResult createTopicResponse = amazonSNSAsync.createTopic(createTopicRequest);
        log.debug("Created SNS Topic : {}", createTopicResponse.getTopicArn());
        return createTopicResponse;
    }

    public DeleteTopicResult deleteTopic(String topicName) {
        CreateTopicResult topic = createTopic(topicName);
        return amazonSNSAsync.deleteTopic(topic.getTopicArn());
    }

    public PublishResult sendMessage(String topicName, String msg) {
        CreateTopicResult topic = createTopic(topicName);
        PublishRequest publishRequest = new PublishRequest(topic.getTopicArn(), msg);
        PublishResult publishResponse = amazonSNSAsync.publish(publishRequest);
        log.debug("SNS MessageId: " + publishResponse.getMessageId());
        return publishResponse;
    }
}
