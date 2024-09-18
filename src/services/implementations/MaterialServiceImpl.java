package services.implementations;


import domain.entities.Material;
import repositories.implementations.MaterialRepository;
import services.MaterialService;
import validator.MaterialValidator;



import java.util.List;
import java.util.Optional;

public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialValidator materialValidator;

    public MaterialServiceImpl(MaterialRepository materialRepository, MaterialValidator materialValidator) {
        this.materialRepository = materialRepository;
        this.materialValidator = materialValidator;
    }

    @Override
    public Material save(Material material) {
        materialValidator.validate(material);
        return materialRepository.save(material);
    }

    @Override
    public Optional<Material> findById(Material material) {
        materialValidator.validateId(material);
        return materialRepository.findById(material);
    }

    @Override
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Override
    public Material update(Material material) {
        materialValidator.validate(material);
        return materialRepository.update(material);
    }

    @Override
    public boolean delete(Material material) {
        materialValidator.validateId(material);
        return materialRepository.delete(material);
    }
}

