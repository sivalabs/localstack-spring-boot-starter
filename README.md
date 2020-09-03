# localstack-spring-boot-starter

This is a SpringBoot starter for LocalStack auto-configuration.

## How to use?

### Add the dependency

**Maven** 

```
<dependency>
    <groupId>io.github.sivalabs</groupId>
    <artifactId>localstack-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

**Gradle**

`compile group: 'io.github.sivalabs', name: 'localstack-spring-boot-starter', version: '0.0.1'`

### Configuration

The following configuration properties are available to customize the default behaviour.

| Property | Required | Default Value |
| --- | --- | --- |
| `localstack.enabled` | no | `true` |
| `localstack.edgePort` | no | `4566` |
| `localstack.defaultRegion` | no | `us-east-1` |
| `localstack.hostname` | no | `localhost` |
| `localstack.hostnameExternal` | no | `localhost` |
| `localstack.dockerImage` | no | `localstack/localstack:0.10.8` |
| `localstack.useSsl` | no | `false` |
| `localstack.services` | no | `""` |

You can customize which AWS services to enable/disable as follows:

| Property  | Value | Default Value |
| --- | --- | --- |
| `localstack.services` | `S3,SQS,DYNAMODB,SNS` | `""`|
| `localstack.s3.enabled` | `false` | `true`|
| `localstack.sqs.enabled` | `true` | `true`|


## Developer Notes

Procedure for deploying to Maven Central https://central.sonatype.org/pages/apache-maven.html

Set version to SNAPSHOT (ex: 1.0.0-SNAPSHOT)

Deploy SNAPSHOT version to https://oss.sonatype.org/content/repositories/snapshots/

`localstack-spring-boot-starter> ./mvnw clean deploy -Prelease`

Deploy release version to Maven Central

```
localstack-spring-boot-starter> ./mvnw release:clean release:prepare -Prelease
localstack-spring-boot-starter> ./mvnw release:perform -Prelease
```
