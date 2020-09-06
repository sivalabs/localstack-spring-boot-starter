package com.sivalabs.demo.services;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsync;
import com.amazonaws.services.identitymanagement.model.CreateGroupRequest;
import com.amazonaws.services.identitymanagement.model.CreateGroupResult;
import com.amazonaws.services.identitymanagement.model.GetGroupRequest;
import com.amazonaws.services.identitymanagement.model.GetGroupResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class IAMTest {
    private static final String test_group = "test_group";

    @Autowired
    private AmazonIdentityManagementAsync iamAsync;

    @Test
    void shouldWorkWithAWSIAM() {
        CreateGroupRequest request = new CreateGroupRequest().withGroupName(test_group);
        CreateGroupResult result = iamAsync.createGroup(request);
        assertThat(result).isNotNull();

        GetGroupRequest getGroupRequest = new GetGroupRequest().withGroupName(test_group);
        GetGroupResult group = iamAsync.getGroup(getGroupRequest);
        assertThat(group).isNotNull();
    }
}
