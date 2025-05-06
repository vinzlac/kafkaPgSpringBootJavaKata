package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourseEventPublisherAdapter implements CourseEventPublisher {
    
    private final KafkaTemplate<String, CourseCreatedEvent> kafkaTemplate;
    
    @Value("${kafka.topics.course-created}")
    private String courseTopic;
    
    @Override
    public void publishCourseCreated(Course course) {
        log.info("Publishing course created event for course ID: {}, name: {}, to topic: {}", 
                course.getId(), course.getNom(), courseTopic);
        
        CourseCreatedEvent event = mapToCourseCreatedEvent(course);
        log.debug("Event payload: {}", event);
        
        kafkaTemplate.send(courseTopic, String.valueOf(course.getId()), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Message successfully sent for course ID: {}, topic: {}, partition: {}, offset: {}", 
                                course.getId(), 
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message for course ID: {}, error: {}", 
                                course.getId(), ex.getMessage(), ex);
                    }
                });
    }
    
    private CourseCreatedEvent mapToCourseCreatedEvent(Course course) {
        log.debug("Mapping course to event: courseId={}, partants={}", course.getId(), course.getPartants().size());
        return CourseCreatedEvent.builder()
                .id(course.getId())
                .nom(course.getNom())
                .numero(course.getNumero())
                .date(course.getDate())
                .partants(course.getPartants().stream()
                        .map(partant -> new PartantDto(
                                partant.getId(),
                                partant.getNom(),
                                partant.getNumero()))
                        .toList())
                .build();
    }
} 