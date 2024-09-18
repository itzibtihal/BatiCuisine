package services.implementations;



import domain.entities.Client;
import domain.entities.Labor;
import domain.entities.Material;
import domain.entities.Project;
import repositories.interfaces.ProjectInterface;
import services.ProjectService;
import validator.ProjectValidator;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectInterface<Project> projectRepository;
    private final ProjectValidator projectValidator;

    public ProjectServiceImpl(ProjectInterface<Project> projectRepository) {
        this.projectRepository = projectRepository;
        this.projectValidator = new ProjectValidator();
    }

    @Override
    public Project save(Project project) {
        if (projectValidator.isValid(project)) {
            return projectRepository.save(project);
        }
        throw new IllegalArgumentException("Invalid project data");
    }

    @Override
    public Optional<Project> findById(UUID id) {
        Project project = new Project();
        project.setId(id);
        return projectRepository.findById(project);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project update(Project project) {
        if (projectValidator.isValid(project)) {
            return projectRepository.update(project);
        }
        throw new IllegalArgumentException("Invalid project data");
    }

    @Override
    public boolean delete(Project project) {
        return projectRepository.delete(project);
    }

    @Override
    public boolean updateFields(UUID projectId, double marginProfit, double totalCost) {
        return projectRepository.updateFields(projectId, marginProfit, totalCost);
    }

    @Override
    public void saveClientProject(Client client, Project project, Material material, Labor labor) {
        projectRepository.saveClientProject(client, project, material, labor);
    }
}

