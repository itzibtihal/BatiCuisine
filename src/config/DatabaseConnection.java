package config;



import exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL ( green / test )
//    private static final String URL = "jdbc:postgresql://localhost:5432/btcuisine";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "password";

    private static final String URL = System.getenv("URL");
    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("PASSWORD");


    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to PostgreSQL database established.");
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("PostgreSQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connection to PostgreSQL database failed.", e);
        }
        return connection;
    }
}