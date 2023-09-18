package com.sivalabs.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.policybuilder.iam.*;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.CreatePolicyRequest;
import software.amazon.awssdk.services.iam.model.CreatePolicyResponse;
import software.amazon.awssdk.services.iam.model.GetPolicyRequest;
import software.amazon.awssdk.services.iam.model.GetPolicyResponse;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.SetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.SetQueueAttributesResponse;

import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class SNSServiceTest {
    private static final String topicName = "test_topic_" + System.currentTimeMillis();
    private static final String queueName = "test_queue_" + System.currentTimeMillis();

    @Autowired
    private SNSService snsService;

    @Autowired
    private SQSService sqsService;

    @Autowired
    private SqsAsyncClient sqsClient;
    @Autowired
    private SnsAsyncClient snsClient;
    @Autowired
    private IamClient iamClient;
    private CreateTopicResponse createTopicResponse;

    @BeforeEach
    void setUp() {
        sqsService.createQueue(queueName).join();
        log.info("Created SQS Queue: {}", queueName);
        this.createTopicResponse = snsService.createTopic(topicName).join();
        log.info("Created SNS topic: {}", topicName);
    }

    @AfterEach
    void tearDown() {
        snsService.deleteTopic(createTopicResponse.topicArn()).join();
        log.info("Deleted SNS topic: {}", topicName);
        sqsService.deleteQueue(queueName).join();
        log.info("Deleted SQS Queue: {}", queueName);
    }

    private String buildPolicyJson(String queueArn, String topicArn) {
        IamPolicy policy = IamPolicy.builder()
                .addStatement(b -> b
                        .effect(IamEffect.ALLOW)
                        .addPrincipal(IamPrincipal.ALL)
                        .addAction("sqs:SendMessage")
                        .addResource(queueArn)
                        .addCondition(b1 -> b1
                                .operator(IamConditionOperator.ARN_EQUALS)
                                .key("aws:SourceArn")
                                .value(topicArn)))
                .build();

        // Use an IamPolicyWriter to write out the JSON string to a more readable format.
        return policy.toJson(IamPolicyWriter.builder()
                .prettyPrint(true)
                .build());
    }

    private String createPolicy(String document) {
        // Create an IamWaiter object.
        var iamWaiter = iamClient.waiter();

        CreatePolicyRequest request = CreatePolicyRequest.builder()
                .policyName(topicName + "_policy")
                .policyDocument(document)
                .build();

        CreatePolicyResponse response = iamClient.createPolicy(request);

        // Wait until the policy is created.
        GetPolicyRequest polRequest = GetPolicyRequest.builder()
                .policyArn(response.policy().arn())
                .build();

        WaiterResponse<GetPolicyResponse> waitUntilPolicyExists = iamWaiter.waitUntilPolicyExists(polRequest);
        waitUntilPolicyExists.matched().response().ifPresent(System.out::println);
        return response.policy().arn();
    }

    @Test
    public void sendAndReceiveSNsMessage() {
        final var getQueueUrlResponse = sqsService.getQueueUrl(queueName).join();
        final var sqsQueueUrl = getQueueUrlResponse.queueUrl();
        final var topicArn = createTopicResponse.topicArn();
        System.out.println("Got topic arn " + topicArn);

        var QueueAttributesRequest = GetQueueAttributesRequest.builder()
                .queueUrl(sqsQueueUrl)
                .attributeNamesWithStrings("All")
                .build();

        var queueAttributesResponse = sqsClient.getQueueAttributes(QueueAttributesRequest).join();
        Map<String, String> sqsAttributeMap = queueAttributesResponse.attributesAsStrings();

        System.out.println("queue attributes: " + sqsAttributeMap);
        final String queueArn = sqsAttributeMap.get("QueueArn");
        System.out.println("Got queue arn " + queueArn);
        var subscribeRequest = SubscribeRequest.builder()
                .protocol("sqs")
                .endpoint(queueArn)
                .returnSubscriptionArn(true)
                .topicArn(topicArn)
                .build();
        var subscribeResponse = snsClient.subscribe(subscribeRequest).join();
        assertThat(subscribeResponse.sdkHttpResponse().statusCode()).isEqualTo(200);
        var policy = buildPolicyJson(queueArn, topicArn);
        System.out.println("Got policy " + policy);
        var policyArn = createPolicy(policy);
        System.out.println("Got policy arn " + policyArn);

        var setQueueAttributesRequest = SetQueueAttributesRequest.builder()
                .queueUrl(sqsQueueUrl)
                .attributes(Map.of(QueueAttributeName.POLICY, policy))
                .build();
        var response = sqsClient.setQueueAttributes(setQueueAttributesRequest).join();
        assertThat(response.sdkHttpResponse().statusCode()).isEqualTo(200);
        var publishResponse = snsService.sendMessageByTopicArn(topicArn, "Test Message").join();
        assertThat(publishResponse.sdkHttpResponse().statusCode()).isEqualTo(200);
        await().atMost(15, SECONDS).untilAsserted(() -> {
            try {
                var receiveMessageResponse = sqsService.readMessages(queueName, 10, 5).join();
                assertThat(receiveMessageResponse.sdkHttpResponse().statusCode()).isEqualTo(200);
                var messages = receiveMessageResponse.messages();
                assertThat(messages.size()).isEqualTo(1);
            } catch (Exception e) {
                System.out.println("Got exception " + e);
            }
        });
    }
}