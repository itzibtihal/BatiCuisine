package services;

import domain.entities.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    Material save(Material material);

    Optional<Material> findById(Material material);

    List<Material> findAll();

    Material update(Material material);

    boolean delete(Material material);
}
