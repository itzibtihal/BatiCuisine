package domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client {
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private boolean isProfessional;
    private List<Project> listProjects;

    public List<Project> getListProjects() {
        return listProjects;
    }

    public void setListProjects(List<Project> listProjects) {
        this.listProjects = listProjects;
    }

    public Client(UUID id, String name, String address, String phone, boolean isProfessional) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.isProfessional = isProfessional;
        this.listProjects = new ArrayList<>();
    }

    public Client() {
        this.listProjects = new ArrayList<>();
    }

    public UUID getId() {  // Updated getter for UUID
        return id;
    }

    public void setId(UUID id) {  // Updated setter for UUID
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {  // Fixed method name
        return address;
    }

    public void setAddress(String address) {  // Fixed method name
        this.address = address;
    }

    public String getPhone() {  // Fixed method name
        return phone;
    }

    public void setPhone(String phone) {  // Fixed method name
        this.phone = phone;
    }

    public boolean isProfessional() {
        return isProfessional;
    }

    public void setProfessional(boolean professional) {
        isProfessional = professional;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", isProfessional=" + isProfessional +
                '}';
    }
}
