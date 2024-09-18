package repositories.implementations;

import config.DatabaseConnection;
import domain.entities.*;
import exceptions.ProjectsNotFoundException;
import repositories.interfaces.ProjectInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectRepository implements ProjectInterface<Project> {
    private final Connection connection;
    private final ClientRepository clientRepository;
    private final ComponentRepository componentRepository;
    private final MaterialRepository materialRepository;
    private final LaborRepository laborRepository;

    public ProjectRepository(ClientRepository clientRepository, ComponentRepository componentRepository, MaterialRepository materialRepository, LaborRepository laborRepository) throws SQLException {
        this.clientRepository = clientRepository;
        this.componentRepository = componentRepository;
        this.materialRepository = materialRepository;
        this.laborRepository = laborRepository;
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Project save(Project project) {
        String sql = "INSERT INTO projects (projectName, profitMargin, totalCost, status, surface, client_id) " +
                      "VALUES (?, ?, ?, ?::projectStatus, ?, ?) RETURNING id;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setDouble(2, project.getProfitMargin());
            preparedStatement.setDouble(3, project.getTotalCost());
            preparedStatement.setString(4, project.getStatus().name());
            preparedStatement.setDouble(5, project.getSurface());
            preparedStatement.setObject(6, project.getClient().getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                project.setId((UUID) resultSet.getObject("id"));
                System.out.println("Project saved with ID: " + project.getId());
                return project;
            } else {
                throw new SQLException("Creating project failed, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println("Error saving project: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Project> findById(Project project) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, project.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();
                client.setId((UUID) resultSet.getObject("client_id"));
                Project foundProject = new Project(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("projectName"),
                        resultSet.getDouble("profitMargin"),
                        resultSet.getDouble("totalCost"),
                        resultSet.getString("status"),
                        resultSet.getDouble("surface"),
                        client
                );
                return Optional.of(foundProject);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public List<Project> findAll() {
        String sql = "SELECT\n" +
                "    p.id AS project_id,\n" +
                "    p.projectName,\n" +
                "    p.profitMargin,\n" +
                "    p.totalCost,\n" +
                "    p.status AS projectStatus,\n" +
                "    p.surface,\n" +
                "    cl.id AS client_id,\n" +
                "    cl.name AS clientName,\n" +
                "    cl.address AS clientAddress,\n" +
                "    cl.phone AS clientPhone,\n" +
                "    cl.isProfessional AS clientIsProfessional,\n" +
                "    comp.id AS component_id,\n" +
                "    comp.name AS componentName,\n" +
                "    comp.componentType AS componentType,\n" +
                "    comp.vatRate AS vatRate,\n" +
                "    ma.id AS materialId,\n" +
                "    ma.quantity AS quantity,\n" +
                "    ma.transportCost AS transportCost,\n" +
                "    ma.qualitycoefficient AS coefficientQuality,\n" +
                "    la.id AS laborId,\n" +
                "    la.hourlyrate AS hourlyCost,\n" +
                "    la.workhours AS workingHours,\n" +
                "    la.workerProductivity AS workerProductivity\n" +
                "FROM\n" +
                "    projects p\n" +
                "    LEFT JOIN clients cl ON p.client_id = cl.id\n" +
                "    LEFT JOIN components comp ON p.id = comp.project_id\n" +
                "    LEFT JOIN materials ma ON comp.id = ma.component_id\n" +
                "    LEFT JOIN labor la ON comp.id = la.component_id;";

        List<Project> projects = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UUID projectId = (UUID) resultSet.getObject("project_id");

                Client client = new Client();
                client.setId((UUID) resultSet.getObject("client_id"));
                client.setName(resultSet.getString("clientName"));
                client.setAddress(resultSet.getString("clientAddress"));
                client.setPhone(resultSet.getString("clientPhone"));
                client.setProfessional(resultSet.getBoolean("clientIsProfessional"));

                Project project = new Project(
                        projectId,
                        resultSet.getString("projectName"),
                        resultSet.getDouble("profitMargin"),
                        resultSet.getDouble("totalCost"),
                        resultSet.getString("projectStatus"),
                        resultSet.getDouble("surface"),
                        client
                );

                projects.add(project);

                UUID componentId = (UUID) resultSet.getObject("component_id");

                Component component = new Component();
                component.setId(componentId);
                component.setName(resultSet.getString("componentName"));
                component.setComponentType(resultSet.getString("componentType"));
                component.setVatRate(resultSet.getDouble("vatRate"));
                component.setProject(project);

                project.addComponent(component);

                UUID materialId = (UUID) resultSet.getObject("materialId");
                Material material = new Material();
                material.setId(materialId);
                material.setQuantity(resultSet.getDouble("quantity"));
                material.setTransportCost(resultSet.getDouble("transportCost"));
                material.setCoefficientQuality(resultSet.getDouble("coefficientQuality"));
                material.setComponent(component);

                component.addMaterial(material);

                UUID laborId = (UUID) resultSet.getObject("laborId");
                Labor labor = new Labor();
                labor.setId(laborId);
                labor.setHourlyRate(resultSet.getDouble("hourlyCost"));
                labor.setWorkHours(resultSet.getInt("workingHours"));
                labor.setWorkerProductivity(resultSet.getDouble("workerProductivity"));
                labor.setComponent(component);

                component.addLabor(labor);

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving projects: " + e.getMessage());
        }

        return projects;
    }

    @Override
    public Project update(Project project) {
        String sql = "UPDATE projects SET projectName = ?, profitMargin = ?, totalCost = ?, status = ?::projectStatus, surface = ?, client_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setDouble(2, project.getProfitMargin());
            preparedStatement.setDouble(3, project.getTotalCost());
            preparedStatement.setString(4, project.getStatus().name());
            preparedStatement.setDouble(5, project.getSurface());
            preparedStatement.setObject(6, project.getClient().getId());
            preparedStatement.setObject(7, project.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Project updated successfully");
            } else {
                throw new ProjectsNotFoundException("Update failed, project not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public boolean delete(Project project) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, project.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Project deleted successfully");
                return true;
            } else {
                throw new ProjectsNotFoundException("Delete failed, project not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateFields(UUID projectId, double marginProfit, double totalCost) {
        return false;
    }

    @Override
    public void saveClientProject(Client client, Project project, Material material, Labor labor) {
        try {
            connection.setAutoCommit(false);

            Client savedClient = clientRepository.save(client);
            project.setClient(savedClient);

            Component materialComponent = new Component();
            materialComponent.setName(material.getName());
            materialComponent.setComponentType("Material");
            materialComponent.setVatRate(material.getVatRate());
            materialComponent.setProject(project);

            Component savedMaterialComponent = componentRepository.save(materialComponent);

            material.setComponent(savedMaterialComponent);
            materialRepository.save(material);

            Component laborComponent = new Component();
            laborComponent.setName(labor.getName());
            laborComponent.setComponentType("Workforce");
            laborComponent.setVatRate(labor.getVatRate());
            laborComponent.setProject(project);

            Component savedWorkforceComponent = componentRepository.save(laborComponent);

            labor.setComponent(savedWorkforceComponent);
            laborRepository.save(labor);

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error during transaction rollback: " + rollbackEx.getMessage());
            }
            System.out.println("Error saving client and project: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
}
