import validator.ClientValidator;
import views.UIFunctions;
import config.DatabaseConnection;
import repositories.implementations.ClientRepository;
import services.ClientServiceImpl;
import views.submenu.submenu1;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
//        try {
//
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            ClientRepository clientRepository = new ClientRepository(connection);
////            ClientService clientService = new ClientService(clientRepository);
//
//            ClientUi clientUi = new ClientUi(clientService);
//            clientUi.showMenu();
//
//        } catch (SQLException e) {
//            System.out.println("Error: Unable to establish a database connection.");
//            e.printStackTrace();
//        }


            Scanner scanner = new Scanner(System.in);

            Connection connection = null;
            try {
                // Obtain the connection from DatabaseConnection
                connection = DatabaseConnection.getInstance().getConnection();

                // Instantiate ClientRepository with the connection
                ClientRepository clientRepository = new ClientRepository(connection);


                ClientValidator clientValidator = new ClientValidator();
                // Instantiate ClientService with the repository and validator
                ClientServiceImpl clientService = new ClientServiceImpl(clientRepository, clientValidator);



                // Initialize submenu1 with clientService
                submenu1 submenu = new submenu1(clientService);

                int choix;
                System.out.println();
                System.out.println("Bienvenue dans l'application Bati Cuisine 🏠🍽️ !");

                do {
                    displayMainMenu();

                    System.out.print("Choisissez une option : ");
                    choix = scanner.nextInt();
                    scanner.nextLine();

                    switch (choix) {
                        case 1:
                            submenu1.createProjectMenu(); // Call createProjectMenu method
                            break;
                        case 2:
                            System.out.println("Afficher les projets existants");
                            break;
                        case 3:
                            System.out.println("Calculer le coût d'un projet");
                            break;
                        case 4:
                            System.err.println("\n---  Fin du projet  ---");
                            break;
                        default:
                            System.out.println("Option invalide. Veuillez réessayer.");
                    }
                } while (choix != 4); // Now 'choix' is in scope here

            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error establishing database connection.");
            } finally {
                // Close the connection if it was successfully created
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            scanner.close();
        }

        public static void displayMainMenu() {
            int menuWidth = 42;
            String title = "     🏗️🔨 Menu Principal 🏗️🔨          ";

            UIFunctions.printBorder(menuWidth);
            System.out.println(UIFunctions.WHITE_BOLD + "|" + UIFunctions.centerText(title, menuWidth - 2) + "|" + UIFunctions.RESET);
            UIFunctions.printBorder(menuWidth);

            System.out.println("|" + UIFunctions.BLUE + " 1. " + UIFunctions.BLUE + "Créer un nouveau projet" + UIFunctions.RESET + UIFunctions.addPadding("Créer un nouveau projet", menuWidth) + "|");
            System.out.println("|" + UIFunctions.BLUE + " 2. " + UIFunctions.BLUE + "Afficher les projets existants" + UIFunctions.RESET + UIFunctions.addPadding("Afficher les projets existants", menuWidth) + "|");
            System.out.println("|" + UIFunctions.BLUE + " 3. " + UIFunctions.BLUE + "Calculer le coût d'un projet" + UIFunctions.RESET + UIFunctions.addPadding("Calculer le coût d'un projet", menuWidth) + "|");
            System.out.println("|" + UIFunctions.BLUE + " 4. " + UIFunctions.BLUE + "Quitter" + UIFunctions.RESET + UIFunctions.addPadding("Quitter", menuWidth) + "|");

            UIFunctions.printBorder(menuWidth);
        }

    }
