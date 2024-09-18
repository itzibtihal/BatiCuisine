package services;

import domain.entities.Labor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LaborService {

    Labor saveLabor(Labor labor) throws IllegalArgumentException;

    Optional<Labor> findLaborById(UUID id) throws IllegalArgumentException;

    List<Labor> findAllLabor();

    Labor updateLabor(Labor labor) throws IllegalArgumentException;

    boolean deleteLabor(Labor labor) throws IllegalArgumentException;

}
