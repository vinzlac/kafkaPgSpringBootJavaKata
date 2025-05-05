package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PartantRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        
        @NotNull(message = "Le numéro est obligatoire")
        @Min(value = 1, message = "Le numéro doit être positif")
        Integer numero
) {} 