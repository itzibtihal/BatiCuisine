package views.submenu;

import domain.entities.Client;
import exceptions.InvalidClientException;
import repositories.implementations.ClientRepository;
import services.ClientService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class submenu1 {

    private static ClientService clientService;
    private static Scanner scanner = new Scanner(System.in);

    public submenu1(ClientService clientService) {
        submenu1.clientService = clientService;
    }

    public static void createProjectMenu() {
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

    public static void searchExistingClient() {
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

    // add new client
    public static void addClient() {
        System.out.println("\n--- Ajouter un nouveau client---");

        // Get client details from user input
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

        } catch (InvalidClientException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Client ajouté avec succès !");
    }


    private static void addClientToProject(Client client) {
        System.out.print("\n     Souhaitez-vous continuer avec ce client ? (y/n) : ");
        String continueChoice = scanner.nextLine();

        if (continueChoice.equalsIgnoreCase("y")) {
            System.out.println("            Vous avez choisi de continuer avec " + client.getName() + ". \n");
        } else {
            System.out.println("          Retour au menu principal.\n");
        }
    }

}
