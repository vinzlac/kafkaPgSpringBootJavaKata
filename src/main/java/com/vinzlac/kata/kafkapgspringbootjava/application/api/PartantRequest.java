package com.vinzlac.kata.kafkapgspringbootjava.application.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PartantRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        
        @Min(value = 1, message = "Le numéro doit être supérieur à zéro")
        int numero
) {
} 