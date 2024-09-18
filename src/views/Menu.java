package views;

import repositories.implementations.ClientRepository;
import services.implementations.ClientServiceImpl;
import validator.ClientValidator;
import config.DatabaseConnection;
import views.submenu.submenu1;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            ClientRepository clientRepository = new ClientRepository(connection);

            ClientValidator clientValidator = new ClientValidator();

            ClientServiceImpl clientService = new ClientServiceImpl(clientRepository, clientValidator);

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
                        submenu1.createProjectMenu();
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
            } while (choix != 4);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error establishing database connection.");
        } finally {

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
