package com.sivalabs.demo.services;

import com.amazonaws.services.secretsmanager.AWSSecretsManagerAsync;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.CreateSecretResult;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class SecretsManagerTest {

    @Autowired
    private AWSSecretsManagerAsync secretsManagerAsync;

    @Test
    void shouldWorkWithSecretsManager() {
        CreateSecretRequest createSecretRequest = new CreateSecretRequest()
                .withName("db_password").withSecretString("secret");
        CreateSecretResult secret = secretsManagerAsync.createSecret(createSecretRequest);

        GetSecretValueRequest getSecretRequest = new GetSecretValueRequest().withSecretId(secret.getARN());
        GetSecretValueResult secretValue = secretsManagerAsync.getSecretValue(getSecretRequest);
        assertThat(secretValue.getName()).isEqualTo("db_password");
        assertThat(secretValue.getSecretString()).isEqualTo("secret");
    }
}
