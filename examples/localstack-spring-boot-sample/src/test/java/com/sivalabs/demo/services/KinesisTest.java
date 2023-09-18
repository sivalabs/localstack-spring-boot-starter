package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class KinesisTest {
    private static final String streamName = "test_kinesis_stream";

    @Autowired
    private KinesisAsyncClient amazonKinesis;

    @Test
    void shouldCreateAndListKinesisStreams() throws Exception {
        createStream(streamName, 2);
        List<String> streams = listStreams();
        assertThat(streams).contains(streamName);
    }

    private List<String> listStreams() {
        ListStreamsRequest listStreamsRequest = ListStreamsRequest.builder().limit(10).build();
        var listStreamsResult = amazonKinesis.listStreams(listStreamsRequest).join();
        List<String> streamNames = listStreamsResult.streamNames();
        while (listStreamsResult.hasMoreStreams()) {
            /*if (!streamNames.isEmpty()) {
                listStreamsRequest.exclusiveStartStreamName(streamNames.get(streamNames.size() - 1));
            }*/
            var req = ListStreamsRequest.builder()
                    .limit(10)
                    .exclusiveStartStreamName(streamNames.get(streamNames.size() - 1))
                    .build();

            listStreamsResult = amazonKinesis.listStreams(req).join();
            streamNames.addAll(listStreamsResult.streamNames());
        }
        return streamNames;
    }

    private void createStream(String streamName, Integer streamSize) throws InterruptedException {
        // Create a stream. The number of shards determines the provisioned throughput.
        var createStreamRequest = CreateStreamRequest.builder().streamName(streamName).shardCount(streamSize).build();
        amazonKinesis.createStream(createStreamRequest).join();
        // The stream is now being created. Wait for it to become active.
        waitForStreamToBecomeAvailable(streamName);
    }

    private void waitForStreamToBecomeAvailable(String streamName) throws InterruptedException {
        System.out.printf("Waiting for %s to become ACTIVE...\n", streamName);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.MINUTES.toMillis(20);
        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));

            try {
                DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                        .streamName(streamName)
                        .limit(10)
                        .build();
                DescribeStreamResponse describeStreamResponse = amazonKinesis.describeStream(describeStreamRequest).join();

                var streamStatus = describeStreamResponse.streamDescription().streamStatus();
                System.out.printf("\t- current state: %s\n", streamStatus);
                if (StreamStatus.ACTIVE == streamStatus) {
                    return;
                }
            } catch (Exception e) {
                throw e;
            }
        }
        throw new RuntimeException(String.format("Stream %s never became active", streamName));
    }
}
