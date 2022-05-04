package com.sivalabs.customlocalstackversion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.localstack.LocalStackContainer;

@SpringBootTest
class CustomLocalstackVersionApplicationTests {

	@Autowired
	private LocalStackContainer localStackContainer;

	@Test
	void shouldUseLatestLocalStackDockerImage() {
		String currentImageName = localStackContainer.getDockerImageName();
		String expectedDockerImageName = "localstack/localstack:latest";
		assertThat(currentImageName).isEqualTo(expectedDockerImageName);
	}

}
