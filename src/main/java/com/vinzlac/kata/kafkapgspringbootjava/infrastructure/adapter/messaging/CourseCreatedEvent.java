package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreatedEvent {
    private Long id;
    private String nom;
    private int numero;
    private LocalDate date;
    private List<PartantDto> partants;
} 