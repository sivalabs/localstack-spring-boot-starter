package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class SecretsManagerTest {

    @Autowired
    private SecretsManagerAsyncClient secretsManagerAsync;

    @Test
    void shouldWorkWithSecretsManager() {
        CreateSecretRequest createSecretRequest = CreateSecretRequest.builder().name("db_password").secretString("secret").build();
        var secret = secretsManagerAsync.createSecret(createSecretRequest).join();

        var getSecretRequest = GetSecretValueRequest.builder().secretId(secret.arn()).build();
        var secretValue = secretsManagerAsync.getSecretValue(getSecretRequest).join();
        assertThat(secretValue.name()).isEqualTo("db_password");
        assertThat(secretValue.secretString()).isEqualTo("secret");
    }
}
