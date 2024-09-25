package com.example.baticuisine.repositories.implementations;

import com.example.baticuisine.config.DatabaseConnection;
import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Quote;
import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.exceptions.QuotesNotFoundException;
import com.example.baticuisine.repositories.interfaces.QuoteInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuoteRepository implements QuoteInterface {

    private Connection connection;

    public QuoteRepository() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Quote save(Quote quote) {
        String query = "INSERT INTO quotes (id, estimatedAmount, issueDate, validatedDate, isAccepted, project_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            UUID quoteId = quote.getId() != null ? quote.getId() : UUID.randomUUID();
            preparedStatement.setObject(1, quoteId);
            preparedStatement.setDouble(2, quote.getEstimatedAmount());
            preparedStatement.setDate(3, Date.valueOf(quote.getIssueDate()));
            preparedStatement.setDate(4, quote.getValidatedDate() != null ? Date.valueOf(quote.getValidatedDate()) : null);
            preparedStatement.setBoolean(5, quote.isAccepted());
            preparedStatement.setObject(6, quote.getProject().getId());

            preparedStatement.executeUpdate();
            quote.setId(quoteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quote;
    }

    @Override
    public Optional<Quote> findById(Quote quote) {
        String query = "SELECT * FROM quotes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, quote.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToQuote(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        String query = "SELECT * FROM quotes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                quotes.add(mapResultSetToQuote(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quotes;
    }

    @Override
    public Quote update(Quote quote) {
        String query = "UPDATE quotes SET estimatedAmount = ?, issueDate = ?, validatedDate = ?, isAccepted = ?, project_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, quote.getEstimatedAmount());
            preparedStatement.setDate(2, Date.valueOf(quote.getIssueDate()));
            preparedStatement.setDate(3, Date.valueOf(quote.getValidatedDate()));
            preparedStatement.setBoolean(4, quote.isAccepted());
            preparedStatement.setObject(5, quote.getProject().getId());
            preparedStatement.setObject(6, quote.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating quote failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while updating the quote.", e);
        }
        return quote;
    }

    @Override
    public boolean delete(Quote quote) {
        String query = "DELETE FROM quotes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, quote.getId());
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred while deleting the quote.", e);
        }
    }


    private Quote mapResultSetToQuote(ResultSet resultSet) throws SQLException {
        Quote quote = new Quote();
        quote.setId((UUID) resultSet.getObject("id"));
        quote.setEstimatedAmount(resultSet.getDouble("estimatedAmount"));
        quote.setIssueDate(resultSet.getDate("issueDate").toLocalDate());
        quote.setValidatedDate(resultSet.getDate("validatedDate").toLocalDate());
        quote.setAccepted(resultSet.getBoolean("isAccepted"));
        Project project = new Project();
        project.setId((UUID) resultSet.getObject("project_id"));
        quote.setProject(project);

        return quote;
    }

    public List<Quote> findQuotesByProject(Project project) {
        String sql = "SELECT q.id AS quote_id, q.estimatedamount, q.issuedate, q.validateddate, q.isaccepted, " +
                "p.id AS project_id, p.projectname, c.id AS client_id, c.name " +
                "FROM quotes q " +
                "JOIN projects p ON q.project_id = p.id " +
                "JOIN clients c ON p.client_id = c.id " +
                "WHERE p.id = ?";

        List<Quote> quotes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, project.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quote quote = new Quote();
                Project foundProject = new Project();
                Client client = new Client();

                quote.setId(UUID.fromString(rs.getString("quote_id")));
                quote.setEstimatedAmount(rs.getDouble("estimatedamount"));
                quote.setIssueDate(rs.getDate("issuedate").toLocalDate());
                quote.setValidatedDate(rs.getDate("validateddate") != null
                        ? rs.getDate("validateddate").toLocalDate()
                        : null);
                quote.setAccepted(rs.getBoolean("isaccepted"));

                foundProject.setId(UUID.fromString(rs.getString("project_id")));
                foundProject.setProjectName(rs.getString("projectname"));
                quote.setProject(foundProject);
                client.setId(UUID.fromString(rs.getString("client_id")));
                client.setName(rs.getString("name"));
                foundProject.setClient(client);

                quotes.add(quote);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (quotes.isEmpty()) {
            throw new QuotesNotFoundException("No quotes found for the provided project!");
        }

        return quotes;
    }


    @Override
    public boolean updateDevisStatus(UUID id) {
        String sql = "UPDATE quotes SET isaccepted = true WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException sqlException) {
            System.out.println("Error updating quote status: " + sqlException.getMessage());
        }

        return false;
    }




}
