package com.vinzlac.kata.kafkapgspringbootjava.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinzlac.kata.kafkapgspringbootjava.application.service.CourseApplicationService;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseApplicationService courseApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRace_ValidRace_ShouldReturnCreated() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        CourseRequest request = new CourseRequest(
                "Course Test",
                1,  // Fixed: This is numero
                today, // Fixed: This is date
                List.of(
                        new PartantRequest("Cheval1", 1),
                        new PartantRequest("Cheval2", 2),
                        new PartantRequest("Cheval3", 3)
                )
        );

        // Create a Course that matches our domain model
        Course course = Course.builder()
                .id(1L)
                .nom("Course Test")
                .numero(1)
                .date(today)
                .build();
        
        // Add partants using the correct method
        Partant partant1 = Partant.builder().nom("Cheval1").numero(1).build();
        Partant partant2 = Partant.builder().nom("Cheval2").numero(2).build();
        Partant partant3 = Partant.builder().nom("Cheval3").numero(3).build();
        
        course.addPartant(partant1);
        course.addPartant(partant2);
        course.addPartant(partant3);

        when(courseApplicationService.createCourse(any(Course.class))).thenReturn(course);

        // When & Then
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Course Test"))
                .andExpect(jsonPath("$.partants.length()").value(3));
    }
} 