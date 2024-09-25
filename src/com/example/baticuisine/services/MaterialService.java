package com.example.baticuisine.services;

import com.example.baticuisine.domain.entities.Material;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialService {
    Material save(Material material);

    Optional<Material> findById(Material material);

    List<Material> findAll();

    Material update(Material material);

    boolean delete(Material material);

    List<Material> findAllByProjectId(UUID projectId);
}
