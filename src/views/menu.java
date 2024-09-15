package views;

import views.submenu.submenu1;

import java.util.Scanner;

public class menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println();
        System.out.println("Bienvenue dans l'application Bati Cuisine 🏠🍽️ !");

        do {
            displayMainMenu();


            System.out.print("Choisissez une option : ");
            choice = scanner.nextInt();


            switch (choice) {
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
        } while (choice != 4);

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
