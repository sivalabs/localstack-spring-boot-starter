package io.github.sivalabs.localstack.autoconfigure.configurator;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.sivalabs.localstack.LocalStackProperties.BEAN_NAME_LOCALSTACK;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ConditionalOnExpression("${localstack.enabled:true}")
@AutoConfigureAfter(name = BEAN_NAME_LOCALSTACK)
public @interface ConditionalOnLocalStackService {

}
