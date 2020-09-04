package io.github.sivalabs.localstack;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.Collection;

@ConfigurationProperties(prefix= LocalStackProperties.LOCALSTACK_PREFIX)
@Setter
@Getter
public class LocalStackProperties {
    public static final String LOCALSTACK_PREFIX = "localstack";
    public static final String BEAN_NAME_LOCALSTACK = "localStackBean";

    private boolean enabled = true;
    private int edgePort = 4566;
    private String defaultRegion = "us-east-1";
    private String hostname = "localhost";
    private String hostnameExternal = "localhost";
    private String dockerImage = "localstack/localstack:0.10.8";
    private boolean useSsl = false;
    private Collection<LocalStackContainer.Service> services;

    private S3Properties s3 = new S3Properties();
    private SQSProperties sqs = new SQSProperties();
    private SNSProperties sns = new SNSProperties();

    @Setter
    @Getter
    public static class S3Properties {
        private boolean enabled = true;
    }

    @Setter
    @Getter
    public static class SQSProperties {
        private boolean enabled = true;
    }

    @Setter
    @Getter
    public static class SNSProperties {
        private boolean enabled = true;
    }
}
