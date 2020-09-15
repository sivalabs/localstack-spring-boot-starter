package io.github.sivalabs.localstack.autoconfigure.configurator.awsv1.initializers;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import io.github.sivalabs.localstack.LocalStackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AmazonS3Initializer {
    private final LocalStackProperties localStackProperties;
    private final AmazonS3 amazonS3;

    public void init() {
        boolean continueOnBucketCreationError = this.localStackProperties.getS3().isContinueOnBucketCreationError();
        List<String> buckets = this.localStackProperties.getS3().getBuckets();
        for (String bucket : buckets) {
            try {
                amazonS3.createBucket(bucket);
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
