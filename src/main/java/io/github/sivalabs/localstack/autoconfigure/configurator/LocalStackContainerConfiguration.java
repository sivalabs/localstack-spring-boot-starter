package io.github.sivalabs.localstack.autoconfigure.configurator;

import io.github.sivalabs.localstack.LocalStackProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;

@Configuration
@ConditionalOnClass({LocalStackContainer.class})
@ConditionalOnProperty(name = "localstack.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LocalStackProperties.class)
@Slf4j
public class LocalStackContainerConfiguration {

    @ConditionalOnMissingBean(name = BEAN_NAME_LOCALSTACK)
    @Bean(name = BEAN_NAME_LOCALSTACK, destroyMethod = "stop")
    public LocalStackContainer localStack(ConfigurableEnvironment environment,
                                          LocalStackProperties properties) {
        log.info("Starting Localstack server. Docker image: {}", properties.getDockerImage());

        LocalStackContainer localStackContainer = new EmbeddedLocalStackContainer(properties.getDockerImage());
        localStackContainer.withEnv("EDGE_PORT", String.valueOf(properties.getEdgePort()))
                .withEnv("DEFAULT_REGION", properties.getDefaultRegion())
                .withEnv("HOSTNAME", properties.getHostname())
                .withEnv("HOSTNAME_EXTERNAL", properties.getHostnameExternal())
                .withEnv("USE_SSL", String.valueOf(properties.isUseSsl()));

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

        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("localstack.host", host);
        map.put("localstack.accessKey", localStack.getAccessKey());
        map.put("localstack.secretKey", localStack.getSecretKey());
        map.put("localstack.region", localStack.getRegion());
        String prefix = "localstack.";
        for (LocalStackContainer.Service service : properties.getServices()) {
            map.put(prefix + service + ".endpoint", localStack.getEndpointConfiguration(service).getServiceEndpoint());
            map.put(prefix + service + ".port", localStack.getMappedPort(service.getPort()));
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
            super();
            setDockerImageName(dockerImageName);
        }
    }
}
