package repositories.implementations;


import config.DatabaseConnection;
import domain.entities.Labor;
import exceptions.LaborNotFoundException;
import repositories.interfaces.LaborInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LaborRepository implements LaborInterface<Labor> {

    private Connection connection;
    private ComponentRepository componentRepository;

    public LaborRepository(ComponentRepository componentRepository) throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.componentRepository = componentRepository;
    }


    @Override
    public Labor save(Labor labor) {
        String sql = "INSERT INTO labor (name, hourlyRate, hoursWorked) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, labor.getName());
            preparedStatement.setDouble(2, labor.getHourlyRate());
            preparedStatement.setDouble(3, labor.getWorkHours());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                labor.setId(resultSet.getObject("id", UUID.class));
                System.out.println("Labor saved successfully with ID: " + labor.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error saving labor: " + e.getMessage());
        }
        return labor;
    }

    @Override
    public Optional<Labor> findById(Labor labor) {
        String sql = "SELECT * FROM labor WHERE id = ?";
        Labor foundLabor = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, labor.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                foundLabor = mapResultSetToLabor(resultSet);
            } else {
                throw new LaborNotFoundException("Labor not found with ID: " + labor.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error finding labor: " + e.getMessage());
        }
        return Optional.ofNullable(foundLabor);
    }

    @Override
    public List<Labor> findAll() {
        List<Labor> labors = new ArrayList<>();
        String sql = "SELECT * FROM labor";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                labors.add(mapResultSetToLabor(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error finding all labors: " + e.getMessage());
        }
        return labors;
    }

    @Override
    public Labor update(Labor labor) {
        String sql = "UPDATE labor SET name = ?, hourlyRate = ?, hoursWorked = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, labor.getName());
            preparedStatement.setDouble(2, labor.getHourlyRate());
            preparedStatement.setDouble(3, labor.getWorkHours());
            preparedStatement.setObject(4, labor.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new LaborNotFoundException("Updating labor failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating labor: " + e.getMessage());
        }
        return labor;
    }

    @Override
    public boolean delete(Labor labor) {
        String sql = "DELETE FROM labor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, labor.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else {
                throw new LaborNotFoundException("Deleting labor failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting labor: " + e.getMessage());
            return false;
        }
    }

    private Labor mapResultSetToLabor(ResultSet resultSet) throws SQLException {
        Labor labor = new Labor();
        labor.setId(resultSet.getObject("id", UUID.class));
        labor.setName(resultSet.getString("name"));
        labor.setHourlyRate(resultSet.getDouble("hourlyRate"));
        labor.setWorkHours(resultSet.getDouble("hoursWorked"));
        return labor;
    }
}
