package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy;
import software.amazon.awssdk.core.waiters.WaiterOverrideConfiguration;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class KinesisTest {
    private static final String streamName = "test_kinesis_stream";

    @Autowired
    private KinesisAsyncClient amazonKinesis;


    @Test
    void shouldCreateAndListKinesisStreams() {
        List<String> streams = listStreams();
        assertThat(streams).doesNotContain(streamName);
        createStream(2);
        streams = listStreams();
        assertThat(streams).contains(streamName);
        deleteStream();
    }

    private List<String> listStreams() {
        ListStreamsRequest listStreamsRequest = ListStreamsRequest.builder().limit(2).build();
        var listStreamsResult = amazonKinesis.listStreams(listStreamsRequest).join();
        List<String> streamNames = listStreamsResult.streamNames();
        System.out.println("found " + streamNames + " streams");
        while (listStreamsResult.hasMoreStreams()) {
            var req = ListStreamsRequest.builder()
                    .limit(2)
                    .exclusiveStartStreamName(streamNames.get(streamNames.size() - 1))
                    .build();

            listStreamsResult = amazonKinesis.listStreams(req).join();
            streamNames.addAll(listStreamsResult.streamNames());
        }
        return streamNames;
    }

    private void createStream(Integer streamSize) {
        // Create a stream. The number of shards determines the provisioned throughput.
        var createStreamRequest = CreateStreamRequest.builder().streamName(streamName).shardCount(streamSize).build();
        System.out.println("Creating stream " + streamName);
        var response = amazonKinesis.createStream(createStreamRequest).join();
        assertThat(response.sdkHttpResponse().statusCode()).isEqualTo(200);
        var waiterConfig = WaiterOverrideConfiguration.builder()
                .waitTimeout(Duration.ofSeconds(180))
                .maxAttempts(10)
                .backoffStrategy(BackoffStrategy.defaultStrategy())
                .build();
        var waiterResponse = amazonKinesis.waiter()
                .waitUntilStreamExists(
                        DescribeStreamRequest.builder().streamName(streamName).limit(2).build(),
                        waiterConfig);
        System.out.println("Waiting for stream to exist...");
        waiterResponse.whenComplete((r, t) -> {
            r.matched().exception().ifPresent(System.out::println);
            assertThat(t).isNull();
            assertThat(r.matched().exception().isPresent()).isFalse();
            assertThat(r.matched().response().isPresent()).isTrue();
            assertThat(r.matched().response().get().streamDescription().streamStatus()).isEqualTo(StreamStatus.ACTIVE);
        }).join();
    }

    private void deleteStream() {
        var deleteStreamRequest = DeleteStreamRequest.builder().streamName(streamName).build();
        var deleteStreamResult = amazonKinesis.deleteStream(deleteStreamRequest).join();
        assertThat(deleteStreamResult.sdkHttpResponse().statusCode()).isEqualTo(200);
        var waiterConfig = WaiterOverrideConfiguration.builder()
                .waitTimeout(Duration.ofSeconds(180))
                .maxAttempts(10)
                .backoffStrategy(BackoffStrategy.defaultStrategy())
                .build();
        System.out.println("Waiting for stream to be deleted...");
        var waiterResponse = amazonKinesis.waiter()
                .waitUntilStreamNotExists(
                        DescribeStreamRequest.builder().streamName(streamName).limit(2).build(),
                        waiterConfig);

        waiterResponse.whenComplete((r, t) -> {
            System.out.println("Stream deleted");
            r.matched().exception().ifPresent(System.out::println);
            assertThat(t).isNull();
            assertThat(r.matched().exception().isPresent()).isTrue();
            assertThat(r.matched().exception().get()).isInstanceOf(ResourceNotFoundException.class);
        }).join();
    }
}
