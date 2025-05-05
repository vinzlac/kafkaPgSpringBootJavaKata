package com.vinzlac.kata.kafkapgspringbootjava.application.service;

import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseEventPublisher;
import com.vinzlac.kata.kafkapgspringbootjava.domain.port.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final CourseEventPublisher courseEventPublisher;
    
    @Transactional
    public Course creerCourse(Course course) {
        // Vérifier si une course avec la même date et le même numéro existe déjà
        Optional<Course> existingCourse = courseRepository.findByDateAndNumero(course.getDate(), course.getNumero());
        if (existingCourse.isPresent()) {
            throw new IllegalArgumentException("Une course avec le numéro " + course.getNumero() + 
                    " existe déjà pour la date " + course.getDate());
        }
        
        // Vérifier que la course est valide (au moins 3 partants avec numéros continus commençant à 1)
        if (!course.estValide()) {
            throw new IllegalArgumentException("La course doit avoir au moins 3 partants avec des numéros continus commençant à 1");
        }
        
        // Sauvegarder la course
        Course savedCourse = courseRepository.save(course);
        
        // Publier l'événement de création de course
        courseEventPublisher.publishCourseCreated(savedCourse);
        
        return savedCourse;
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public List<Course> getCoursesByDate(LocalDate date) {
        return courseRepository.findByDate(date);
    }
} 