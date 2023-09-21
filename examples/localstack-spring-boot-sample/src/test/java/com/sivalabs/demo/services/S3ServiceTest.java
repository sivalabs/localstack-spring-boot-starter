package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class S3ServiceTest {
    private static final String bucketName = "test-bucket-"+System.currentTimeMillis();

    @Autowired
    private S3Service s3Service;

    @BeforeEach
    public void setUp() {
        s3Service.createBucket(bucketName);
        log.info("Created S3 Bucket: {}", bucketName);
    }

    @AfterEach
    public void tearDown() {
        s3Service.deleteBucketForce(bucketName);
        log.info("Deleted S3 Bucket: {}", bucketName);
    }

    @Test
    public void shouldStoreAndRetrieveDataFromS3Bucket() throws IOException {
        s3Service.store(bucketName, "my-key-1", "my-value-1");
        var response = s3Service.getObject(bucketName, "my-key-1");
        assertThat(new String(response.readAllBytes(), StandardCharsets.UTF_8)).isEqualTo("my-value-1");
    }
}