package com.example.baticuisine.repositories.implementations;


import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.domain.entities.Component;
import com.example.baticuisine.domain.entities.Material;
import com.example.baticuisine.exceptions.MaterialsNotFoundException;
import com.example.baticuisine.repositories.interfaces.MaterialInterface;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MaterialRepository implements MaterialInterface<Material> {
    private Connection connection;
    private final ComponentRepository componentRepository;

    public MaterialRepository(ComponentRepository componentRepository) throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.componentRepository = componentRepository;
    }

    @Override
    public Material save(Material material) {
        String sql = "INSERT INTO materials (id, component_id, unitCost, quantity, transportCost, qualitycoefficient) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, material.getId(), Types.OTHER);
            preparedStatement.setObject(2, material.getComponent().getId(), Types.OTHER);
            preparedStatement.setDouble(3, material.getUnitCost());
            preparedStatement.setDouble(4, material.getQuantity());
            preparedStatement.setDouble(5, material.getTransportCost());
            preparedStatement.setDouble(6, material.getCoefficientQuality());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UUID generatedId = (UUID) resultSet.getObject(1);
                material.setId(generatedId);
               // System.out.println("Material saved successfully with ID: " + generatedId);
            } else {
                throw new SQLException("Failed to save material, no ID obtained.");
            }
        } catch (SQLException e) {
            System.out.println("Error saving material: " + e.getMessage());
            throw new RuntimeException("Error saving material", e);
        }

        return material;
    }

    @Override
    public Optional<Material> findById(Material material) {
        String sql = "SELECT * FROM materials WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, material.getId(), Types.OTHER);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToMaterial(resultSet));
            } else {
                throw new MaterialsNotFoundException("Material not found with ID: " + material.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error finding material by ID: " + e.getMessage());
            throw new MaterialsNotFoundException("Error finding material", e);
        }
    }

    @Override
    public List<Material> findAll() {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM materials";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                materials.add(mapResultSetToMaterial(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all materials: " + e.getMessage());
            throw new RuntimeException("Error finding all materials", e);
        }
        return materials;
    }

    @Override
    public Material update(Material material) {
        String sql = "UPDATE materials SET unitCost = ?, quantity = ?, transportCost = ?, qualitycoefficient = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, material.getUnitCost());
            preparedStatement.setDouble(2, material.getQuantity());
            preparedStatement.setDouble(3, material.getTransportCost());
            preparedStatement.setDouble(4, material.getCoefficientQuality());
            preparedStatement.setObject(5, material.getId(), Types.OTHER);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return material;
            } else {
                throw new MaterialsNotFoundException("Material not found with ID: " + material.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating material: " + e.getMessage());
            throw new RuntimeException("Error updating material",e);
        }
    }

    @Override
    public boolean delete(Material material) {
        String sql = "DELETE FROM materials WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, material.getId(), Types.OTHER);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting material: " + e.getMessage());
            throw new RuntimeException("Error deleting material", e);
        }
    }

    private Material mapResultSetToMaterial(ResultSet resultSet) throws SQLException {
        Material material = new Material();
        material.setId((UUID) resultSet.getObject("id"));
        material.setUnitCost(resultSet.getDouble("unitCost"));
        material.setQuantity(resultSet.getDouble("quantity"));
        material.setTransportCost(resultSet.getDouble("transportCost"));
        material.setCoefficientQuality(resultSet.getDouble("coefficientQuality"));

        UUID componentId = (UUID) resultSet.getObject("component_id");
        Component component = new Component();
        component.setId(componentId);
        material.setComponent(componentRepository.findById(component)
                .orElseThrow(() -> new SQLException("Component not found with ID: " + componentId)));

        return material;
    }


    @Override
    public List<Material> findAllByProjectId(UUID projectId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT m.id, m.unitCost, m.quantity, m.transportCost, m.qualityCoefficient, " +
                "c.id AS component_id, c.name AS component_name, c.componenttype AS component_type, c.vatrate " +
                "FROM materials m " +
                "JOIN components c ON m.component_id = c.id " +
                "WHERE c.project_id = ?";


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, projectId, java.sql.Types.OTHER);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Material material = new Material();
                material.setId((UUID) resultSet.getObject("id"));
                material.setUnitCost(resultSet.getDouble("unitcost"));
                material.setQuantity(resultSet.getDouble("quantity"));
                material.setTransportCost(resultSet.getDouble("transportcost"));
                material.setCoefficientQuality(resultSet.getDouble("qualitycoefficient"));

                // Create and set the component
                Component component = new Component();
                component.setId((UUID) resultSet.getObject("component_id"));
                component.setName(resultSet.getString("component_name"));
                component.setComponentType(resultSet.getString("component_type"));
                component.setVatRate(resultSet.getDouble("vatrate"));

                material.setComponent(component);

                materials.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error finding materials by project ID: " + e.getMessage());
        }

        return materials;
    }






}
