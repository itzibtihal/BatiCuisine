package services;

import domain.entities.Client;
import exceptions.InvalidClientException;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client save(Client client) throws InvalidClientException;

    Optional<Client> findById(Client client) throws InvalidClientException;

    List<Client> findAll();

    Client update(Client client) throws InvalidClientException;

    boolean delete(Client client) throws InvalidClientException;

    List<Client> findClientsByName(String name) throws InvalidClientException;
}
