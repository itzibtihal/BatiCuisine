package validator;

import domain.entities.Component;

import java.util.UUID;

public class ComponentValidator {

    public void validate(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        if (component.getId() == null || component.getId().toString().isEmpty()) {
            throw new IllegalArgumentException("Component ID cannot be null or empty");
        }
        if (component.getName() == null || component.getName().isEmpty()) {
            throw new IllegalArgumentException("Component name cannot be null or empty");
        }
        if (component.getComponentType() == null || component.getComponentType().isEmpty()) {
            throw new IllegalArgumentException("Component type cannot be null or empty");
        }
        if (component.getVatRate() < 0) {
            throw new IllegalArgumentException("VAT rate cannot be negative");
        }
        if (component.getProject() == null || component.getProject().getId() == null) {
            throw new IllegalArgumentException("Component must be associated with a valid project");
        }
    }
}
