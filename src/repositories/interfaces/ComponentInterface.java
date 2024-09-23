package repositories.interfaces;

import domain.entities.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComponentInterface  <T extends Component> extends CrudInterface<Component>{

    @Override
    public Component save(Component component);

    @Override
    public Optional<Component> findById(Component component);

    @Override
    public List<Component> findAll();

    @Override
    public Component update(Component component);

    @Override
    public boolean delete(Component component);

    public void updateFieldsComponent(UUID componentId, double vta);

    public double findTvaForComponent(UUID id);
}
