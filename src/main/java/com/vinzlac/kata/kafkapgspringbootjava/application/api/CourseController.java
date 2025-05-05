package com.vinzlac.kata.kafkapgspringbootjava.application.api;

import com.vinzlac.kata.kafkapgspringbootjava.application.service.CourseApplicationService;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Course;
import com.vinzlac.kata.kafkapgspringbootjava.domain.model.Partant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseApplicationService courseApplicationService;
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle course avec ses partants")
    @ApiResponse(responseCode = "201", description = "Course créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        Course course = Course.builder()
                .nom(courseRequest.nom())
                .numero(courseRequest.numero())
                .date(courseRequest.date())
                .build();
        
        // Ajouter les partants
        courseRequest.partants().forEach(partantRequest -> {
            Partant partant = Partant.builder()
                    .nom(partantRequest.nom())
                    .numero(partantRequest.numero())
                    .build();
            course.ajouterPartant(partant);
        });
        
        Course savedCourse = courseApplicationService.createCourse(course);
        return new ResponseEntity<>(mapToResponse(savedCourse), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une course par son ID")
    @ApiResponse(responseCode = "200", description = "Course trouvée")
    @ApiResponse(responseCode = "404", description = "Course non trouvée")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return courseApplicationService.getCourseById(id)
                .map(course -> ResponseEntity.ok(mapToResponse(course)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les courses pour une date donnée")
    public ResponseEntity<List<CourseResponse>> getCoursesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Course> courses = courseApplicationService.getCoursesByDate(date);
        List<CourseResponse> responses = courses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    private CourseResponse mapToResponse(Course course) {
        List<PartantResponse> partantResponses = course.getPartants().stream()
                .map(partant -> new PartantResponse(
                        partant.getId(),
                        partant.getNom(),
                        partant.getNumero()))
                .collect(Collectors.toList());
        
        return new CourseResponse(
                course.getId(),
                course.getNom(),
                course.getNumero(),
                course.getDate(),
                partantResponses
        );
    }
} 