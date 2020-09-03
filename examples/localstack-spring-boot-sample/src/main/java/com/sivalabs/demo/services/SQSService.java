package com.sivalabs.demo.services;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SQSService {
    private final AmazonSQSAsync amazonSQS;

    public void createQueue(String queueName) {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName)
                .addAttributesEntry("DelaySeconds", "60")
                .addAttributesEntry("MessageRetentionPeriod", "86400");

        try {
            amazonSQS.createQueue(createQueueRequest);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
    }

    public ListQueuesResult listQueues() {
        return amazonSQS.listQueues();
    }

    public void sendMessage(String queueName, String msg) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(msg)
                .withDelaySeconds(0);
        amazonSQS.sendMessage(sendMessageRequest);
    }

    public List<Message> readMessages(String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        return amazonSQS.receiveMessage(queueUrl).getMessages();
    }

    public void deleteMessage(String queueName, Message message) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
    }

    public void deleteQueue(String queueName) {
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        amazonSQS.deleteQueue(queueUrl);
    }

}

