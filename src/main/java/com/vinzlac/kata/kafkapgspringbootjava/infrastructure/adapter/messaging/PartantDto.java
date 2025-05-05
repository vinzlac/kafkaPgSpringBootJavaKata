package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartantDto {
    private Long id;
    private String nom;
    private int numero;
} 