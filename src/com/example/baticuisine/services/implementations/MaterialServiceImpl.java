package com.example.baticuisine.services.implementations;


import com.example.baticuisine.domain.entities.Material;
import com.example.baticuisine.repositories.implementations.ComponentRepository;
import com.example.baticuisine.repositories.implementations.MaterialRepository;

import com.example.baticuisine.services.MaterialService;
import com.example.baticuisine.validator.MaterialValidator;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialValidator materialValidator;
    private ComponentRepository componentRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialValidator materialValidator,ComponentRepository componentRepository) {
        this.materialRepository = materialRepository;
        this.materialValidator = materialValidator;
        this.componentRepository = componentRepository;
    }

    @Override
    public Material save(Material material) {
        materialValidator.validate(material);
        return materialRepository.save(material);
    }

    @Override
    public Optional<Material> findById(Material material) {
        materialValidator.validateId(material);
        return materialRepository.findById(material);
    }

    @Override
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Override
    public Material update(Material material) {
        materialValidator.validate(material);
        return materialRepository.update(material);
    }

    @Override
    public boolean delete(Material material) {
        materialValidator.validateId(material);
        return materialRepository.delete(material);
    }

    @Override
    public List<Material> findAllByProjectId(UUID projectId) {
        List<Material> materials = materialRepository.findAllByProjectId(projectId);
        if (materials.isEmpty()) {
            System.out.println("No materials found for the specified project ID.");
        }
        return materials;
    }
    private double getVatRateForMaterial(Material material) {
        return componentRepository.findTvaForComponent(material.getId());
    }


    public double calculateMaterial(Material material) {
        double total = material.getUnitCost() * material.getQuantity() * material.getCoefficientQuality();
        return total + material.getTransportCost();
    }

    public double calculateMaterialAfterVatRate(Material material) {
        return calculateMaterial(material);
    }

    private double applyVat(double cost, double vatRate) {
        return cost + (cost * vatRate / 100);
    }

    public double calculateMaterialBeforeVatRate(Material material) {
        double costBeforeVat = calculateMaterial(material);
        double vatRate = getVatRateForMaterial(material);
        return applyVat(costBeforeVat, vatRate);
    }
}

