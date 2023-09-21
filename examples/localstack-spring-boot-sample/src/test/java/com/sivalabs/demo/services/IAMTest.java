package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.iam.IamAsyncClient;
import software.amazon.awssdk.services.iam.model.CreateGroupRequest;
import software.amazon.awssdk.services.iam.model.GetGroupRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class IAMTest {
    private static final String test_group = "test_group";

    @Autowired
    private IamAsyncClient iamAsync;

    @Test
    void shouldWorkWithAWSIAM() {
        var request = CreateGroupRequest.builder().groupName(test_group).build();
        var result = iamAsync.createGroup(request).join();
        assertThat(result).isNotNull();

        var getGroupRequest = GetGroupRequest.builder().groupName(test_group).build();
        var group = iamAsync.getGroup(getGroupRequest).join();
        assertThat(group).isNotNull();
    }
}
