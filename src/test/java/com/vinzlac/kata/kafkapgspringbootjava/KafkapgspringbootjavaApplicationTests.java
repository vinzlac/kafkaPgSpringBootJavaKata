package com.vinzlac.kata.kafkapgspringbootjava;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, 
               topics = {"course-created-test"},
               bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@TestPropertySource(properties = {
        "kafka.topics.course-created=course-created-test"
})
class KafkapgspringbootjavaApplicationTests {

	@Test
	void contextLoads() {
	}

}
