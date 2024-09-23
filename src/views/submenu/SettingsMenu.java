package views.submenu;

import domain.entities.Client;
import domain.entities.Project;
import domain.enums.ProjectStatus;
import services.implementations.ClientServiceImpl;
import services.implementations.ProjectServiceImpl;
import views.UIFunctions;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class SettingsMenu {
    private final ProjectServiceImpl projectService;
    private final ClientServiceImpl clientService;
    private final Scanner scanner;

    public SettingsMenu(ProjectServiceImpl projectService, ClientServiceImpl clientService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public void displaySettingMenu() {
        int choice;

        do {
            displayMenuOptions();
            System.out.print("Choisissez une option : ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    deleteClient();
                    break;
                case 2:
                    deleteProject();
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
        int menuWidth = 42;
        String settingsEmoji = "⚙️";
        //System.out.println("   ");
        String title = "           " + settingsEmoji + " Réglages " + settingsEmoji + "              ";

        UIFunctions.printBorder(menuWidth);
        System.out.println(UIFunctions.WHITE_BOLD + "|" + UIFunctions.centerText(title, menuWidth - 2) + "|" + UIFunctions.RESET);
        UIFunctions.printBorder(menuWidth);

        System.out.println("|" + UIFunctions.BLUE + " 1. " + UIFunctions.BLUE + "Supprimer un client" + UIFunctions.RESET + UIFunctions.addPadding("Supprimer un client", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 2. " + UIFunctions.BLUE + "Supprimer un projet" + UIFunctions.RESET + UIFunctions.addPadding("Supprimer un projet", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 5. " + UIFunctions.BLUE + "Retour" + UIFunctions.RESET + UIFunctions.addPadding("Retour", menuWidth) + "|");

        UIFunctions.printBorder(menuWidth);
    }

    private void deleteClient() {
        listAllClients();
        System.out.print("Entrez l'ID du client à supprimer : ");
        String clientIdInput = scanner.nextLine();

        try {
            UUID clientId = UUID.fromString(clientIdInput);
            Client client = new Client();
            client.setId(clientId);
            boolean isDeleted = clientService.delete(client);
            if (isDeleted) {
                System.out.println("Client supprimé avec succès.");
            } else {
                System.err.println("Échec de la suppression du client. Client introuvable.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ID du client invalide. Veuillez réessayer.");
        }
    }

    private void deleteProject() {
        listAllProjects();
        System.out.print("Entrez l'ID du projet à supprimer : ");
        String projectIdInput = scanner.nextLine();

        try {
            UUID projectId = UUID.fromString(projectIdInput);
            Project project = new Project();
            project.setId(projectId);
            boolean isDeleted = projectService.delete(project);
            if (isDeleted) {
                System.out.println("Projet supprimé avec succès.");
            } else {
                System.err.println("Échec de la suppression du projet. Projet introuvable.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ID du projet invalide. Veuillez réessayer.");
        }
    }



    private void displayProjectTable(List<Project> projects) {
        System.out.println("|--------------------------------------|-----------------------------|---------------|-----------------------------|");
        System.out.println("| ID du projet                         | Nom du projet               | Statut        | Client                      |");
        System.out.println("|--------------------------------------|-----------------------------|---------------|-----------------------------|");

        for (Project project : projects) {
            UUID projectId = project.getId(); // Assuming getId() returns a UUID
            String clientName = project.getClient() != null ? project.getClient().getName() : "N/A";
            ProjectStatus status = project.getStatus();

            System.out.printf("| %-30s | %-27s | %-13s | %-27s |%n",
                    projectId,
                    project.getProjectName(),
                    status,
                    clientName
            );
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
    }
    private void displayClientTable(List<Client> clients) {
        System.out.println("|--------------------------------------|-----------------------------|---------------|-----------------------------|---------------|");
        System.out.println("| ID du client                         | Nom du client               | Adresse       | Téléphone                   | Professionnel |");
        System.out.println("|--------------------------------------|-----------------------------|---------------|-----------------------------|---------------|");

        for (Client client : clients) {
            UUID clientId = client.getId();
            String clientName = client.getName() != null ? client.getName() : "N/A";
            String address = client.getAddress() != null ? client.getAddress() : "N/A";
            String phone = client.getPhone() != null ? client.getPhone() : "N/A";
            boolean isProfessional = client.isProfessional();
            String professionalStatus = isProfessional ? "Oui" : "Non";

            System.out.printf("| %-30s | %-27s | %-13s | %-27s | %-13s |%n",
                    clientId,
                    clientName,
                    address,
                    phone,
                    professionalStatus
            );
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
    }


    private void listAllProjects() {
        List<Project> projects = projectService.findAll();

        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
        } else {
            displayProjectTable(projects);
        }
    }
    private void listAllClients() {
        List<Client> clients = clientService.findAll();

        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
        } else {
            displayClientTable(clients);
        }
    }


}
