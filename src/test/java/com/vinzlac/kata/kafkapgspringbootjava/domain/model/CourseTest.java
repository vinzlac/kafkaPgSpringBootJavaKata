package com.vinzlac.kata.kafkapgspringbootjava.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void addRunner_ShouldAddRunner() {
        // Given
        Course course = new Course();
        Partant partant = Partant.builder().nom("Cheval1").numero(1).build();
        
        // When
        course.addPartant(partant);
        
        // Then
        assertEquals(1, course.getPartants().size());
        assertEquals(partant, course.getPartants().get(0));
    }
    
    @Test
    void addRunner_WithExistingNumber_ShouldThrowException() {
        // Given
        Course course = new Course();
        course.addPartant(Partant.builder().nom("Cheval1").numero(1).build());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
                course.addPartant(Partant.builder().nom("Cheval2").numero(1).build()));
    }
    
    @Test
    void isValid_LessThanThreeRunners_ShouldReturnFalse() {
        // Given
        Course course = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(LocalDate.now())
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(1).build(),
                        Partant.builder().nom("Cheval2").numero(2).build()
                ))
                .build();
        
        // When & Then
        assertFalse(course.isValid());
    }
    
    @Test
    void isValid_NonContiguousRunnerNumbers_ShouldReturnFalse() {
        // Given
        Course course = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(LocalDate.now())
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(1).build(),
                        Partant.builder().nom("Cheval2").numero(2).build(),
                        Partant.builder().nom("Cheval3").numero(4).build()
                ))
                .build();
        
        // When & Then
        assertFalse(course.isValid());
    }
    
    @Test
    void isValid_RunnerNumbersNotStartingWithOne_ShouldReturnFalse() {
        // Given
        Course course = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(LocalDate.now())
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(2).build(),
                        Partant.builder().nom("Cheval2").numero(3).build(),
                        Partant.builder().nom("Cheval3").numero(4).build()
                ))
                .build();
        
        // When & Then
        assertFalse(course.isValid());
    }
    
    @Test
    void isValid_ValidRace_ShouldReturnTrue() {
        // Given
        Course course = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(LocalDate.now())
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(1).build(),
                        Partant.builder().nom("Cheval2").numero(2).build(),
                        Partant.builder().nom("Cheval3").numero(3).build()
                ))
                .build();
        
        // When & Then
        assertTrue(course.isValid());
    }
} 