package com.sivalabs.demo.services;

import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class SQSServiceTest {
    private static final String queueName = "test_queue_" + System.currentTimeMillis();

    @Autowired
    private SQSService sqsService;

    @BeforeEach
    void setUp() {
        sqsService.createQueue(queueName);
        log.info("Created SQS Queue: {}", queueName);
    }

    @AfterEach
    void tearDown() {
        sqsService.deleteQueue(queueName);
        log.info("Deleted SQS Queue: {}", queueName);
    }

    @Test
    void shouldGetAllQueues() {
        ListQueuesResult listQueuesResult = sqsService.listQueues();

        assertThat(listQueuesResult.getQueueUrls()).isNotEmpty();

        for (String queueUrl : listQueuesResult.getQueueUrls()) {
            log.info("QueueUrl: {}", queueUrl);
        }
    }

    @Test
    public void sendAndReceiveSqsMessage() {
        sqsService.sendMessage(queueName, "Test Message");

        List<Message> messages = sqsService.readMessages(queueName);
        await().atMost(5, SECONDS).untilAsserted(() -> assertThat(messages.size()).isEqualTo(1));
        for (Message message : messages) {
            log.info("Received Message from SQS Queue: {}, MessageBody: {}", queueName, message.getBody());
        }
    }
}