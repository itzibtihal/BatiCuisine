package domain.entities;

import java.util.UUID;

public class Component {
    private UUID id;
    private String name;
    private String componentType;
    private double vatRate;
    private Project project;

    public Component(UUID id, String name, String componentType, double vatRate, Project project) {
        this.id = id;
        this.name = name;
        this.componentType = componentType;
        this.vatRate = vatRate;
        this.project = project;
    }

    public Component() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", componentType='" + componentType + '\'' +
                ", vatRate=" + vatRate +
                ", project=" + project +
                '}';
    }
}
