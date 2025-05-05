package com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.repository;

import com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByDateAndNumero(LocalDate date, int numero);
    List<CourseEntity> findByDate(LocalDate date);
} 