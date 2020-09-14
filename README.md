# Localstack SpringBoot Starter 

![Build](https://github.com/sivalabs/localstack-spring-boot-starter/workflows/Build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.sivalabs%3Alocalstack-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.sivalabs%3Alocalstack-spring-boot-starter)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.sivalabs/localstack-spring-boot-starter)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.github.sivalabs%22)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://raw.githubusercontent.com/sivalabs/localstack-spring-boot-starter/master/LICENSE)

`Localstack-spring-boot-starter` is a [SpringBoot](https://spring.io/projects/spring-boot) starter for [LocalStack](https://github.com/localstack/localstack) auto-configuration.
This starter will spin up the *Localstack* docker container using [Testcontainers](https://www.testcontainers.org/) 
and auto-configure beans such as `AmazonS3`, `AmazonSQSAsync`, etc.

## Motivation
[LocalStack](https://github.com/localstack/localstack) provides an easy-to-use test/mocking framework for developing AWS based Cloud applications.
We can use [Testcontainers](https://www.testcontainers.org/modules/localstack/) to spin up a *Localstack* docker container, 
but we need to configure Amazon service clients like `AmazonS3`, `AmazonSQSAsync` which is typical boilerplate that we copy-paste from project to project.
Instead of copy-pasting the code snippets, creating a SpringBoot starter which autoconfigures the Amazon service clients is a better approach and less error prone.
Hence, the birth of `localstack-spring-boot-starter` :-)

## Requirements
* JDK 8+
* Tested with SpringBoot 2.3.3.RELEASE, should work fine with any SpringBoot 2.x versions

## How to use?

### Add dependencies

**Maven** 

```xml
<dependencies>
    <dependency>
        <groupId>io.github.sivalabs</groupId>
        <artifactId>localstack-spring-boot-starter</artifactId>
        <version>0.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>localstack</artifactId>
        <version>1.14.3</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk</artifactId>
        <version>1.11.852</version>
    </dependency>
</dependencies>
```

**Gradle**

```groovy
implementation 'io.github.sivalabs:localstack-spring-boot-starter:0.0.1'
implementation 'org.testcontainers:localstack:1.14.3'
implementation 'com.amazonaws:aws-java-sdk:1.11.852'
```

### Enable LocalStack AutoConfiguration
You can enable LocalStack AutoConfiguration by adding `@EnableLocalStack` annotation to either main entrypoint class or 
any `@Configuration` class.

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

#### How to use only for Integration Tests?
You may want to use `localstack-spring-boot-starter` only for testing. 
In that case, you can add `@EnableLocalStack` annotation combined with `@Profile("integration-test")` annotation 
so that the Localstack AutoConfiguration is only activated while running integration tests.

```java
@Configuration
@EnableLocalStack
@Profile("integration-test")
public class TestConfig {
}
```

You can activate `integration-test` profile using `@ActiveProfiles` as follows:

```java
@SpringBootTest
@ActiveProfiles("integration-test")
class SomeIntegrationTest {
    @Autowired
    private AmazonS3 amazonS3;
    
    @Autowired
    private AmazonSQSAsync amazonSQS;

    @Test
    void someTest() {

    }
} 
```

### Configuration

The following configuration properties are available to customize the default behaviour.

| Property | Required | Default Value |
| -------- | -------- | ------------- |
| `localstack.enabled`          | no | `true`       |
| `localstack.edgePort`         | no | `4566`       |
| `localstack.defaultRegion`    | no | `us-east-1`  |
| `localstack.hostname`         | no | `localhost`  |
| `localstack.hostnameExternal` | no | `localhost`  |
| `localstack.dockerImage`      | no | `localstack/localstack:0.11.2` |
| `localstack.useSsl`           | no | `false`      |
| `localstack.services`         | no | `""`         |

You can customize which AWS services to enable/disable as follows:

| Property  | Value | Default Value |
| --------- | ----- | ------------- |
| `localstack.services` | `SQS, S3, SNS, DYNAMODB, DYNAMODBSTREAMS, KINESIS, IAM, LAMBDA, CLOUDWATCH, SECRETSMANAGER` | `""`|
| `localstack.s3.enabled`               | `false`   | `true`|
| `localstack.sqs.enabled`              | `true`    | `true`|
| `localstack.sns.enabled`              | `false`   | `true`|
| `localstack.dynamodb.enabled`         | `true`    | `true`|
| `localstack.dynamodbstreams.enabled`  | `false`   | `true`|
| `localstack.kinesis.enabled`          | `true`    | `true`|
| `localstack.iam.enabled`              | `false`   | `true`|
| `localstack.secretsmanager.enabled`   | `true`    | `true`|
| `localstack.lambda.enabled`           | `false`   | `true`|
| `localstack.cloudwatch.enabled`       | `true`    | `true`|

## Examples
* [Minimal SpringBoot application](https://github.com/sivalabs/localstack-spring-boot-starter/tree/master/examples/localstack-spring-boot-sample)
* [SpringCloud AWS application](https://github.com/sivalabs/localstack-spring-boot-starter/tree/master/examples/localstack-spring-cloud-aws-sample)

## Want to Contribute?

You can contribute to `localstack-spring-boot-starter` project in many ways:
* Use the starter and report if there are any bugs by [opening an issue](https://github.com/sivalabs/localstack-spring-boot-starter/issues/new)
* Add support for auto-configuration of [more services](https://github.com/localstack/localstack#overview)
* Add more [example applications](https://github.com/sivalabs/localstack-spring-boot-starter/tree/v0.0.2/examples)

## Credits
The implementation of `localstack-spring-boot-starter` is inspired by [testcontainers-spring-boot](https://github.com/testcontainers/testcontainers-spring-boot).
The `testcontainers-spring-boot` also provides localstack support, but it only spins up the docker container, and 
you will have to configure the beans like `AmazonS3`, `AmazonSQSAsync` etc by yourself.

## License
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://raw.githubusercontent.com/sivalabs/localstack-spring-boot-starter/master/LICENSE)

## Change Log

### Release 0.0.1
* Initial release with Auto Configuration for `S3` and `SQS` based on AWS Java SDK v1

### Release 0.0.2
* Added Auto Configuration for `SNS, DYNAMODB, DYNAMODBSTREAMS, KINESIS, IAM, LAMBDA, CLOUDWATCH` and `SECRETSMANAGER` based on AWS Java SDK v1


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
