package com.example.baticuisine.views.submenu;

import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.enums.ProjectStatus;
import com.example.baticuisine.services.implementations.ProjectServiceImpl;
import com.example.baticuisine.views.UIFunctions;

import java.util.List;
import java.util.Scanner;

public class ProjectMenu {

    private final ProjectServiceImpl projectService;

    public ProjectMenu(ProjectServiceImpl projectService) {
        this.projectService = projectService;
    }

    public void displayProjectMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenuOptions();
            System.out.print("Choisissez une option : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listAllProjects();
                    break;
                case 2:
                    listProjectsByStatus(ProjectStatus.INPROGRESS);
                    break;
                case 3:
                    listProjectsByStatus(ProjectStatus.COMPLETED);
                    break;
                case 4:
                    listProjectsByStatus(ProjectStatus.CANCELLED);
                    break;
                case 5:
                    System.err.println("Retour au menu principal");
                    break;
                default:
                    System.err.println("Option invalide. Veuillez réessayer.");
            }
        } while (choice != 5);
    }

    private void displayMenuOptions() {
        int menuWidth = 50;
        String title = "                Menu Projets                ";

        UIFunctions.printBorder(menuWidth);
        System.out.println(UIFunctions.WHITE_BOLD + "|" + UIFunctions.centerText(title, menuWidth - 2) + "|" + UIFunctions.RESET);
        UIFunctions.printBorder(menuWidth);

        System.out.println("|" + UIFunctions.BLUE + " 1. " + UIFunctions.BLUE + "Projets du plus récent au plus ancien" + UIFunctions.RESET + UIFunctions.addPadding("Projets du plus récent au plus ancien", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 2. " + UIFunctions.BLUE + "Projets en cours" + UIFunctions.RESET + UIFunctions.addPadding("Projets en cours", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 3. " + UIFunctions.BLUE + "Projets terminés" + UIFunctions.RESET + UIFunctions.addPadding("Projets terminés", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 4. " + UIFunctions.BLUE + "Projets annulés" + UIFunctions.RESET + UIFunctions.addPadding("Projets annulés", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 5. " + UIFunctions.BLUE + "Retour" + UIFunctions.RESET + UIFunctions.addPadding("Retour", menuWidth) + "|");

        UIFunctions.printBorder(menuWidth);
    }

    private void listAllProjects() {
        List<Project> projects = projectService.findAll();

        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
        } else {
            displayProjectTable(projects);
        }
    }

    private void listProjectsByStatus(ProjectStatus status) {
        List<Project> projects = projectService.findByStatus(status);

        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé avec le statut : " + status);
        } else {
            displayProjectTable(projects);
        }
    }

    private void displayProjectTable(List<Project> projects) {
        System.out.println("|--------------------------------|-----------------------------|---------------|---------------|");
        System.out.println("| Nom du projet                  | Client                      | Statut        | Profit Margin |");
        System.out.println("|--------------------------------|-----------------------------|---------------|---------------|");

        for (Project project : projects) {
            String clientName = project.getClient().getName();
            ProjectStatus status = project.getStatus();
            double profitMargin = project.getProfitMargin();

            System.out.printf("| %-30s | %-27s | %-13s | %-13.2f |%n",
                    project.getProjectName(),
                    clientName,
                    status,
                    profitMargin
            );
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }
}
