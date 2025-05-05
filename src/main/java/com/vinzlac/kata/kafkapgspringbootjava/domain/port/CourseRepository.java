package com.vinzlac.kata.kafkapgspringbootjava.domain.port;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course save(Course course);
    Optional<Course> findById(Long id);
    Optional<Course> findByDateAndNumero(LocalDate date, int numero);
    List<Course> findByDate(LocalDate date);
} 