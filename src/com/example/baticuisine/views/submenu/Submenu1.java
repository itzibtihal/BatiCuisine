package com.example.baticuisine.views.submenu;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.domain.entities.Project;
import com.example.baticuisine.domain.enums.ProjectStatus;
import com.example.baticuisine.exceptions.InvalidClientException;
import com.example.baticuisine.services.implementations.ClientServiceImpl;
import com.example.baticuisine.services.implementations.ProjectServiceImpl;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Submenu1 {

    private ClientServiceImpl clientService;
    private ProjectServiceImpl projectService;
    private final MaterialMenu materialMenu;
    private final LaborMenu laborMenu;
    private final Scanner scanner;


    public Submenu1(ClientServiceImpl clientService, ProjectServiceImpl projectService , MaterialMenu materialMenu, LaborMenu laborMenu) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.materialMenu = materialMenu;
        this.laborMenu = laborMenu;
        this.scanner = new Scanner(System.in);
    }

    public void createProjectMenu() {
        int choice = 0;
        boolean validInput = false;

        System.out.println("\n--- Recherche de client --");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?\n");

        while (!validInput) {
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.print("Choisissez une option : ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1 || choice == 2) {
                    validInput = true;
                } else {
                    System.out.println("Option invalide. Veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.nextLine();
            }
        }

        switch (choice) {
            case 1:
                searchExistingClient();
                break;
            case 2:
                addClient();
                break;
        }
    }

    public void searchExistingClient() {
        System.out.println("\n--- Recherche de client existant ---");
        System.out.print("Entrez le nom du client : ");
        String clientName = scanner.nextLine();

        List<Client> foundClients = clientService.findClientsByName(clientName);
        if (!foundClients.isEmpty()) {
            Client client = foundClients.get(0);

            System.out.println("\n     Client trouvé !");
            System.out.println("            *Nom : " + client.getName());
            System.out.println("            *Adresse : " + client.getAddress());
            System.out.println("            *Numéro de téléphone : " + client.getPhone());

            addClientToProject(client);
        } else {
            System.out.println("Client non trouvé.");
        }
    }


    public void addClient() {
        System.out.println("\n--- Ajouter un nouveau client---");


        System.out.print("            Entrer le nom du client: ");
        String name = scanner.nextLine();

        System.out.print("            Entrer l'addresse: ");
        String address = scanner.nextLine();

        System.out.print("            Entrer le Numéro de téléphone: ");
        String phone = scanner.nextLine();

        System.out.print("            Le client est-il un professionnel ? (true/false): ");
        boolean isProfessional = Boolean.parseBoolean(scanner.nextLine());

        Client client = new Client();
        client.setName(name);
        client.setAddress(address);
        client.setPhone(phone);
        client.setProfessional(isProfessional);

        try {
            clientService.save(client);
            System.out.println("Client ajouté avec succès !");
        } catch (InvalidClientException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void addClientToProject(Client client) {
        try {

            System.out.print("\n     Souhaitez-vous continuer avec ce client ? (y/n) : ");
            String continueChoice = scanner.nextLine();

            if (continueChoice.equalsIgnoreCase("y")) {
                System.out.println("            Vous avez choisi de continuer avec " + client.getName() + ". \n");
            } else {
                System.out.println("          Retour au menu principal.\n");
                return;
            }

            System.out.println("\n          Ajouter un Nouveau Projet ");

            System.out.print("    Entrez le nom du projet : ");
            String projectName = scanner.nextLine();

            System.out.print("    Entrez le statut du projet (ENCOURS, TERMINE, ANNULE) : ");
            String statut = scanner.nextLine().toUpperCase();

            ProjectStatus statutProjet;
            switch (statut) {
                case "ENCOURS":
                    statutProjet = ProjectStatus.INPROGRESS;
                    break;
                case "TERMINE":
                    statutProjet = ProjectStatus.COMPLETED;
                    break;
                case "ANNULE":
                    statutProjet = ProjectStatus.CANCELLED;
                    break;
                default:
                    throw new IllegalArgumentException("Statut de projet non valide : " + statut);
            }

            System.out.print("    Entrez la surface du projet (en mètres carrés) : ");
            double surface = scanner.nextDouble();
            scanner.nextLine();

            Project project = new Project(
                    UUID.randomUUID(),
                    projectName,
                    0,
                    0,
                    statutProjet.name(),
                    surface,
                    client
            );
            Project savedProject = projectService.save(project);

            materialMenu.addMaterial(savedProject);
            laborMenu.addLabor(savedProject);

        } catch (Exception e) {
            System.out.println(" An error occurred while adding the project: " + e.getMessage());
        }

    }
}
