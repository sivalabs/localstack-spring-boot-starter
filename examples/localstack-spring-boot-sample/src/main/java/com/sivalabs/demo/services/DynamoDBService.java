package com.sivalabs.demo.services;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DynamoDBService {
    private final DynamoDbAsyncClient amazonDynamoDBAsync;

    public CompletableFuture<List<String>> listTables() {
        ListTablesRequest request = ListTablesRequest.builder().limit(10).build();
        return amazonDynamoDBAsync.listTables(request).thenApply(ListTablesResponse::tableNames);
    }
}
