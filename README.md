# localstack-spring-boot-starter ![Build](https://github.com/sivalabs/localstack-spring-boot-starter/workflows/Master%20Branch%20CI/badge.svg)

This is a SpringBoot starter for [LocalStack](https://github.com/localstack/localstack) auto-configuration.
This starter will spin up the *Localstack* docker container using [Testcontainers](https://www.testcontainers.org/) 
and auto-configure beans such as `AmazonS3`, `AmazonSQSAsync`, etc.

## How to use?

### Add the dependency

**Maven** 

```xml
<dependency>
    <groupId>io.github.sivalabs</groupId>
    <artifactId>localstack-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

**Gradle**

```groovy
implementation 'io.github.sivalabs:localstack-spring-boot-starter:0.0.1'
```

### Enable LocalStack AutoConfiguration

```java
package com.sivalabs.demo;

import io.github.sivalabs.localstack.EnableLocalStack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableLocalStack
public class LocalStackStarterDemoApplication {
    
    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonSQSAsync amazonSQS;

    public static void main(String[] args) {
        SpringApplication.run(LocalStackStarterDemoApplication.class, args);
    }
}
```

### Configuration

The following configuration properties are available to customize the default behaviour.

| Property | Required | Default Value |
| --- | --- | --- |
| `localstack.enabled` | no | `true` |
| `localstack.edgePort` | no | `4566` |
| `localstack.defaultRegion` | no | `us-east-1` |
| `localstack.hostname` | no | `localhost` |
| `localstack.hostnameExternal` | no | `localhost` |
| `localstack.dockerImage` | no | `localstack/localstack:0.11.2` |
| `localstack.useSsl` | no | `false` |
| `localstack.services` | no | `""` |

You can customize which AWS services to enable/disable as follows:

| Property  | Value | Default Value |
| --- | --- | --- |
| `localstack.services` | `S3,SQS,DYNAMODB,SNS` | `""`|
| `localstack.s3.enabled` | `false` | `true`|
| `localstack.sqs.enabled` | `true` | `true`|

## Note
The implementation of this starter is inspired by [testcontainers-spring-boot](https://github.com/testcontainers/testcontainers-spring-boot).
The `testcontainers-spring-boot` also provides localstack support, but it will only spin up the docker container, and 
you will have to configure the beans like `AmazonS3`, `AmazonSQSAsync` etc yourself.
So, I created this starter which will auto-configure the beans based on configuration properties.

## Developer Notes

Procedure for deploying to Maven Central https://central.sonatype.org/pages/apache-maven.html

Set version to SNAPSHOT (ex: 1.0.0-SNAPSHOT)

Deploy SNAPSHOT version to https://oss.sonatype.org/content/repositories/snapshots/

```shell script
localstack-spring-boot-starter> ./mvnw clean deploy -Prelease
```

Deploy release version to Maven Central

```shell script
localstack-spring-boot-starter> ./mvnw release:clean release:prepare -Prelease
localstack-spring-boot-starter> ./mvnw release:perform -Prelease
```

Search for release artifacts on https://oss.sonatype.org/#nexus-search;quick~sivalabs
