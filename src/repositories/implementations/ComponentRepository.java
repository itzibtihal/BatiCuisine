package repositories.implementations;

import domain.entities.Component;
import repositories.interfaces.ComponentInterface;

import java.util.List;
import java.util.Optional;

public class ComponentRepository implements ComponentInterface<Component> {
    @Override
    public Component save(Component component) {
        return null;
    }

    @Override
    public Optional<Component> findById(Component component) {
        return Optional.empty();
    }

    @Override
    public List<Component> findAll() {
        return null;
    }

    @Override
    public Component update(Component component) {
        return null;
    }

    @Override
    public boolean delete(Component component) {
        return false;
    }
}
