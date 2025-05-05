package com.vinzlac.kata.kafkapgspringbootjava.application.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Données invalides", 
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::getFieldErrorMessage)
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Erreur de validation", 
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Une erreur inattendue s'est produite", 
                List.of(exception.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private String getFieldErrorMessage(FieldError fieldError) {
        return String.format("Le champ '%s' %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
} 