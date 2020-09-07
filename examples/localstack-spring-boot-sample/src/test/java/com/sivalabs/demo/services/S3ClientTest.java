package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class S3ClientTest {

    private static final String bucketName = "test-bucket-" + System.currentTimeMillis();

    @Autowired
    private S3Client s3Client;

    @BeforeEach
    public void setUp() {
        CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucketName).build();
        s3Client.createBucket(request);
        log.info("Created S3 Bucket: {}", bucketName);
    }

    @AfterEach
    public void tearDown() {
        DeleteBucketRequest request = DeleteBucketRequest.builder().bucket(bucketName).build();
        s3Client.deleteBucket(request);
        log.info("Deleted S3 Bucket: {}", bucketName);
    }

    @Test
    public void shouldStoreRetrieveAndDeleteDataFromS3Bucket() {
        String key = "my-key-1";
        s3Client.putObject(
            PutObjectRequest.builder().bucket(bucketName).key(key).build(),
            RequestBody.fromBytes("my-value-1".getBytes())
        );

        String content = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build(),
            ResponseTransformer.toBytes()).asString(StandardCharsets.UTF_8);
        assertThat(content).isNotNull();
        assertThat(content).isEqualTo("my-value-1");

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
        s3Client.deleteObject(deleteObjectRequest);
    }

}
