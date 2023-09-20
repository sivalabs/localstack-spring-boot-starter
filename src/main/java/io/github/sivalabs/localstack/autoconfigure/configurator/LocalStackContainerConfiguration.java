package io.github.sivalabs.localstack.autoconfigure.configurator;

import io.github.sivalabs.localstack.LocalStackProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;

@Configuration
@ConditionalOnClass({LocalStackContainer.class})
@ConditionalOnProperty(name = "localstack.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LocalStackProperties.class)
public class LocalStackContainerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LocalStackContainerConfiguration.class);

    @ConditionalOnMissingBean(name = BEAN_NAME_LOCALSTACK)
    @Bean(name = BEAN_NAME_LOCALSTACK, destroyMethod = "stop")
    public LocalStackContainer localStack(ConfigurableEnvironment environment,
                                          LocalStackProperties properties) {
        log.info("Starting Localstack server. Docker image: {}", properties.getDockerImage());

        LocalStackContainer localStackContainer = new EmbeddedLocalStackContainer(properties.getDockerImage());
        localStackContainer.withEnv("GATEWAY_LISTEN", String.valueOf(properties.getGatewayListen()))
                .withEnv("HOSTNAME", properties.getHostname())
                .withEnv("LOCALSTACK_HOST", properties.getLocalstackHost());

        for (LocalStackContainer.Service service : properties.getServices()) {
            localStackContainer.withServices(service);
        }
        localStackContainer.start();
        registerLocalStackEnvironment(localStackContainer, environment, properties);
        return localStackContainer;
    }

    private void registerLocalStackEnvironment(LocalStackContainer localStack,
                                               ConfigurableEnvironment environment,
                                               LocalStackProperties properties) {
        String host = localStack.getContainerIpAddress();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("localstack.host", host);
        map.put("localstack.accessKey", localStack.getAccessKey());
        map.put("localstack.secretKey", localStack.getSecretKey());
        map.put("localstack.region", localStack.getRegion());
        String prefix = "localstack.";
        for (LocalStackContainer.Service service : properties.getServices()) {
            map.put(prefix + service + ".endpoint", localStack.getEndpointOverride(service));
        }
        log.info("Started Localstack. Connection details: {}", map);

        MapPropertySource propertySource = new MapPropertySource("localstackInfo", map);
        environment.getPropertySources().addFirst(propertySource);
        setSystemProperties(localStack);
    }

    private static void setSystemProperties(LocalStackContainer localStack) {
        System.setProperty("aws.accessKeyId", localStack.getAccessKey());
        System.setProperty("aws.secretKey", localStack.getAccessKey());
        System.setProperty("com.amazonaws.sdk.disableCbor", "true");
        System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");
    }

    private static class EmbeddedLocalStackContainer extends LocalStackContainer {
        EmbeddedLocalStackContainer(final String dockerImageName) {
            setDockerImageName(dockerImageName);
        }
    }
}
