package com.sivalabs.demo.services;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.ListFunctionsResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class LambdaTest {
    @Autowired
    private AWSLambdaAsync awsLambda;

    @Test
    void shouldWorkWithLambda() {
        ListFunctionsResult functionResult = awsLambda.listFunctions();
        List<FunctionConfiguration> functions = functionResult.getFunctions();
        for (FunctionConfiguration config : functions) {
            System.out.println("The function name is "+config.getFunctionName());
        }
    }
}
