package com.example.baticuisine.views;

import com.example.baticuisine.services.implementations.ClientServiceImpl;
import com.example.baticuisine.services.implementations.ProjectServiceImpl;
import com.example.baticuisine.views.submenu.*;

import java.util.Scanner;

public class Menu {
    private final ClientServiceImpl clientService;
    private final ProjectServiceImpl projectService;
    private final Submenu1 submenu;
    private final ProjectMenu projectMenu;
    private final SettingsMenu settingsMenu;
    private final CostMenu costMenu;

    public Menu(ClientServiceImpl clientService, ProjectServiceImpl projectService, MaterialMenu materialMenu, LaborMenu laborMenu, ProjectMenu projectMenu,SettingsMenu settingsMenu,CostMenu costMenu) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.submenu = new Submenu1(clientService, projectService, materialMenu, laborMenu);
        this.projectMenu = projectMenu;
        this.settingsMenu = settingsMenu;
        this.costMenu = costMenu;

    }

    public void display() {
        Scanner scanner = new Scanner(System.in);
        int choix;

        System.out.println();
        System.out.println("Bienvenue dans l'application Bati Cuisine  !");

        do {
            displayMainMenu();
            System.out.print("Choisissez une option : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    submenu.createProjectMenu();
                    break;
                case 2:
                    projectMenu.displayProjectMenu();
                    break;
                case 3:
                    totalCost();
                    break;
                case 4:
                    settingsMenu.displaySettingMenu();
                    break;
                case 5:
                    System.err.println("\n---  Fin du projet  ---");
                    break;
            }
        } while (choix != 5);

        scanner.close();
    }

    public static void displayMainMenu() {
        int menuWidth = 42;
        String title = "        Menu Principal             ";

        UIFunctions.printBorder(menuWidth);
        System.out.println(UIFunctions.WHITE_BOLD + "|" + UIFunctions.centerText(title, menuWidth - 2) + "|" + UIFunctions.RESET);
        UIFunctions.printBorder(menuWidth);

        System.out.println("|" + UIFunctions.BLUE + " 1. " + UIFunctions.BLUE + "Créer un nouveau projet" + UIFunctions.RESET + UIFunctions.addPadding("Créer un nouveau projet", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 2. " + UIFunctions.BLUE + "Afficher les projets existants" + UIFunctions.RESET + UIFunctions.addPadding("Afficher les projets existants", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 3. " + UIFunctions.BLUE + "Calculer le coût d'un projet" + UIFunctions.RESET + UIFunctions.addPadding("Calculer le coût d'un projet", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 4. " + UIFunctions.BLUE + "Réglages" + UIFunctions.RESET + UIFunctions.addPadding("Réglages", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 5. " + UIFunctions.BLUE + "Quitter" + UIFunctions.RESET + UIFunctions.addPadding("Quitter", menuWidth) + "|");

        UIFunctions.printBorder(menuWidth);
    }
    public void totalCost() {
        costMenu.save();
    }

}
