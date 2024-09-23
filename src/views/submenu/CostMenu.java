package views.submenu;

import domain.entities.Labor;
import domain.entities.Material;
import domain.entities.Project;
import domain.entities.Quote;
import domain.enums.ProjectStatus;
import exceptions.QuotesNotFoundException;
import repositories.implementations.ComponentRepository;
import repositories.implementations.ProjectRepository;
import services.QuoteService;
import services.implementations.LaborServiceImpl;
import services.implementations.MaterialServiceImpl;
import services.implementations.ProjectServiceImpl;
import services.implementations.QuoteServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CostMenu {
    private static Scanner scanner;
    private final ProjectRepository projectRepository;
    private final ComponentRepository componentRepository;
    private final MaterialServiceImpl materialService;
    private final LaborServiceImpl laborService;
    private final QuoteServiceImpl quoteService;
    private final ProjectServiceImpl projectService;
    private final QuoteMenu quoteMenu;
    private final double discount = 0.0;

    public CostMenu(ProjectRepository projectRepository, ComponentRepository componentRepository, MaterialServiceImpl materialService, LaborServiceImpl laborService, QuoteServiceImpl quoteService, QuoteMenu quoteMenu,ProjectServiceImpl projectService) {
        this.quoteService = quoteService;
        this.quoteMenu = quoteMenu;
        scanner = new Scanner(System.in);
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.componentRepository = componentRepository;
        this.materialService = materialService;
        this.laborService = laborService;
    }

    private static boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.next().trim().toLowerCase();
        return input.equals("o") || input.equals("oui");
    }

    public void save() {
        listAllProjects();
        System.out.println("            --- Calcul du coût total ---            ");

        System.out.print("       Entrez l'ID du projet (UUID) : ");
        String projectIdStr = scanner.nextLine();
        UUID projectId = UUID.fromString(projectIdStr);

        Project project = projectService.findById(projectId).orElseThrow(() ->
                new RuntimeException("       Projet non trouvé"));

        List<Material> materials = materialService.findAllByProjectId(projectId);
        List<Labor> laborList = laborService.findAllByProjectId(projectId);

        double totalMaterialBeforeVat = 0;
        double totalMaterialAfterVat = 0;

        for (Material material : materials) {
            double materialCostBeforeVat = materialService.calculateMaterialBeforeVatRate(material);
            double materialCostAfterVat = materialService.calculateMaterialAfterVatRate(material);

            totalMaterialBeforeVat += materialCostBeforeVat;
            totalMaterialAfterVat += materialCostAfterVat;
        }

        double totalLaborBeforeVat = 0;
        double totalLaborAfterVat = 0;

        for (Labor labor : laborList) {
            double laborCostBeforeVat = laborService.calculateLaborBeforeVat(labor);
            double laborCostAfterVat = laborService.calculateLaborAfterVat(labor);

            totalLaborBeforeVat += laborCostBeforeVat;
            totalLaborAfterVat += laborCostAfterVat;
        }

        double totalCostBeforeMargin = totalMaterialBeforeVat + totalLaborBeforeVat;
        double totalCostAfterVat = totalMaterialAfterVat + totalLaborAfterVat;

        double totalCost = totalCostAfterVat;
        double marginRate = 0.0;


        if (getYesNoInput("       Souhaitez-vous appliquer une marge bénéficiaire au projet ? (o/n) : ")) {
            System.out.print("       Entrez le pourcentage de marge bénéficiaire : ");
            marginRate = scanner.nextDouble();
            scanner.nextLine();
            project.setProfitMargin(marginRate);
            double profitMargin = totalCost * marginRate / 100;
            totalCost += profitMargin;
        }


         projectRepository.updateProject(projectId,project.getProfitMargin(),totalCost);


        System.out.println("\n       ---        Résultat du calcul        ---       ");
        System.out.println("       Nom du projet : " + project.getProjectName());
        System.out.println("       Client : " + project.getClient().getName());
        System.out.println("       Adresse : " + project.getClient().getAddress());
        System.out.println("       Surface : " + project.getSurface() + " m²");
        System.out.println("       ---        Détails du coût        ---       ");
        System.out.println("        des matériaux avant TVA : " + String.format("%.2f", totalMaterialBeforeVat) + " €");
        System.out.println("       Coût des matériaux après TVA : " + String.format("%.2f", totalMaterialAfterVat) + " €");
        System.out.println("       Coût de la main d'œuvre avant TVA : " + String.format("%.2f", totalLaborBeforeVat) + " €");
        System.out.println("       Coût de la main d'œuvre après TVA : " + String.format("%.2f", totalLaborAfterVat) + " €");
        System.out.println("       Coût total avant marge : " + String.format("%.2f", totalCostBeforeMargin) + " €");


        if (project.getClient() != null && project.getClient().isProfessional()) {
            System.out.println("\n       --- Professional Client Discount Applied ---");
            System.out.print("       Enter discount percentage: ");
            double discount = scanner.nextDouble();
            scanner.nextLine();

            // Ensure the discount is valid
            if (discount < 0 || discount > 100) {
                System.out.println("Invalid discount percentage. Please enter a value between 0 and 100.");
                return;
            }

            totalCost *= (1 - discount / 100);  // Apply the discount
            System.out.println("       Discounted Total Cost: " + String.format("%.2f", totalCost) + " €");
        }



        LocalDate issueDateParsed = null;
        while (issueDateParsed == null) {
            System.out.print("       Entrez la date d'émission (aaaa-MM-jj) : ");
            String issueDate = scanner.nextLine();
            if (!issueDate.isEmpty()) {
                try {
                    issueDateParsed = LocalDate.parse(issueDate);
                } catch (DateTimeParseException e) {
                    System.out.println("       Date invalide. Veuillez entrer une date au format aaaa-MM-jj.");
                }
            } else {
                System.out.println("       La date ne peut pas être vide. Veuillez réessayer.");
            }
        }

        // Prompt for validation date with validation
        LocalDate validatedDateParsed = null;
        while (validatedDateParsed == null) {
            System.out.print("       Entrez la date de validation (aaaa-MM-jj) : ");
            String validatedDate = scanner.nextLine();
            if (!validatedDate.isEmpty()) {
                try {
                    validatedDateParsed = LocalDate.parse(validatedDate);
                } catch (DateTimeParseException e) {
                    System.out.println("       Date invalide. Veuillez entrer une date au format aaaa-MM-jj.");
                }
            } else {
                System.out.println("       La date ne peut pas être vide. Veuillez réessayer.");
            }
        }


        Quote quote = new Quote(UUID.randomUUID(), totalCost, issueDateParsed, validatedDateParsed, false, project);
        quoteService.save(quote);

        System.out.print("       Souhaitez-vous accepter le devis ? (Oui/Non) : ");
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "oui":
            case "o":
                quoteService.updateDevisStatus(quote.getId());
                projectRepository.updateStatus(projectId,ProjectStatus.COMPLETED.name());
                System.out.println("       Devis accepté. Projet marqué comme TERMINÉ.");
                break;
            case "non":
            case "n":
                projectRepository.updateStatus(projectId,ProjectStatus.CANCELLED.name());
                System.out.println("       Devis rejeté. Projet marqué comme ANNULÉ.");
                break;
            default:
                System.out.println("       Choix invalide. Veuillez entrer 'Oui' ou 'Non'.");
        }

        try {
            quoteMenu.findQuotesByProject(projectId);
        }  catch (QuotesNotFoundException quoteNotFoundException) {
            System.out.println(quoteNotFoundException.getMessage());
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
    private void listAllProjects() {
        List<Project> projects = projectService.findAll();

        if (projects.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
        } else {
            displayProjectTable(projects);
        }
    }

}
