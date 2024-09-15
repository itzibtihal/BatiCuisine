import views.submenu.ClientUi;
import config.DatabaseConnection;
import repositories.implementations.ClientRepository;
import services.ClientService;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        try {

            Connection connection = DatabaseConnection.getInstance().getConnection();
            ClientRepository clientRepository = new ClientRepository(connection);
            ClientService clientService = new ClientService(clientRepository);

            ClientUi clientUi = new ClientUi(clientService);
            clientUi.showMenu();

        } catch (SQLException e) {
            System.out.println("Error: Unable to establish a database connection.");
            e.printStackTrace();
        }
    }
    }
