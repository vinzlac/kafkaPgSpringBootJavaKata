package com.vinzlac.kata.kafkapgspringbootjava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;
    private String nom;
    private int numero;
    private LocalDate date;
    
    @Builder.Default
    private List<Partant> partants = new ArrayList<>();
    
    public void addPartant(Partant partant) {
        if (partants == null) {
            partants = new ArrayList<>();
        }
        
        // Vérifier si le numéro est déjà utilisé
        if (partants.stream().anyMatch(p -> p.getNumero() == partant.getNumero())) {
            throw new IllegalArgumentException("Un partant avec le numéro " + partant.getNumero() + " existe déjà dans cette course");
        }
        
        partants.add(partant);
    }
    
    public boolean isValid() {
        // Une course doit avoir au moins 3 partants
        if (partants == null || partants.size() < 3) {
            return false;
        }
        
        // Vérifier que les numéros des partants commencent à 1 et sont continus
        var numerosPartants = partants.stream()
                .map(Partant::getNumero)
                .sorted()
                .toList();
        
        // Vérifier si les numéros commencent à 1 et sont continus
        for (int i = 0; i < numerosPartants.size(); i++) {
            if (numerosPartants.get(i) != i + 1) {
                return false;
            }
        }
        
        return true;
    }
} 