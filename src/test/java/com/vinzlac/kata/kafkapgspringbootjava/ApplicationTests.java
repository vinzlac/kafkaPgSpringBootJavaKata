package com.vinzlac.kata.kafkapgspringbootjava;

import com.vinzlac.kata.kafkapgspringbootjava.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = Application.class)
@EmbeddedKafka(partitions = 1, topics = {"course-created-test"})
@TestPropertySource(properties = {
    "kafka.topics.course-created=course-created-test",
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@ActiveProfiles("test")
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
