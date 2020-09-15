package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleSQSListener {

    @SqsListener(value = "${app.queueName}", deletionPolicy = SqsMessageDeletionPolicy.NO_REDRIVE)
    public void process(String message) {
        log.info("Received message from queue1: {}", message);
    }
}
