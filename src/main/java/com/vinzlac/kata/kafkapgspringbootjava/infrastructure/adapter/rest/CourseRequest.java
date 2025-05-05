package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CourseRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        
        @NotNull(message = "Le numéro est obligatoire")
        @Min(value = 1, message = "Le numéro doit être positif")
        Integer numero,
        
        @NotNull(message = "La date est obligatoire")
        LocalDate date,
        
        @NotEmpty(message = "La course doit avoir au moins 3 partants")
        List<@Valid PartantRequest> partants
) {} 