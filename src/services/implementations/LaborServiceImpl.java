package services.implementations;

import domain.entities.Labor;
import repositories.implementations.ComponentRepository;
import repositories.implementations.LaborRepository;
import services.LaborService;
import validator.LaborValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LaborServiceImpl implements LaborService {
    private final LaborRepository laborRepository;
    private final LaborValidator laborValidator;
    private ComponentRepository componentRepository;

    public LaborServiceImpl(LaborRepository laborRepository, LaborValidator laborValidator,ComponentRepository componentRepository) {
        this.laborRepository = laborRepository;
        this.laborValidator = laborValidator;
        this.componentRepository = componentRepository;
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

    @Override
    public List<Labor> findAllByProjectId(UUID projectId) {
        return laborRepository.findAllByProjectId(projectId);
    }

    public double calculateLabor(Labor labor) {
        return labor.getWorkHours() * labor.getHourlyRate() * labor.getWorkerProductivity();
    }

    public double getLaborVatRate(Labor labor) {
        return componentRepository.findTvaForComponent(labor.getId());
    }

    public double calculateLaborBeforeVat(Labor labor) {
        return calculateLabor(labor);
    }

    private double applyVat(double cost, double vatRate) {
        return cost + (cost * vatRate / 100);
    }

    public double calculateLaborAfterVat(Labor labor) {
        double costBeforeVat = calculateLaborBeforeVat(labor);
        double vatRate = getLaborVatRate(labor);
        return applyVat(costBeforeVat, vatRate);
    }



}
