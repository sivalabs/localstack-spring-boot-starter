While using the `localstack-spring-boot-starter`, chances are testcontainers will be using default localstack version unless you have manually pulled the latest version.

This example shows how to use custom/latest version in case if you need it, for eg, in CI environment.

The `localstack-spring-boot-starter` library uses [Testcontainers Java library](https://github.com/testcontainers/testcontainers-java) and its [LocalStack Module](https://www.testcontainers.org/modules/localstack/) to achieve its purpose.
Thus, you need to tell testcontainers which docker image or version of localstack to use.

As per the official documentation, [there are four ways to do so](https://www.testcontainers.org/features/image_name_substitution/).
The easiest way is to override image name via configuration which can be as following.

1. Create a property file on the classpath named `testcontainers.properties`. For eg, `src/test/resources/testcontainers.properties`.
2. Specify the image name in above file. For eg,
    ```
    localstack.container.image=localstack/localstack:latest
    #localstack.container.image=localstack/localstack:0.14.2
    ```
That's it!

However, note that providing custom docker image in this way is discouraged and deprecated. If it is removed in future, you will need to consider one of the other ways.