package services;

import domain.entities.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComponentService {
    Component save(Component component);

    Optional<Component> findById(Component component);

    List<Component> findAll();

    Component update(Component component);

    boolean delete(Component component);

    void updateFieldsComponent(UUID componentId, double vta);

}
