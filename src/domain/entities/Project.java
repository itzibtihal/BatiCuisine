package domain.entities;

import domain.enums.ProjectStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private UUID id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    private ProjectStatus status;
    private double surface;
    private Client client;
    private List<Component> components;


    public Project(UUID id, String projectName, double profitMargin, double totalCost, String status, double surface, Client client) {
        this.id = id;
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.components = new ArrayList<>();
        this.status = ProjectStatus.valueOf(status);
        this.surface = surface;
        this.client = client;
    }

    public Project() {
        this.components = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", profitMargin=" + profitMargin +
                ", totalCost=" + totalCost +
                ", status=" + status +
                ", surface=" + surface +
                ", client=" + client +
                ", components=" + components +
                '}';
    }
}
