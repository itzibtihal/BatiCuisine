package validator;

import domain.entities.Project;

public class ProjectValidator {
    public boolean isValid(Project project) {
        return isNameValid(project.getProjectName()) &&
                isProfitMarginValid(project.getProfitMargin()) &&
                isTotalCostValid(project.getTotalCost()) &&
                isSurfaceValid(project.getSurface());
    }

    private boolean isNameValid(String projectName) {
        return projectName != null && !projectName.trim().isEmpty();
    }

    private boolean isProfitMarginValid(double profitMargin) {
        return profitMargin >= 0;
    }

    private boolean isTotalCostValid(double totalCost) {
        return totalCost >= 0;
    }

    private boolean isSurfaceValid(double surface) {
        return surface > 0;
    }
}
