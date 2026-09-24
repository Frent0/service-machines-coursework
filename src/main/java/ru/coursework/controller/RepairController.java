package ru.coursework.controller;

import ru.coursework.model.Repair;
import ru.coursework.repository.RepairRepository;

import java.util.List;

public class RepairController {

    private final RepairRepository repository;

    public RepairController() {
        repository = new RepairRepository();
    }

    public List<Repair> getRepairs() {
        return repository.findAll();
    }

    public void addRepair(Repair repair) {
        repository.save(repair);
    }

    public void deleteRepair(Repair repair) {
        repository.delete(repair);
    }

    public void updateRepair(Repair repair) {
        repository.update(repair);
    }

    public List<Repair> getRepairsByClient(Integer clientId) {
        return repository.findByClientId(clientId);
    }
}