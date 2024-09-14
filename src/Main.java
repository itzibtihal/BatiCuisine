import config.DatabaseConnection;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                System.out.println("Test passed: Connection successful!");
            } else {
                System.out.println("Test failed: Connection is null.");
            }
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
        }
    }
}