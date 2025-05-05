package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.rest;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details
) {} 