package services;

import domain.entities.Client;
import exceptions.InvalidClientException;
import repositories.implementations.ClientRepository;
import validator.ClientValidator;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientValidator clientValidator;

    public ClientService(ClientRepository clientRepository, ClientValidator clientValidator) {
        this.clientRepository = clientRepository;
        this.clientValidator = new ClientValidator();
    }

    public void save(Client client) throws InvalidClientException {
        clientValidator.validate(client);
        clientRepository.save(client);
    }


    public Optional<Client> findById(Client client) throws InvalidClientException {
        clientValidator.validateId(client.getId());
        return clientRepository.findById(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void update(Client client) throws InvalidClientException {
        clientValidator.validate(client);
        clientRepository.update(client);
    }

    public void delete(Client client) throws InvalidClientException {
        clientValidator.validateId(client.getId());
        clientRepository.delete(client);
    }

    public List<Client> findClientsByName(String name) throws InvalidClientException {
        clientValidator.validateName(name);
        return clientRepository.findByName(name);
    }
}
