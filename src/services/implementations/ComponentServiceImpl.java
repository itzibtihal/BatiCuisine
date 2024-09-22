package services.implementations;


import domain.entities.Component;
import repositories.implementations.ComponentRepository;
import services.ComponentService;
import validator.ComponentValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentValidator componentValidator;

    public ComponentServiceImpl(ComponentRepository componentRepository, ComponentValidator componentValidator) {
        this.componentRepository = componentRepository;
        this.componentValidator = componentValidator;
    }

    @Override
    public Component save(Component component) {
        componentValidator.validate(component);
        return componentRepository.save(component);
    }

    @Override
    public Optional<Component> findById(Component component) {
        return componentRepository.findById(component);
    }

    @Override
    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    @Override
    public Component update(Component component) {
        componentValidator.validate(component);
        return componentRepository.update(component);
    }

    @Override
    public boolean delete(Component component) {
        return componentRepository.delete(component);
    }

    @Override
    public void updateFieldsComponent(UUID componentId, double vta) {
        componentRepository.updateFieldsComponent(componentId, vta);
    }
}
