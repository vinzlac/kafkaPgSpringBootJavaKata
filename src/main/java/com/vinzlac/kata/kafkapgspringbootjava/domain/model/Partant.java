package com.vinzlac.kata.kafkapgspringbootjava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partant {
    private Long id;
    private String nom;
    private int numero;
} 