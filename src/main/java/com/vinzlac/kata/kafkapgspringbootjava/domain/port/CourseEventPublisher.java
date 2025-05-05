package com.vinzlac.kata.kafkapgspringbootjava.domain.port;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;

public interface CourseEventPublisher {
    void publishCourseCreated(Course course);
} 