package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        sqsService.createQueue(queueName).join();
        log.info("Created SQS Queue: {}", queueName);
    }

    @AfterEach
    void tearDown() {
        sqsService.deleteQueue(queueName).join();
        log.info("Deleted SQS Queue: {}", queueName);
    }

    @Test
    void shouldGetAllQueues() {
        var listQueuesResult = sqsService.listQueues().join();

        assertThat(listQueuesResult.queueUrls()).isNotEmpty();

        for (String queueUrl : listQueuesResult.queueUrls()) {
            log.info("QueueUrl: {}", queueUrl);
        }
    }

    @Test
    public void sendAndReceiveSqsMessage() {
        sqsService.sendMessage(queueName, "Test Message").join();

        await().atMost(15, SECONDS).untilAsserted(() -> {
            var messages = sqsService.readMessages(queueName, 10, 5).join().messages();
            assertThat(messages.size()).isEqualTo(1);
        });
    }
}