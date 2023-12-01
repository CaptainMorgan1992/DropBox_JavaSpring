package me.code.individual_assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(basePackages = "me.code.individual_assignment")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
