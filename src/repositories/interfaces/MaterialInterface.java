package repositories.interfaces;

import domain.entities.Material;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialInterface <T extends Material> extends CrudInterface<Material>{
    @Override
    public Material save(Material entity);

    @Override
    public Optional<Material> findById(Material material);

    @Override
    public List<Material> findAll();

    @Override
    public Material update(Material entity);

    @Override
    public boolean delete(Material entity);

    List<Material> findAllByProjectId(UUID id);
}
