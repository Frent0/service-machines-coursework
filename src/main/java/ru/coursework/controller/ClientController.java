package ru.coursework.controller;

import ru.coursework.model.Client;
import ru.coursework.repository.ClientRepository;

import java.util.List;

public class ClientController {

    private final ClientRepository repository;

    public ClientController() {
        repository = new ClientRepository();
    }

    public List<Client> getClients() {
        return repository.findAll();
    }

    public void addClient(Client client) {
        repository.save(client);
    }

    public void deleteClient(Client client) {
        repository.delete(client);
    }

    public void updateClient(Client client) {
        repository.update(client);
    }
}