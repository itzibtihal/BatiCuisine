package repositories.interfaces;

import domain.entities.Labor;

import java.util.List;
import java.util.Optional;

public interface LaborInterface <T extends Labor> extends CrudInterface<Labor> {
    @Override
    public Labor save(Labor labor);

    @Override
    public Optional<Labor> findById(Labor labor);

    @Override
    public List<Labor> findAll();

    @Override
    public Labor update(Labor entity);

    @Override
    public boolean delete(Labor entity);

}
