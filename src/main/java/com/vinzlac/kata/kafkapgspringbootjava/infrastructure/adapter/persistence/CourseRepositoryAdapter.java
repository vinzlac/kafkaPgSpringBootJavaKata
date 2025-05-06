package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseRepository;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.CourseEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.PartantEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.repository.CourseJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourseRepositoryAdapter implements CourseRepository {
    
    private final CourseJpaRepository courseJpaRepository;
    
    @Override
    public Course save(Course course) {
        log.info("Saving course: name={}, numero={}, date={}, partants={}", 
                course.getNom(), course.getNumero(), course.getDate(), course.getPartants().size());
        CourseEntity courseEntity = toEntity(course);
        CourseEntity savedEntity = courseJpaRepository.save(courseEntity);
        Course savedCourse = toDomain(savedEntity);
        log.debug("Course saved successfully with ID: {}", savedCourse.getId());
        return savedCourse;
    }
    
    @Override
    public Optional<Course> findById(Long id) {
        log.info("Finding course by ID: {}", id);
        Optional<Course> result = courseJpaRepository.findById(id).map(this::toDomain);
        log.debug("Course found by ID {}: {}", id, result.isPresent());
        return result;
    }
    
    @Override
    public Optional<Course> findByDateAndNumero(LocalDate date, int numero) {
        log.info("Finding course by date: {} and numero: {}", date, numero);
        Optional<Course> result = courseJpaRepository.findByDateAndNumero(date, numero).map(this::toDomain);
        log.debug("Course found by date and numero: {}", result.isPresent());
        return result;
    }
    
    @Override
    public List<Course> findByDate(LocalDate date) {
        log.info("Finding all courses for date: {}", date);
        List<Course> courses = courseJpaRepository.findByDate(date).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        log.debug("Found {} courses for date: {}", courses.size(), date);
        return courses;
    }
    
    private CourseEntity toEntity(Course course) {
        CourseEntity courseEntity = CourseEntity.builder()
                .id(course.getId())
                .nom(course.getNom())
                .numero(course.getNumero())
                .date(course.getDate())
                .build();
        
        course.getPartants().forEach(partant -> {
            PartantEntity partantEntity = PartantEntity.builder()
                    .id(partant.getId())
                    .nom(partant.getNom())
                    .numero(partant.getNumero())
                    .build();
            
            courseEntity.addPartant(partantEntity);
        });
        
        return courseEntity;
    }
    
    private Course toDomain(CourseEntity entity) {
        List<Partant> partants = entity.getPartants().stream()
                .map(partantEntity -> Partant.builder()
                        .id(partantEntity.getId())
                        .nom(partantEntity.getNom())
                        .numero(partantEntity.getNumero())
                        .build())
                .collect(Collectors.toList());
        
        return Course.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .numero(entity.getNumero())
                .date(entity.getDate())
                .partants(partants)
                .build();
    }
} 