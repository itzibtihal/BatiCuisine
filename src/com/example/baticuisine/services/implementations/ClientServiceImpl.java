package com.example.baticuisine.services.implementations;

import com.example.baticuisine.domain.entities.Client;
import com.example.baticuisine.exceptions.InvalidClientException;
import com.example.baticuisine.repositories.implementations.ClientRepository;

import com.example.baticuisine.services.ClientService;
import com.example.baticuisine.validator.ClientValidator;

import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;

    public ClientServiceImpl(ClientRepository clientRepository, ClientValidator clientValidator) {
        this.clientRepository = clientRepository;
        this.clientValidator = new ClientValidator();
    }


    @Override
    public Client save(Client client) throws InvalidClientException {
        clientValidator.validate(client);
        return clientRepository.save(client);
    }



    @Override
    public Optional<Client> findById(Client client) throws InvalidClientException {
        clientValidator.validateId(client.getId());
        return clientRepository.findById(client);
    }


    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }


    @Override
    public Client update(Client client) throws InvalidClientException {
        clientValidator.validate(client);
        return clientRepository.update(client);
    }


    @Override
    public boolean delete(Client client) throws InvalidClientException {
        clientValidator.validateId(client.getId());
        return clientRepository.delete(client);
    }


    @Override
    public List<Client> findClientsByName(String name) throws InvalidClientException {
        clientValidator.validateName(name);
        return clientRepository.findByName(name);
    }

}
