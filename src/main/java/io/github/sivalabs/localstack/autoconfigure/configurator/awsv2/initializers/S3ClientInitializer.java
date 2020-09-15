package io.github.sivalabs.localstack.autoconfigure.configurator.awsv2.initializers;

import com.amazonaws.SdkClientException;
import io.github.sivalabs.localstack.LocalStackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class S3ClientInitializer {
    private final LocalStackProperties localStackProperties;
    private final S3Client s3Client;


    public void init() {
        boolean continueOnBucketCreationError = this.localStackProperties.getS3().isContinueOnBucketCreationError();
        List<String> buckets = this.localStackProperties.getS3().getBuckets();
        for (String bucket : buckets) {
            try {
                CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucket).build();
                s3Client.createBucket(request);
                log.debug("Created S3 bucket {}", bucket);
            } catch (SdkClientException e) {
                log.error("Failed to create S3 bucket: {}, Error: {}", bucket, e);
                if (!continueOnBucketCreationError) {
                    throw e;
                }
            }
        }
    }
}
