package com.vinzlac.kata.kafkapgspringbootjava.application.api;

import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details
) {
} 