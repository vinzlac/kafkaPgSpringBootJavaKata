package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vinzlac.kata.kafkapgspringbootjava.application.service.CourseService;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private ObjectMapper objectMapper;
    private Course course;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        today = LocalDate.now();
        course = Course.builder()
                .id(1L)
                .nom("Course Test")
                .numero(1)
                .date(today)
                .partants(List.of(
                        Partant.builder().id(1L).nom("Cheval1").numero(1).build(),
                        Partant.builder().id(2L).nom("Cheval2").numero(2).build(),
                        Partant.builder().id(3L).nom("Cheval3").numero(3).build()
                ))
                .build();
    }

    @Test
    void createRace_ValidRequest_ShouldReturnCreatedRace() throws Exception {
        // Given
        CourseRequest request = new CourseRequest(
                "Course Test",
                1,
                today,
                List.of(
                        new PartantRequest("Cheval1", 1),
                        new PartantRequest("Cheval2", 2),
                        new PartantRequest("Cheval3", 3)
                )
        );
        
        when(courseService.creerCourse(any(Course.class))).thenReturn(course);
        
        // When & Then
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Course Test"))
                .andExpect(jsonPath("$.numero").value(1))
                .andExpect(jsonPath("$.partants.length()").value(3));
    }
    
    @Test
    void createRace_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        CourseRequest request = new CourseRequest(
                "",  // Nom vide
                1,
                today,
                List.of(new PartantRequest("Cheval1", 1))  // Moins de 3 partants
        );
        
        // When & Then
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getRaceById_ExistingRace_ShouldReturnRace() throws Exception {
        // Given
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        
        // When & Then
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Course Test"))
                .andExpect(jsonPath("$.numero").value(1))
                .andExpect(jsonPath("$.partants.length()").value(3));
    }
    
    @Test
    void getRaceById_NonExistingRace_ShouldReturnNotFound() throws Exception {
        // Given
        when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getRacesByDate_ShouldReturnRaces() throws Exception {
        // Given
        when(courseService.getCoursesByDate(today)).thenReturn(List.of(course));
        
        // When & Then
        mockMvc.perform(get("/api/courses")
                .param("date", today.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Course Test"))
                .andExpect(jsonPath("$[0].numero").value(1))
                .andExpect(jsonPath("$[0].partants.length()").value(3));
    }
} 