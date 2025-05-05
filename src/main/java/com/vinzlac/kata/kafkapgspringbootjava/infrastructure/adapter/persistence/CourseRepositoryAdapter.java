package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseRepository;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.CourseEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.PartantEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.repository.CourseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseRepositoryAdapter implements CourseRepository {
    
    private final CourseJpaRepository courseJpaRepository;
    
    @Override
    public Course save(Course course) {
        CourseEntity courseEntity = toEntity(course);
        CourseEntity savedEntity = courseJpaRepository.save(courseEntity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Course> findById(Long id) {
        return courseJpaRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public Optional<Course> findByDateAndNumero(LocalDate date, int numero) {
        return courseJpaRepository.findByDateAndNumero(date, numero).map(this::toDomain);
    }
    
    @Override
    public List<Course> findByDate(LocalDate date) {
        return courseJpaRepository.findByDate(date).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
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