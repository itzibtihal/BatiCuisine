package views.submenu;

import java.util.Scanner;

public class submenu1 {
    private static Scanner scanner = new Scanner(System.in);


    public static void createProjectMenu() {
        int choice;

        System.out.println("\n--- Recherche de client --");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?\n");

        do {
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.print("Choisissez une option : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Chercher un client existant");

                    break;
                case 2:
                    System.out.println("Ajouter un nouveau client");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        } while (choice != 1 && choice != 2);
    }
}
