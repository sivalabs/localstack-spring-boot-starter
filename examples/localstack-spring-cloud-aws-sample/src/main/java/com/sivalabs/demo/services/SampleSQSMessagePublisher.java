package com.sivalabs.demo.services;

import com.sivalabs.demo.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class SampleSQSMessagePublisher {
    private final SQSService sqsService;
    private final ApplicationProperties applicationProperties;

    @Scheduled(fixedDelay = 5 * 1000)
    public void sendMessage() {
        sqsService.sendMessage(applicationProperties.getQueueName(), "test-message-" + System.currentTimeMillis());
        log.info("Sent a test message to queue: {} at : {}", applicationProperties.getQueueName(), Instant.now());
    }
}
