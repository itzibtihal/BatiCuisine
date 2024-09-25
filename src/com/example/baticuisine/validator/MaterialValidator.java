package com.example.baticuisine.validator;

import com.example.baticuisine.domain.entities.Material;

public class MaterialValidator {

    public void validate(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (material.getUnitCost() < 0) {
            throw new IllegalArgumentException("Unit cost cannot be negative");
        }
        if (material.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (material.getTransportCost() < 0) {
            throw new IllegalArgumentException("Transport cost cannot be negative");
        }
        if (material.getCoefficientQuality() < 0) {
            throw new IllegalArgumentException("Coefficient quality cannot be negative");
        }
        if (material.getComponent() == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
    }

    public void validateId(Material material) {
        if (material == null || material.getId() == null) {
            throw new IllegalArgumentException("Material ID cannot be null");
        }
    }

}
