package com.example.baticuisine.views.submenu;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.enums.ProjectStatus;
import com.example.baticuisine.exceptions.ClientNotFoundException;
import com.example.baticuisine.exceptions.ProjectsNotFoundException;
import com.example.baticuisine.services.implementations.ClientServiceImpl;
import com.example.baticuisine.services.implementations.ProjectServiceImpl;
import com.example.baticuisine.views.UIFunctions;

import java.util.List;
import java.util.Optional;
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
                    modifyClient();
                    break;
                case 2:
                    deleteClient();
                    break;
                case 3:
                    updateProject();
                    break;
                case 4:
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
        String settingsEmoji = " ";
        //System.out.println("   ");
        String title = "           " + settingsEmoji + " Réglages " + settingsEmoji + "              ";

        UIFunctions.printBorder(menuWidth);
        System.out.println(UIFunctions.WHITE_BOLD + "|" + UIFunctions.centerText(title, menuWidth - 2) + "|" + UIFunctions.RESET);
        UIFunctions.printBorder(menuWidth);
        System.out.println("|" + UIFunctions.BLUE + " 1. " + UIFunctions.BLUE + "Modifier un client" + UIFunctions.RESET + UIFunctions.addPadding("Modifier un client", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 2. " + UIFunctions.BLUE + "Supprimer un client" + UIFunctions.RESET + UIFunctions.addPadding("Supprimer un client", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 3. " + UIFunctions.BLUE + "Modifier un projet" + UIFunctions.RESET + UIFunctions.addPadding("Modifier un projet", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 4. " + UIFunctions.BLUE + "Supprimer un projet" + UIFunctions.RESET + UIFunctions.addPadding("Supprimer un projet", menuWidth) + "|");
        System.out.println("|" + UIFunctions.BLUE + " 5. " + UIFunctions.BLUE + "Retour" + UIFunctions.RESET + UIFunctions.addPadding("Retour", menuWidth) + "|");

        UIFunctions.printBorder(menuWidth);
    }

    private void modifyClient() {
        listAllClients();
        System.out.print("Entrez l'ID du client à modifier : ");
        String clientIdInput = scanner.nextLine();

        try {
            UUID clientId = UUID.fromString(clientIdInput);
            Client client = new Client();
            client.setId(clientId);


            Optional<Client> optionalClient = clientService.findById(client);

            if (optionalClient.isPresent()) {
                Client foundClient = optionalClient.get();


                System.out.println("Client trouvé : " + foundClient.getName());
                System.out.println("Que souhaitez-vous modifier ? ");
                System.out.println("1. Nom");
                System.out.println("2. Adresse");
                System.out.println("3. Téléphone");
                System.out.println("4. Statut professionnel");
                System.out.println("5. Annuler");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Entrez le nouveau nom : ");
                        foundClient.setName(scanner.nextLine());
                        break;
                    case 2:
                        System.out.print("Entrez la nouvelle adresse : ");
                        foundClient.setAddress(scanner.nextLine());
                        break;
                    case 3:
                        System.out.print("Entrez le nouveau numéro de téléphone : ");
                        foundClient.setPhone(scanner.nextLine());
                        break;
                    case 4:
                        System.out.print("Est-ce que ce client est professionnel ? (oui/non) : ");
                        String isProfessionalInput = scanner.nextLine();
                        foundClient.setProfessional(isProfessionalInput.equalsIgnoreCase("oui"));
                        break;
                    case 5:
                        System.out.println("Modification annulée.");
                        return;
                    default:
                        System.err.println("Option invalide. Veuillez réessayer.");
                        return;
                }


                clientService.update(foundClient);
                System.out.println("Client modifié avec succès.");
            } else {
                System.err.println("Client introuvable.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ID du client invalide. Veuillez réessayer.");
        } catch (ClientNotFoundException e) {
            System.err.println(e.getMessage());
        }
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

    private void updateProject() {
        listAllProjects();
        System.out.print("Entrez l'ID du projet à modifier : ");
        String projectIdInput = scanner.nextLine();

        try {
            UUID projectId = UUID.fromString(projectIdInput);

            Optional<Project> optionalProject = projectService.findById(projectId);

            if (optionalProject.isPresent()) {
                Project foundProject = optionalProject.get();


                System.out.println("Projet trouvé : " + foundProject.getProjectName());
                System.out.println("Que souhaitez-vous modifier ? ");
                System.out.println("1. Nom du projet");
                System.out.println("2. Marge bénéficiaire");
                System.out.println("3. Coût total");
                System.out.println("4. Statut");
                System.out.println("5. Surface");
                System.out.println("6. Annuler");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Entrez le nouveau nom du projet : ");
                        foundProject.setProjectName(scanner.nextLine());
                        break;
                    case 2:
                        System.out.print("Entrez la nouvelle marge bénéficiaire : ");
                        foundProject.setProfitMargin(scanner.nextDouble());
                        scanner.nextLine();
                        break;
                    case 3:
                        System.out.print("Entrez le nouveau coût total : ");
                        foundProject.setTotalCost(scanner.nextDouble());
                        scanner.nextLine();
                        break;
                    case 4:
                        System.out.print("Entrez le nouveau statut (INPROGRESS, COMPLETED, CANCELLED) : ");
                        String statusInput = scanner.nextLine().toUpperCase();
                        foundProject.setStatus(ProjectStatus.valueOf(statusInput));
                        break;
                    case 5:
                        System.out.print("Entrez la nouvelle surface : ");
                        foundProject.setSurface(scanner.nextDouble());
                        scanner.nextLine();
                        break;
                    case 6:
                        System.out.println("Modification annulée.");
                        return;
                    default:
                        System.err.println("Option invalide. Veuillez réessayer.");
                        return;
                }

                projectService.update(foundProject);
                System.out.println("Projet modifié avec succès.");
            } else {
                System.err.println("Projet introuvable.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ID du projet invalide. Veuillez réessayer.");
        } catch (ProjectsNotFoundException e) {
            System.err.println(e.getMessage());
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
