package services.implementations;

import domain.entities.Labor;
import repositories.implementations.LaborRepository;
import services.LaborService;
import validator.LaborValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LaborServiceImpl implements LaborService {
    private final LaborRepository laborRepository;
    private final LaborValidator laborValidator;

    public LaborServiceImpl(LaborRepository laborRepository, LaborValidator laborValidator) {
        this.laborRepository = laborRepository;
        this.laborValidator = laborValidator;
    }



    @Override
    public Labor save(Labor labor) throws IllegalArgumentException {
        laborValidator.validateLabor(labor);
        return laborRepository.save(labor);
    }



    @Override
    public Optional<Labor> findById(UUID id) throws IllegalArgumentException {
        laborValidator.validateId(id);
        Labor labor = new Labor();
        labor.setId(id);
        return laborRepository.findById(labor);
    }


    @Override
    public List<Labor> findAll() {
        return laborRepository.findAll();
    }


    @Override
    public Labor update(Labor labor) throws IllegalArgumentException {
        laborValidator.validateLabor(labor);
        return laborRepository.update(labor);
    }



    @Override
    public boolean delete(Labor labor) throws IllegalArgumentException {
        laborValidator.validateLabor(labor);
        return laborRepository.delete(labor);
    }
}
