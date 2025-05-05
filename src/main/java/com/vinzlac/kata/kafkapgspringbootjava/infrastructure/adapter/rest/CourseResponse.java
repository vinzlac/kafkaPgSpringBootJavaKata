package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.rest;

import java.time.LocalDate;
import java.util.List;

public record CourseResponse(
        Long id,
        String nom,
        int numero,
        LocalDate date,
        List<PartantResponse> partants
) {} 