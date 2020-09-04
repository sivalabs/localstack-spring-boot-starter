package com.sivalabs.demo.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private AmazonDynamoDBAsync amazonDynamoDBAsync;

    @Test
    void shouldGetTableNames() {
        List<String> tables = dynamoDBService.listTables();
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
        keyToGet.put(key, new AttributeValue(name));

        GetItemRequest request = new GetItemRequest().withKey(keyToGet).withTableName(tableName);
        GetItemResult outcome = amazonDynamoDBAsync.getItem(request);
        return outcome.getItem().get(key).getS();
    }

    private void insertData(String name) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put(key, new AttributeValue(name));
        amazonDynamoDBAsync.putItem(tableName, itemValues);
        log.info("Item inserted into table: {} successfully", tableName);
    }

    private void createTable() {
        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(new AttributeDefinition(key, ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement(key, KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withTableName(tableName);
        CreateTableResult result = amazonDynamoDBAsync.createTable(request);
        log.info("Table with name : {} created successfully", result.getTableDescription().getTableName());
    }
}