package com.sivalabs.demo.services;

import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;

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
    public void shouldStoreAndRetrieveDataFromS3Bucket() {
        s3Service.store(bucketName, "my-key-1", "my-value-1");
        S3Object s3Object = s3Service.getObject(bucketName, "my-key-1");
        assertThat(s3Object.getObjectContent()).hasSameContentAs(new ByteArrayInputStream("my-value-1".getBytes()));
    }
}