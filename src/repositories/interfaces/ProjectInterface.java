package repositories.interfaces;

import domain.entities.Client;
import domain.entities.Labor;
import domain.entities.Material;

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

}
