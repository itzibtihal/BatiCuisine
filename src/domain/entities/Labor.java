package domain.entities;

import java.util.UUID;

public class Labor extends Component {
    private double hourlyRate;
    private double workHours;
    private double workerProductivity;
    private Component component;

    public Labor(UUID id, String name, String componentType, double vatRate, double hourlyRate, double workHours, double workerProductivity, Project project, Component component) {
        super(id, name, componentType, vatRate, project);
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
        this.component = component;
    }

    public Labor() {
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public double getWorkerProductivity() {
        return workerProductivity;
    }

    public void setWorkerProductivity(double workerProductivity) {
        this.workerProductivity = workerProductivity;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public String toString() {
        return "Labor{" +
                "hourlyRate=" + hourlyRate +
                ", workHours=" + workHours +
                ", workerProductivity=" + workerProductivity +
                ", component=" + component +
                '}';
    }
}
