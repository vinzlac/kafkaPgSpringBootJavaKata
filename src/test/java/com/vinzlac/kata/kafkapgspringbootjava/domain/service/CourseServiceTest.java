package com.vinzlac.kata.kafkapgspringbootjava.domain.service;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseEventPublisher;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseEventPublisher courseEventPublisher;

    @InjectMocks
    private CourseService courseService;

    private Course validCourse;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        validCourse = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(today)
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(1).build(),
                        Partant.builder().nom("Cheval2").numero(2).build(),
                        Partant.builder().nom("Cheval3").numero(3).build()
                ))
                .build();
    }

    @Test
    void createRace_ValidRace_ShouldCreateRace() {
        // Given
        when(courseRepository.findByDateAndNumero(today, 1)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(validCourse);
        
        // When
        Course result = courseService.createCourse(validCourse);
        
        // Then
        assertEquals(validCourse, result);
        verify(courseRepository).save(validCourse);
        verify(courseEventPublisher).publishCourseCreated(validCourse);
    }
    
    @Test
    void createRace_ExistingRace_ShouldThrowException() {
        // Given
        when(courseRepository.findByDateAndNumero(today, 1)).thenReturn(Optional.of(validCourse));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(validCourse));
        verify(courseRepository, never()).save(any(Course.class));
        verify(courseEventPublisher, never()).publishCourseCreated(any(Course.class));
    }
    
    @Test
    void createRace_InvalidRace_ShouldThrowException() {
        // Given
        Course invalidCourse = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(today)
                .partants(List.of(
                        Partant.builder().nom("Cheval1").numero(1).build()
                ))
                .build();
        
        when(courseRepository.findByDateAndNumero(today, 1)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(invalidCourse));
        verify(courseRepository, never()).save(any(Course.class));
        verify(courseEventPublisher, never()).publishCourseCreated(any(Course.class));
    }
    
    @Test
    void getRaceById_ExistingRace_ShouldReturnRace() {
        // Given
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.of(validCourse));
        
        // When
        Optional<Course> result = courseService.getCourseById(id);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(validCourse, result.get());
    }
    
    @Test
    void getRaceById_NonExistingRace_ShouldReturnEmpty() {
        // Given
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        
        // When
        Optional<Course> result = courseService.getCourseById(id);
        
        // Then
        assertTrue(result.isEmpty());
    }
    
    @Test
    void getRacesByDate_ShouldReturnRaces() {
        // Given
        when(courseRepository.findByDate(today)).thenReturn(List.of(validCourse));
        
        // When
        List<Course> result = courseService.getCoursesByDate(today);
        
        // Then
        assertEquals(1, result.size());
        assertEquals(validCourse, result.get(0));
    }
} 