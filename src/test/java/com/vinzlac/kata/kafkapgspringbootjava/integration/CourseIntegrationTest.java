package com.vinzlac.kata.kafkapgspringbootjava.integration;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import com.vinzlac.kata.kafkapgspringbootjava.domain.service.CourseService;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging.CourseCreatedEvent;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging.CourseEventPublisherAdapter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"courses"})
@ActiveProfiles("test")
class CourseIntegrationTest {
    
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseEventPublisherAdapter publisherAdapter;
    
    private Consumer<String, CourseCreatedEvent> consumer;
    
    @BeforeEach
    void setUp() {
        consumer = configureConsumer();
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void publishCourseCreated_ShouldSendMessageKafka() throws Exception {
        // Créer et sauvegarder une course
        Course course = Course.builder()
                .nom("Course Kafka Test")
                .numero(1)
                .date(LocalDate.now())
                .build();
        
        course.addPartant(Partant.builder().nom("Cheval1").numero(1).build());
        course.addPartant(Partant.builder().nom("Cheval2").numero(2).build());
        course.addPartant(Partant.builder().nom("Cheval3").numero(3).build());
        
        // Quand on crée une course, l'événement devrait être publié
        Course savedCourse = courseService.createCourse(course);
        
        // Consommer et vérifier le message
        ConsumerRecord<String, CourseCreatedEvent> record = KafkaTestUtils.getSingleRecord(
                consumer, "course-created-test", Duration.ofMillis(5000));
        
        assertNotNull(record);
        assertEquals(String.valueOf(savedCourse.getId()), record.key());
        assertEquals(savedCourse.getNom(), record.value().getNom());
        assertEquals(savedCourse.getNumero(), record.value().getNumero());
        assertEquals(3, record.value().getPartants().size());
    }
    
    private Consumer<String, CourseCreatedEvent> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        Consumer<String, CourseCreatedEvent> consumer = new DefaultKafkaConsumerFactory<>(
                consumerProps, new StringDeserializer(),
                new JsonDeserializer<>(CourseCreatedEvent.class, false)).createConsumer();
                
        consumer.subscribe(Collections.singletonList("course-created-test"));
        return consumer;
    }
} 