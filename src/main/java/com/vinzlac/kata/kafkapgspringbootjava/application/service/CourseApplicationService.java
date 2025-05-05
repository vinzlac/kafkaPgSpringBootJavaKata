package com.vinzlac.kata.kafkapgspringbootjava.application.service;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseApplicationService {
    
    private final CourseService courseService;
    
    public Course createCourse(Course course) {
        return courseService.createCourse(course);
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseService.getCourseById(id);
    }
    
    public List<Course> getCoursesByDate(LocalDate date) {
        return courseService.getCoursesByDate(date);
    }
} 