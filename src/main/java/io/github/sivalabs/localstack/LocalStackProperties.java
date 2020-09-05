package io.github.sivalabs.localstack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.Collection;

@ConfigurationProperties(prefix = LocalStackProperties.LOCALSTACK_PREFIX)
@Setter
@Getter
public class LocalStackProperties {
    public static final String LOCALSTACK_PREFIX = "localstack";
    public static final String BEAN_NAME_LOCALSTACK = "localStackBean";
    public static final boolean ENABLE_SERVICE_BY_DEFAULT = true;

    private boolean enabled = true;
    private int edgePort = 4566;
    private String defaultRegion = "us-east-1";
    private String hostname = "localhost";
    private String hostnameExternal = "localhost";
    private String dockerImage = "localstack/localstack:0.11.2";
    private boolean useSsl = false;
    private Collection<LocalStackContainer.Service> services;

    private S3Properties s3 = new S3Properties();
    private SQSProperties sqs = new SQSProperties();
    private SNSProperties sns = new SNSProperties();
    private DynamoDBProperties dynamodb = new DynamoDBProperties();
    private DynamoDBStreamsProperties dynamodbstreams = new DynamoDBStreamsProperties();
    private KinesisProperties kinesis = new KinesisProperties();
    private LambdaProperties lambda = new LambdaProperties();
    private IamProperties iam = new IamProperties();
    private SecretsManagerProperties secretsmanager = new SecretsManagerProperties();
    private CloudWatchProperties cloudwatch = new CloudWatchProperties();

    @Setter
    @Getter
    public static class S3Properties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class SQSProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class SNSProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class DynamoDBProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class DynamoDBStreamsProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class KinesisProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class LambdaProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class IamProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class SecretsManagerProperties extends CommonProperties {
    }

    @Setter
    @Getter
    public static class CloudWatchProperties extends CommonProperties {
    }

    @Setter
    @Getter
    private static class CommonProperties {
        private boolean enabled = ENABLE_SERVICE_BY_DEFAULT;
    }
}
