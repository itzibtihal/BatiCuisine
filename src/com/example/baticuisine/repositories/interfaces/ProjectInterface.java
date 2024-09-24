package com.example.baticuisine.repositories.interfaces;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Labor;
import com.example.baticuisine.domain.entities.Material;
import com.example.baticuisine.domain.enums.ProjectStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectInterface <Project> extends CrudInterface<Project>{
    @Override
    public Project save(Project entity);

    @Override
    public Optional<Project> findById(Project project);

    @Override
    public List<Project> findAll();

    @Override
    public Project update(Project entity);

    @Override
    public boolean delete(Project entity);

    public boolean updateFields(UUID projectId , double marginProfit , double totalCost);

//  public void saveProjectWithDetails(Client client, Project project, Material material, Labor labor);

    public void saveClientProject(Client client, Project project, Material material, Labor labor);

    List<Project> findByStatus(ProjectStatus status);

    public void updateProject(UUID id, double marginProfit, double totalCost);

    public boolean updateStatus(UUID id, String status);
}
