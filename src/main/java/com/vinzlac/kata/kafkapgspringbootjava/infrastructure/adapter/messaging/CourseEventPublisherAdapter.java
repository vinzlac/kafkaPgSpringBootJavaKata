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
        CourseCreatedEvent event = mapToCourseCreatedEvent(course);
        kafkaTemplate.send(courseTopic, String.valueOf(course.getId()), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message envoyé avec succès pour la course {} : {}", 
                                course.getId(), result.getRecordMetadata());
                    } else {
                        log.error("Impossible d'envoyer le message pour la course {}", 
                                course.getId(), ex);
                    }
                });
    }
    
    private CourseCreatedEvent mapToCourseCreatedEvent(Course course) {
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