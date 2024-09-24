package com.example.baticuisine.validator;

import com.example.baticuisine.domain.entities.Labor;

import java.util.UUID;

public class LaborValidator {
    public void validateLabor(Labor labor) throws IllegalArgumentException {
        if (labor == null) {
            throw new IllegalArgumentException("Labor cannot be null");
        }
        if (labor.getName() == null || labor.getName().isEmpty()) {
            throw new IllegalArgumentException("Labor name cannot be empty");
        }
        if (labor.getHourlyRate() <= 0) {
            throw new IllegalArgumentException("Hourly rate must be positive");
        }
        if (labor.getWorkHours() < 0) {
            throw new IllegalArgumentException("Hours worked cannot be negative");
        }
    }


    public void validateId(UUID id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Labor ID cannot be null");
        }
    }
}
