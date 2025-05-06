package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartantEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private int numero;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
} 