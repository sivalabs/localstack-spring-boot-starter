package com.sivalabs.demo.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamoDBService {
    private final AmazonDynamoDBAsync amazonDynamoDBAsync;

    public List<String> listTables() {
        ListTablesRequest request = new ListTablesRequest().withLimit(10);
        ListTablesResult tableList = amazonDynamoDBAsync.listTables(request);
        return tableList.getTableNames();
    }
}
