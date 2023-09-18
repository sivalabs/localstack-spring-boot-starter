package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.retry.backoff.FixedDelayBackoffStrategy;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbAsyncWaiter;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
class DynamoDBStreamsTest {

    @Autowired
    private DynamoDbAsyncClient dynamoDBClient;

    @Autowired
    private DynamoDbStreamsAsyncClient streamsClient;

    @Test
    void shouldBeAbleToDoBasicOperationsWithDynamoDB() {
        try (DynamoDbAsyncWaiter asyncWaiter =
                DynamoDbAsyncWaiter.builder()
                        .client(dynamoDBClient)
                        .overrideConfiguration(o -> o.backoffStrategy(
                                FixedDelayBackoffStrategy.create(Duration.ofSeconds(2))))
                        .scheduledExecutorService(Executors.newScheduledThreadPool(3))
                        .build()) {

            // Source: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.LowLevel.Walkthrough.html
            // Create a table, with a stream enabled
            String tableName = "TestTableForStreams";

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>(
                    Collections.singletonList(AttributeDefinition.builder()
                            .attributeName("Id")
                            .attributeType("N").build()));

            ArrayList<KeySchemaElement> keySchema = new ArrayList<>(
                    Collections.singletonList(KeySchemaElement.builder()
                            .attributeName("Id")
                            .keyType(KeyType.HASH)
                            .build())); // Partition key

            StreamSpecification streamSpecification = StreamSpecification.builder()
                    .streamEnabled(true)
                    .streamViewType(StreamViewType.NEW_AND_OLD_IMAGES)
                    .build();

            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(keySchema)
                    .attributeDefinitions(attributeDefinitions)
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(10L)
                            .writeCapacityUnits(10L)
                            .build())
                    .streamSpecification(streamSpecification)
                    .build();

            System.out.println("Issuing CreateTable request for " + tableName);
            dynamoDBClient.createTable(createTableRequest).join();
            System.out.println("Waiting for " + tableName + " to be created...");

            try {
                var waiterResponse = asyncWaiter.waitUntilTableExists(b -> b.tableName(tableName),
                        o -> o.waitTimeout(Duration.ofMinutes(1)));
                waiterResponse.whenComplete((r, t) -> {
                    if (t == null) {
                        // print out the matched ResourceNotFoundException
                        r.matched().exception().ifPresent(System.out::println);
                    }
                }).join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Print the stream settings for the table
            var describeTableResult = dynamoDBClient.describeTable(DescribeTableRequest.builder().tableName(tableName).build()).join();
            String streamArn = describeTableResult.table().latestStreamArn();
            System.out.println("Current stream ARN for " + tableName + ": " + streamArn);
            StreamSpecification streamSpec = describeTableResult.table().streamSpecification();
            System.out.println("Stream enabled: " + streamSpec.streamEnabled());
            System.out.println("Update view type: " + streamSpec.streamViewType());
            System.out.println();

            // Generate write activity in the table

            System.out.println("Performing write activities on " + tableName);
            int maxItemCount = 10;
            for (Integer i = 1; i <= maxItemCount; i++) {
                System.out.println("Processing item " + i + " of " + maxItemCount);

                // Write a new item
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("Id", AttributeValue.builder().n(i.toString()).build());
                item.put("Message", AttributeValue.builder().s("New item!").build());
                dynamoDBClient.putItem(PutItemRequest.builder().item(item).tableName(tableName).build()).join();


                // Update the item
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("Id", AttributeValue.builder().n(i.toString()).build());
                Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
                attributeUpdates.put("Message", AttributeValueUpdate.builder()
                        .action(AttributeAction.PUT)
                        .value(AttributeValue.fromS("This item has changed"))
                        .build());
                dynamoDBClient.updateItem(UpdateItemRequest.builder().attributeUpdates(attributeUpdates).key(key).tableName(tableName).build()).join();

                // Delete the item
                dynamoDBClient.deleteItem(DeleteItemRequest.builder().key(key).tableName(tableName).build()).join();
            }

            // Get all the shard IDs from the stream.  Note that DescribeStream returns
            // the shard IDs one page at a time.
            String lastEvaluatedShardId = null;

            do {
                var describeStreamResult = streamsClient.describeStream(
                        DescribeStreamRequest.builder()
                                .streamArn(streamArn)
                                .exclusiveStartShardId(lastEvaluatedShardId)
                                .build()).join();
                List<Shard> shards = describeStreamResult.streamDescription().shards();

                // Process each shard on this page

                for (Shard shard : shards) {
                    String shardId = shard.shardId();
                    System.out.println("Shard: " + shard);

                    // Get an iterator for the current shard

                    GetShardIteratorRequest getShardIteratorRequest =
                            GetShardIteratorRequest.builder()
                                    .streamArn(streamArn)
                                    .shardId(shardId)
                                    .shardIteratorType(ShardIteratorType.TRIM_HORIZON)
                                    .build();

                    var getShardIteratorResult =
                            streamsClient.getShardIterator(getShardIteratorRequest).join();
                    String currentShardIter = getShardIteratorResult.shardIterator();

                    // Shard iterator is not null until the Shard is sealed (marked as READ_ONLY).
                    // To prevent running the loop until the Shard is sealed, which will be on average
                    // 4 hours, we process only the items that were written into DynamoDB and then exit.
                    int processedRecordCount = 0;
                    while (currentShardIter != null && processedRecordCount < maxItemCount) {
                        System.out.println("    Shard iterator: " + currentShardIter);

                        // Use the shard iterator to read the stream records

                        var getRecordsResult = streamsClient.getRecords(
                                GetRecordsRequest.builder()
                                        .shardIterator(currentShardIter)
                                        .build()).join();
                        var records = getRecordsResult.records();
                        for (var record : records) {
                            System.out.println("        " + record.dynamodb());
                        }
                        processedRecordCount += records.size();
                        currentShardIter = getRecordsResult.nextShardIterator();
                    }
                }

                // If LastEvaluatedShardId is set, then there is
                // at least one more page of shard IDs to retrieve
                lastEvaluatedShardId = describeStreamResult.streamDescription().lastEvaluatedShardId();

            } while (lastEvaluatedShardId != null);

            // Delete the table
            System.out.println("Deleting the table...");
            dynamoDBClient.deleteTable(DeleteTableRequest.builder().tableName(tableName).build()).join();

            System.out.println("Demo complete");
        }
    }
}
