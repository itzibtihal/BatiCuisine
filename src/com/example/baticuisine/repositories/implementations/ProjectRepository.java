package com.example.baticuisine.repositories.implementations;

import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.domain.entities.*;
import com.example.baticuisine.domain.enums.ProjectStatus;
import com.example.baticuisine.exceptions.ProjectsNotFoundException;
import com.example.baticuisine.repositories.interfaces.ProjectInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                //System.out.println("Project saved with ID: " + project.getId());
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
        String sql = "SELECT p.*, c.name AS client_name, c.address AS client_address, c.isProfessional AS client_professional " +
                "FROM projects p " +
                "JOIN clients c ON p.client_id = c.id " +
                "WHERE p.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, project.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();
                client.setId((UUID) resultSet.getObject("client_id"));
                client.setName(resultSet.getString("client_name"));
                client.setAddress(resultSet.getString("client_address"));
                client.setProfessional(resultSet.getBoolean("client_professional"));

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
            e.printStackTrace();
        }
        return Optional.empty();
    }



    @Override

    public List<Project> findAll() {
        String sql = "SELECT DISTINCT p.id AS project_id, p.projectName, p.profitMargin, p.totalCost, " +
                "p.status AS projectStatus, p.surface, " +
                "cl.id AS client_id, cl.name AS clientName, cl.address AS clientAddress, " +
                "cl.phone AS clientPhone, cl.isProfessional AS clientIsProfessional " +
                "FROM projects p " +
                "LEFT JOIN clients cl ON p.client_id = cl.id;";

        List<Project> projects = new ArrayList<>();
        Set<UUID> seenProjectIds = new HashSet<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UUID projectId = (UUID) resultSet.getObject("project_id");

                if (seenProjectIds.add(projectId)) {
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
                }
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
                //System.out.println("Project updated successfully");
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
               // System.out.println("Project deleted successfully");
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

    @Override
    public List<Project> findByStatus(ProjectStatus status) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects p LEFT JOIN clients c ON p.client_id = c.id WHERE p.status = ?::projectStatus";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status.name());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();

                client.setName(resultSet.getString("name"));


                Project project = new Project(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("projectName"),
                        resultSet.getDouble("profitMargin"),
                        resultSet.getDouble("totalCost"),
                        resultSet.getString("status"),
                        resultSet.getDouble("surface"),
                        client
                );
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding projects by status: " + e.getMessage());
        }

        return projects;
    }

    @Override
    public void updateProject(UUID id, double marginProfit, double totalCost) {
        String sql = "UPDATE projects SET profitMargin = ?, totalCost = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, marginProfit);
            preparedStatement.setDouble(2, totalCost);
            preparedStatement.setObject(3, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
               // System.out.println("Project updated successfully");
            } else {
                System.err.println("Update failed, project not found");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public boolean updateStatus(UUID id, String status) {
        String sql = "UPDATE projects SET status = ?::projectStatus WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setObject(2, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }


}
