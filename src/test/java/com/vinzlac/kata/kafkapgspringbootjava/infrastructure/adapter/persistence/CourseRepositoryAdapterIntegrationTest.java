package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.vinzlac.kata.kafkapgspringbootjava.application.Application;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.CourseEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.PartantEntity;
import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.repository.CourseJpaRepository;

@DataJpaTest
@Import(CourseRepositoryAdapter.class)
@ContextConfiguration(classes = Application.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
class CourseRepositoryAdapterIntegrationTest {

    @Autowired
    private CourseRepositoryAdapter courseRepositoryAdapter;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Test
    void save_ShouldSaveAndReturnDomainRace() {
        // Given
        LocalDate today = LocalDate.now();
        Course course = Course.builder()
                .nom("Course Test")
                .numero(1)
                .date(today)
                .build();
        
        course.addPartant(Partant.builder().nom("Cheval1").numero(1).build());
        course.addPartant(Partant.builder().nom("Cheval2").numero(2).build());
        course.addPartant(Partant.builder().nom("Cheval3").numero(3).build());
        
        // When
        Course savedCourse = courseRepositoryAdapter.save(course);
        
        // Then
        assertNotNull(savedCourse.getId());
        assertEquals("Course Test", savedCourse.getNom());
        assertEquals(1, savedCourse.getNumero());
        assertEquals(today, savedCourse.getDate());
        assertEquals(3, savedCourse.getPartants().size());
        
        // Vérifier que la course est bien enregistrée en base
        Optional<CourseEntity> foundEntity = courseJpaRepository.findById(savedCourse.getId());
        assertTrue(foundEntity.isPresent());
        assertEquals("Course Test", foundEntity.get().getNom());
        assertEquals(3, foundEntity.get().getPartants().size());
    }
    
    @Test
    void findById_ExistingRace_ShouldReturnRace() {
        // Given
        CourseEntity courseEntity = prepareRaceWithRunners();
        CourseEntity savedEntity = courseJpaRepository.save(courseEntity);
        
        // When
        Optional<Course> result = courseRepositoryAdapter.findById(savedEntity.getId());
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(savedEntity.getId(), result.get().getId());
        assertEquals(savedEntity.getNom(), result.get().getNom());
        assertEquals(savedEntity.getNumero(), result.get().getNumero());
        assertEquals(savedEntity.getDate(), result.get().getDate());
        assertEquals(savedEntity.getPartants().size(), result.get().getPartants().size());
    }
    
    @Test
    void findByDateAndNumber_ExistingRace_ShouldReturnRace() {
        // Given
        LocalDate today = LocalDate.now();
        CourseEntity courseEntity = prepareRaceWithRunners();
        courseEntity.setDate(today);
        courseEntity.setNumero(5);
        courseJpaRepository.save(courseEntity);
        
        // When
        Optional<Course> result = courseRepositoryAdapter.findByDateAndNumero(today, 5);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(courseEntity.getNom(), result.get().getNom());
        assertEquals(5, result.get().getNumero());
        assertEquals(today, result.get().getDate());
    }
    
    @Test
    void findByDate_WithExistingRaces_ShouldReturnRaces() {
        // Given
        LocalDate today = LocalDate.now();
        
        CourseEntity course1 = prepareRaceWithRunners();
        course1.setDate(today);
        course1.setNumero(1);
        
        CourseEntity course2 = prepareRaceWithRunners();
        course2.setDate(today);
        course2.setNumero(2);
        
        courseJpaRepository.saveAll(List.of(course1, course2));
        
        // When
        List<Course> result = courseRepositoryAdapter.findByDate(today);
        
        // Then
        assertEquals(2, result.size());
    }
    
    private CourseEntity prepareRaceWithRunners() {
        CourseEntity courseEntity = CourseEntity.builder()
                .nom("Course Test")
                .numero(1)
                .date(LocalDate.now())
                .build();
        
        courseEntity.addPartant(PartantEntity.builder().nom("Cheval1").numero(1).build());
        courseEntity.addPartant(PartantEntity.builder().nom("Cheval2").numero(2).build());
        courseEntity.addPartant(PartantEntity.builder().nom("Cheval3").numero(3).build());
        
        return courseEntity;
    }
} 