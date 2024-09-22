package services;

import domain.entities.Labor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LaborService {

    Labor save(Labor labor) throws IllegalArgumentException;

    Optional<Labor> findById(UUID id) throws IllegalArgumentException;

    List<Labor> findAll();

    Labor update(Labor labor) throws IllegalArgumentException;

    boolean delete(Labor labor) throws IllegalArgumentException;

}
