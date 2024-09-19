package views.submenu;

import domain.entities.Component;
import domain.entities.Material;
import domain.entities.Project;
import domain.enums.ComponentType;
import services.ComponentService;
import services.MaterialService;
import services.implementations.MaterialServiceImpl;

import java.util.Scanner;
import java.util.UUID;

public class MaterialMenu {

    private final MaterialServiceImpl materialService;
    private final ComponentService componentService;
    private final Scanner scanner;

    public MaterialMenu(MaterialServiceImpl materialService, ComponentService componentService) {
        this.materialService = materialService;
        this.componentService = componentService;
        this.scanner = new Scanner(System.in);
    }

    public Material addMaterial(Project project) {
        String continueChoice;
        Material material = null;

        do {
            System.out.println("\n            🏗️ Ajouter un Matériau 🏗️");

            System.out.print("        Entrez le nom du matériau : ");
            String name = scanner.nextLine();

            System.out.print("        Entrez la quantité de ce matériau : ");
            double quantity = scanner.nextDouble();

            System.out.print("        Entrez le coût unitaire du matériau (€/m² ou €/litre) : ");
            double unitCost = scanner.nextDouble();

            System.out.print("        Entrez le coût de transport du matériau (€) : ");
            double transportCost = scanner.nextDouble();

            System.out.print("        Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double coefficientQuality = scanner.nextDouble();

            System.out.print("        Entrez le taux de TVA du matériau : ");
            double vatRate = scanner.nextDouble();
            scanner.nextLine();

            Component component = new Component();
            component.setName(name);
            component.setComponentType(ComponentType.MATERIAL.name());
            component.setVatRate(vatRate);
            component.setProject(project);

            Component savedComponent = componentService.save(component);

            UUID materialId = UUID.randomUUID();

            material = new Material(
                    materialId,
                    name,
                    "Material",
                    vatRate,
                    project,
                    unitCost,
                    quantity,
                    transportCost,
                    coefficientQuality,
                    savedComponent
            );

            materialService.save(material);

            System.out.println("\n✅ Matériau ajouté avec succès !\n");
            System.out.println(material);

            System.out.print("👉 Souhaitez-vous ajouter un autre matériau ? (o/n) : ");
            continueChoice = scanner.nextLine().trim().toLowerCase();

        } while (continueChoice.equals("o"));

        return material;
    }




    private String MaterialTable(Material material) {
        return "📋 Informations sur le Matériau\n" +
                "📛 Nom : " + material.getName() + "\n" +
                "📦 Quantité : " + material.getQuantity() + "\n" +
                "💰 Coût Unitaire : €" + material.getUnitCost() + "\n" +
                "🚚 Coût de Transport : €" + material.getTransportCost() + "\n" +
                "🔧 Coefficient de Qualité : " + material.getCoefficientQuality() + "\n" +
                "📊 Taux de TVA : " + material.getVatRate() + "%\n";
    }

}
