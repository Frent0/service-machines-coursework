package ru.coursework.controller;

import ru.coursework.model.RepairType;
import ru.coursework.repository.RepairTypeRepository;

import java.util.List;

public class RepairTypeController {

    private final RepairTypeRepository repository;

    public RepairTypeController() {
        repository = new RepairTypeRepository();
    }

    public List<RepairType> getRepairTypes() {
        return repository.findAll();
    }

    public void addRepairType(RepairType repairType) {
        repository.save(repairType);
    }

    public void deleteRepairType(RepairType repairType) {
        repository.delete(repairType);
    }

    public void updateRepairType(RepairType repairType) {
        repository.update(repairType);
    }
}