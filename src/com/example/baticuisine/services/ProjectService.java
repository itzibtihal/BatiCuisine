package com.example.baticuisine.services;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Labor;
import com.example.baticuisine.domain.entities.Material;
import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.enums.ProjectStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Project save(Project project);

    Optional<Project> findById(UUID id);

    List<Project> findAll();

    Project update(Project project);

    boolean delete(Project project);

    boolean updateFields(UUID projectId, double marginProfit, double totalCost);

    void saveClientProject(Client client, Project project, Material material, Labor labor);

    List<Project> findByStatus(ProjectStatus status);
}
