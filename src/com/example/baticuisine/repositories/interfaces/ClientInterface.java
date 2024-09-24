package com.example.baticuisine.repositories.interfaces;

import java.util.List;
import java.util.Optional;

public interface ClientInterface <Client>  extends CrudInterface<Client> {

    @Override
    public Client save(Client client);

    @Override
    public Optional<Client> findById(Client client);

    @Override
    public List<Client> findAll();

    @Override
    public Client update(Client client);

    @Override
    public boolean delete(Client client);

    public List<Client> findByName(String name);
}
