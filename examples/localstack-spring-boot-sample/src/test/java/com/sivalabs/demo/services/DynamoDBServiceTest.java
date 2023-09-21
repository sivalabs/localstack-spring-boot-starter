package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class DynamoDBServiceTest {

    private static final String tableName = "sample";
    private static final String key = "Name";

    @Autowired
    private DynamoDBService dynamoDBService;

    @Autowired
    private DynamoDbAsyncClient amazonDynamoDBAsync;

    @Test
    void shouldGetTableNames() {
        List<String> tables = dynamoDBService.listTables().join();
        assertThat(tables).isNotNull();
    }

    @Test
    void shouldBeAbleToDoBasicOperationsWithDynamoDB() {
        String name = "Siva";
        createTable();
        insertData(name);
        String result = readData(name);
        assertThat(result).isEqualTo(name);
    }

    private String readData(String name) {
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(key, AttributeValue.fromS(name));

        GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName(tableName).build();
        GetItemResponse outcome = amazonDynamoDBAsync.getItem(request).join();
        return outcome.item().get(key).s();
    }

    private void insertData(String name) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put(key, AttributeValue.fromS(name));
        amazonDynamoDBAsync.putItem(PutItemRequest.builder().item(itemValues).tableName(tableName).build()).join();
        log.info("Item inserted into table: {} successfully", tableName);
    }

    private void createTable() {
        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder().attributeName(key).attributeType(ScalarAttributeType.S).build())
                .keySchema(KeySchemaElement.builder().attributeName(key).keyType(KeyType.HASH).build())
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
                .tableName(tableName)
                .build();

        CreateTableResponse result = amazonDynamoDBAsync.createTable(request).join();
        log.info("Table with name : {} created successfully", result.tableDescription().tableName());
    }
}