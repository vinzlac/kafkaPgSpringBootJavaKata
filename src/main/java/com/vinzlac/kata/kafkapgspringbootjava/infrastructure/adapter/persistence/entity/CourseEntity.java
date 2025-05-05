package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses",
       uniqueConstraints = @UniqueConstraint(columnNames = {"date", "numero"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private int numero;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PartantEntity> partants = new ArrayList<>();
    
    public void addPartant(PartantEntity partant) {
        partants.add(partant);
        partant.setCourse(this);
    }
} 