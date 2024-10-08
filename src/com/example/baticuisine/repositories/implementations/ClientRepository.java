package com.example.baticuisine.repositories.implementations;

import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.repositories.interfaces.ClientInterface;
import com.example.baticuisine.exceptions.ClientNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository implements ClientInterface<Client> {

    private Connection connection;

    public ClientRepository(Connection connection) throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Client save(Client client) {
        try {
            if (isClientNameExists(client.getName())) {
                throw new RuntimeException("Client with the name '" + client.getName() + "' already exists.");
            }

            connection.setAutoCommit(false);
            String query = "INSERT INTO clients (id, name, address, phone, isProfessional) VALUES (?, ?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                UUID clientId = UUID.randomUUID();
                preparedStatement.setObject(1, clientId);
                preparedStatement.setString(2, client.getName());
                preparedStatement.setString(3, client.getAddress());
                preparedStatement.setString(4, client.getPhone());
                preparedStatement.setBoolean(5, client.isProfessional());

                preparedStatement.executeUpdate();
                connection.commit();

                client.setId(clientId);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Optional<Client> findById(Client client) {
        String query = "SELECT * FROM clients WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, client.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Client foundClient = new Client();
                    foundClient.setId((UUID) resultSet.getObject("id"));
                    foundClient.setName(resultSet.getString("name"));
                    foundClient.setAddress(resultSet.getString("address"));
                    foundClient.setPhone(resultSet.getString("phone"));
                    foundClient.setProfessional(resultSet.getBoolean("isProfessional"));

                    return Optional.of(foundClient);
                } else {
                    throw new ClientNotFoundException("Client with ID " + client.getId() + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while finding client.", e);
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client();
                client.setId((UUID) resultSet.getObject("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhone(resultSet.getString("phone"));
                client.setProfessional(resultSet.getBoolean("isProfessional"));

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Client update(Client client) {
        String query = "UPDATE clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getAddress());
            preparedStatement.setString(3, client.getPhone());
            preparedStatement.setBoolean(4, client.isProfessional());
            preparedStatement.setObject(5, client.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                return client;
            } else {
                throw new ClientNotFoundException("No client found with ID " + client.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while updating client.", e);
        }
    }

    @Override
    public boolean delete(Client client) {
        String query = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, client.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                return true;
            } else {
                throw new ClientNotFoundException("No client found with ID " + client.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while deleting client.", e);
        }
    }

    @Override
    public List<Client> findByName(String name) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = mapResultSetToClient(rs);
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getObject("id", UUID.class));
        client.setName(rs.getString("name"));
        client.setAddress(rs.getString("address"));
        client.setPhone(rs.getString("phone"));
        client.setProfessional(rs.getBoolean("isProfessional"));

        return client;
    }

    private boolean isClientNameExists(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM clients WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }


}
