package views.submenu;

import domain.entities.Component;
import domain.entities.Labor;
import domain.entities.Project;
import domain.enums.ComponentType;

import services.implementations.ComponentServiceImpl;
import services.implementations.LaborServiceImpl;

import java.util.Scanner;
import java.util.UUID;

public class LaborMenu {
    private final LaborServiceImpl laborService;
    private final ComponentServiceImpl componentService;
    private final Scanner scanner;

    public LaborMenu(LaborServiceImpl laborService, ComponentServiceImpl componentService) {
        this.laborService = laborService;
        this.componentService = componentService;
        this.scanner = new Scanner(System.in);
    }

    public Labor addLabor(Project project) {
        String continueChoice;
        Labor labor = null;

        do {
            System.out.println("\n 🔨 Ajouter une Main-d'œuvre 🔨");

            System.out.print("👷 Entrez le nom de la main-d'œuvre : ");
            String name = scanner.nextLine();

            System.out.print("📊 Entrez le taux de TVA de la main-d'œuvre : ");
            double vatRate = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("💰 Entrez le taux horaire pour cette main-d'œuvre (€/h) : ");
            double hourlyRate = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("⏱️ Entrez le nombre d'heures travaillées : ");
            double workHours = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("🔧 Entrez le coefficient de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double workerProductivity = scanner.nextDouble();
            scanner.nextLine();

            Component component = new Component();
            component.setName(name);
            component.setComponentType(ComponentType.LABOR.name());
            component.setVatRate(vatRate);
            component.setProject(project);

            Component savedComponent = componentService.save(component);

            UUID laborId = UUID.randomUUID();
            labor = new Labor(
                    laborId,
                    name,
                    "labor",
                    vatRate,
                    hourlyRate,
                    workHours,
                    workerProductivity,
                    project,
                    savedComponent
            );

            laborService.save(labor);

            System.out.println("\n✅ Main-d'œuvre ajoutée avec succès !\n");
            System.out.println(labor);

            System.out.print("👉 Souhaitez-vous ajouter une autre main-d'œuvre ? (o/n) : ");
            continueChoice = scanner.nextLine().trim().toLowerCase();

        } while (continueChoice.equals("o"));

        return labor;
    }




}
