package com.sivalabs.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client amazonS3;

    public CreateBucketResponse createBucket(String bucketName) {
        return amazonS3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    }

    public ListBucketsResponse listBuckets() {
        return amazonS3.listBuckets();
    }

    public ResponseInputStream<GetObjectResponse> getObject(String bucketName, String key) {
        return amazonS3.getObject(GetObjectRequest.builder().bucket(bucketName).key(key).build());
    }

    public PutObjectResponse store(String bucketName, String key, String value) {
        return amazonS3.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromString(value)
        );
    }

    public PutObjectResponse store(String bucketName, String key, InputStream inputStream, long contentLength) {
        return amazonS3.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromInputStream(inputStream, contentLength)
        );
    }

    public PutObjectResponse store(String bucketName, String key, byte[] bytes) {
        return amazonS3.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromBytes(bytes)
        );
    }

    public DeleteBucketResponse deleteBucket(String bucketName) {
        return amazonS3.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
    }

    public DeleteBucketResponse deleteBucketForce(String bucketName) {
//        try {
            // Delete all objects from the bucket. This is sufficient
            // for unversioned buckets. For versioned buckets, when you attempt to delete objects, Amazon S3 inserts
            // delete markers for all objects, but doesn't delete the object versions.
            // To delete objects from versioned buckets, delete all of the object versions before deleting
            // the bucket (see below for an example).
            ListObjectsV2Request listRequest =
                    ListObjectsV2Request.builder()
                            .bucket(bucketName)
                            .build();
            ListObjectsV2Iterable paginatedListResponse = amazonS3.listObjectsV2Paginator(listRequest);

            for (ListObjectsV2Response listResponse : paginatedListResponse) {
                List<ObjectIdentifier> objects =
                        listResponse.contents().stream()
                                .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                                .toList();

                if (objects.isEmpty()) {
                    break;
                }
                DeleteObjectsRequest deleteRequest =
                        DeleteObjectsRequest.builder()
                                .bucket(bucketName)
                                .delete(Delete.builder().objects(objects).build())
                                .build();
                amazonS3.deleteObjects(deleteRequest);
            }

            ListObjectVersionsRequest listVersionsRequest = ListObjectVersionsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<ObjectVersion> objectVersions = amazonS3.listObjectVersions(listVersionsRequest).versions();

            // Delete all versions of objects
            for (ObjectVersion objectVersion : objectVersions) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectVersion.key())
                        .versionId(objectVersion.versionId())
                        .build();

                amazonS3.deleteObject(deleteObjectRequest);
                System.out.println("Deleted object: " + objectVersion.key() + " (Version ID: " + objectVersion.versionId() + ")");
            }

            // After all objects and object versions are deleted, delete the bucket.
            return amazonS3.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
//        } catch (AmazonServiceException e) {
//            // The call was transmitted successfully, but Amazon S3 couldn't process
//            // it, so it returned an error response.
//            e.printStackTrace();
//        } catch (SdkClientException e) {
//            // Amazon S3 couldn't be contacted for a response, or the client couldn't
//            // parse the response from Amazon S3.
//            e.printStackTrace();
//        }
    }

}

