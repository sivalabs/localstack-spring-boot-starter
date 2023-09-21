package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;

@SpringBootTest
@Slf4j
public class LambdaTest {
    @Autowired
    private LambdaAsyncClient awsLambda;

    @Test
    void shouldWorkWithLambda() {
        var functionResult = awsLambda.listFunctions().join();
        var functions = functionResult.functions();
        for (var config : functions) {
            System.out.println("The function name is "+config.functionName());
        }
    }
}
