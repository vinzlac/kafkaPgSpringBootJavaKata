package com.vinzlac.kata.kafkapgspringbootjava.integration;

import com.vinzlac.kata.kafkapgspringbootjava.application.service.CourseService;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging.CourseCreatedEvent;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging.CourseEventPublisherAdapter;

import java.time.LocalDate;
import java.time.Duration;

import java.util.Map;
import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, 
               topics = {"course-created-test"},
               bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true",
        "kafka.topics.course-created=course-created-test"
})
class CourseIntegrationTest {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private KafkaTemplate<String, CourseCreatedEvent> kafkaTemplate;
    
    @SpyBean
    private CourseEventPublisherAdapter publisherAdapter;
    
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void publishCourseCreated_DevraitEnvoyerMessageKafka() throws Exception {
        // Créer un consumer de test pour vérifier la réception
        Consumer<String, CourseCreatedEvent> consumer = configureConsumer();
        
        // Créer et sauvegarder une course
        Course course = Course.builder()
                .nom("Course Kafka Test")
                .numero(1)
                .date(LocalDate.now())
                .build();
        
        course.ajouterPartant(Partant.builder().nom("Cheval1").numero(1).build());
        course.ajouterPartant(Partant.builder().nom("Cheval2").numero(2).build());
        course.ajouterPartant(Partant.builder().nom("Cheval3").numero(3).build());
        
        // Quand on crée une course, l'événement devrait être publié
        Course savedCourse = courseService.creerCourse(course);
        
        // Vérifier que la méthode publishCourseCreated a été appelée
        verify(publisherAdapter, times(1)).publishCourseCreated(any(Course.class));
        
        // Consommer et vérifier le message
        ConsumerRecord<String, CourseCreatedEvent> record = KafkaTestUtils.getSingleRecord(
                consumer, "course-created-test", Duration.ofMillis(5000));
        
        assertNotNull(record);
        assertEquals(String.valueOf(savedCourse.getId()), record.key());
        assertEquals(savedCourse.getNom(), record.value().getNom());
        assertEquals(savedCourse.getNumero(), record.value().getNumero());
        assertEquals(3, record.value().getPartants().size());
        
        consumer.close();
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