package com.vinzlac.kata.kafkapgspringbootjava.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.vinzlac.kata.kafkapgspringbootjava")
@EntityScan("com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.entity")
@EnableJpaRepositories("com.vinzlac.kata.kafkapgspringbootjava.infrastructure.adapter.persistence.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

} 