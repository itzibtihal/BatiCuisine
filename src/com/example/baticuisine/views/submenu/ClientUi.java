package com.example.baticuisine.views.submenu;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.services.implementations.ClientServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ClientUi {

    private ClientServiceImpl clientService;
    private Scanner scanner;

    public ClientUi(ClientServiceImpl clientService) {
        this.clientService = clientService;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("=== Client Management ===");
            System.out.println("1. Add new client");
            System.out.println("2. Find client by ID");
            System.out.println("3. View all clients");
            System.out.println("4. Update client");
            System.out.println("5. Delete client");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    findClientById();
                    break;
                case 3:
                    viewAllClients();
                    break;
                case 4:
                    updateClient();
                    break;
                case 5:
                    deleteClient();
                    break;
                case 6:
                    System.out.println("Exiting Client Management...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addClient() {
        System.out.println("Enter client's name:");
        String name = scanner.nextLine();

        System.out.println("Enter client's address:");
        String address = scanner.nextLine();

        System.out.println("Enter client's phone number:");
        String phone = scanner.nextLine();

        System.out.println("Is the client a professional? (true/false):");
        boolean isProfessional = scanner.nextBoolean();

        Client client = new Client();
        client.setName(name);
        client.setAddress(address);
        client.setPhone(phone);
        client.setProfessional(isProfessional);
        clientService.save(client);
        System.out.println("Client added successfully!");
    }

    private void findClientById() {
        System.out.println("Enter client ID:");
        UUID clientId = UUID.fromString(scanner.nextLine());
        Client clientWithId = new Client();
        clientWithId.setId(clientId);
        Optional<Client> clientOptional = clientService.findById(clientWithId);
        System.out.println("Searched for Client ID: " + clientId);
        if (clientOptional.isPresent()) {
            displayClientInfo(clientOptional.get());
        } else {
            System.out.println("Client not found.");
        }
    }


    private void viewAllClients() {
        List<Client> clients = clientService.findAll();
        if (!clients.isEmpty()) {
            for (Client client : clients) {
                displayClientInfo(client);
            }
        } else {
            System.out.println("No clients available.");
        }
    }

    private void updateClient() {
        System.out.println("Enter the client UUID to update:");
        String uuidString = scanner.nextLine().trim();
        UUID clientId;

        try {
            clientId = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
            return;
        }

        Client clientToFind = new Client();
        clientToFind.setId(clientId);
        Optional<Client> clientOptional = clientService.findById(clientToFind);

        if (clientOptional.isPresent()) {
            Client clientToUpdate = clientOptional.get();
            System.out.println("Enter new client's name (press Enter to keep current):");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                clientToUpdate.setName(name);
            }

            System.out.println("Enter new client's address (press Enter to keep current):");
            String address = scanner.nextLine();
            if (!address.isEmpty()) {
                clientToUpdate.setAddress(address);
            }

            System.out.println("Enter new client's phone number (press Enter to keep current):");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) {
                clientToUpdate.setPhone(phone);
            }

            System.out.println("Is the client a professional? (y/n, press Enter to keep current):");
            String isProfessionalInput = scanner.nextLine().toLowerCase();
            if (!isProfessionalInput.isEmpty()) {
                boolean isProfessional = isProfessionalInput.equals("y");
                clientToUpdate.setProfessional(isProfessional);
            }

            clientService.update(clientToUpdate);
            System.out.println("Client updated successfully!");
        } else {
            System.out.println("Client not found.");
        }
    }

    private void deleteClient() {
        System.out.println("Enter the client ID to delete:");
        String uuidString = scanner.nextLine().trim();
        UUID clientId;

        try {
            clientId = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
            return;
        }

        Client client = new Client();
        client.setId(clientId);
        Optional<Client> clientOptional =   clientService.findById(client);
        if (clientOptional != null) {
            clientService.delete(client);
            System.out.println("Client deleted successfully!");
        } else {
            System.out.println("Client not found.");
        }
    }

    private void displayClientInfo(Client client) {
        System.out.println("Client ID: " + client.getId());
        System.out.println("Name: " + client.getName());
        System.out.println("Address: " + client.getAddress());
        System.out.println("Phone: " + client.getPhone());
        System.out.println("Professional: " + (client.isProfessional() ? "Yes" : "No"));
    }

}
