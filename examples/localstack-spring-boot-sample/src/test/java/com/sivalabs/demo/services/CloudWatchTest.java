package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class CloudWatchTest {

    @Autowired
    private CloudWatchAsyncClient cloudWatchAsyncClient;

    @Test
    void shouldWorkWithCloudWatch() {
        Dimension dimension = Dimension.builder()
                .name("UNIQUE_PAGES")
                .value("URLS")
                .build();

        MetricDatum datum = MetricDatum.builder()
                .metricName("PAGES_VISITED")
                .unit(StandardUnit.NONE)
                .value(20.0)
                .dimensions(dimension)
                .build();

        PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("SITE/TRAFFIC")
                .metricData(datum)
                .build();

        cloudWatchAsyncClient.putMetricData(request);

        PutMetricDataResponse response = cloudWatchAsyncClient.putMetricData(request).join();
        assertThat(response).isNotNull();
    }
}
