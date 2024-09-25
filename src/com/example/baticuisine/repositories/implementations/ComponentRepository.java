package com.example.baticuisine.repositories.implementations;

import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Component;
import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.exceptions.ComponentNotFoundException;
import com.example.baticuisine.repositories.interfaces.ComponentInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ComponentRepository implements ComponentInterface<Component> {

    private final Connection connection;

    public ComponentRepository() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Component save(Component component) {
        String sql = "INSERT INTO components (id, name, componentType, vatRate, project_id) " +
                      "VALUES (?, ?, ?, ?, ?) RETURNING id;";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, component.getId());
                preparedStatement.setString(2, component.getName());
                preparedStatement.setString(3, component.getComponentType());
                preparedStatement.setDouble(4, component.getVatRate());
                preparedStatement.setObject(5, component.getProject().getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    component.setId((UUID) resultSet.getObject("id"));
                    connection.commit();
                    return component;
                } else {
                    throw new SQLException("Creating component failed, no ID obtained.");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Error saving component: " + e.getMessage());
                throw new RuntimeException("Error saving component", e);
            }
        } catch (SQLException e) {
            System.err.println("Transaction management error: " + e.getMessage());
            throw new RuntimeException("Transaction error", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<Component> findById(Component component) {
        String query = "SELECT * FROM components WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, component.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client();
                    client.setId((UUID) resultSet.getObject("client_id"));
                    client.setName(resultSet.getString("name"));
                    client.setAddress(resultSet.getString("address"));
                    client.setPhone(resultSet.getString("phone"));
                    client.setProfessional(resultSet.getBoolean("isProfessional"));

                    Project project = new Project(
                            (UUID) resultSet.getObject("project_id"),
                            resultSet.getString("projectName"),
                            resultSet.getDouble("profitMargin"),
                            resultSet.getDouble("totalCost"),
                            resultSet.getString("status"),
                            resultSet.getDouble("surface"),
                            client
                    );

                    Component foundComponent = new Component(
                            (UUID) resultSet.getObject("id"),
                            resultSet.getString("name"),
                            resultSet.getString("componentType"),
                            resultSet.getDouble("vatRate"),
                            project
                    );
                    return Optional.of(foundComponent);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Component> findAll() {
        String query = "SELECT c.*, p.*, cl.* FROM components c " +
                       "LEFT JOIN projects p ON p.id = c.project_id " +
                       "LEFT JOIN clients cl ON cl.id = p.client_id";
        List<Component> componentList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client();
                client.setId((UUID) resultSet.getObject("client_id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhone(resultSet.getString("phone"));
                client.setProfessional(resultSet.getBoolean("isProfessional"));

                Project project = new Project(
                        (UUID) resultSet.getObject("project_id"),
                        resultSet.getString("projectName"),
                        resultSet.getDouble("profitMargin"),
                        resultSet.getDouble("totalCost"),
                        resultSet.getString("status"),
                        resultSet.getDouble("surface"),
                        client
                );

                Component component = new Component(
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("name"),
                        resultSet.getString("componentType"),
                        resultSet.getDouble("vatRate"),
                        project
                );
                componentList.add(component);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return componentList;
    }

    @Override
    public Component update(Component component) {
        String sql = "UPDATE components SET name = ?, componentType = ?, vatRate = ? WHERE id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, component.getName());
                preparedStatement.setString(2, component.getComponentType());
                preparedStatement.setDouble(3, component.getVatRate());
                preparedStatement.setObject(4, component.getId()); // Set UUID
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    connection.commit();
                    System.out.println("Component updated successfully");
                } else {
                    connection.rollback();
                    throw new ComponentNotFoundException("Component not found for update");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Transaction management error: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
        return component;
    }

    @Override
    public boolean delete(Component component) {
        String sql = "DELETE FROM components WHERE id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, component.getId());
                int result = preparedStatement.executeUpdate();
                if (result == 1) {
                    connection.commit();
                    System.out.println("Component deleted successfully");
                    return true;
                } else {
                    connection.rollback();
                    throw new ComponentNotFoundException("Component not found for deletion");
                }
            } catch (SQLException sqlException) {
                connection.rollback();
                throw new ComponentNotFoundException(sqlException.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Transaction management error: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void updateFieldsComponent(UUID componentId, double vta) {
        String sql = "UPDATE components SET vatRate = ? WHERE id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, vta);
                preparedStatement.setObject(2, componentId);
                int result = preparedStatement.executeUpdate();
                if (result == 1) {
                    connection.commit();
                    System.out.println("Component VAT rate updated successfully");
                } else {
                    connection.rollback();
                    System.out.println("Update failed, component not found");
                }
            } catch (SQLException sqlException) {
                connection.rollback();
                System.out.println(sqlException.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Transaction management error: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public double findTvaForComponent(UUID id) {
        String sql = "SELECT vatRate FROM components WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("vatRate");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return 0.0;
    }



}
