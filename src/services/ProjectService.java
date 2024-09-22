package services;

import domain.entities.Client;
import domain.entities.Labor;
import domain.entities.Material;
import domain.entities.Project;
import domain.enums.ProjectStatus;

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
