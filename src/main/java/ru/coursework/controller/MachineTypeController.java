package ru.coursework.controller;

import ru.coursework.model.MachineType;
import ru.coursework.repository.MachineTypeRepository;

import java.util.List;

public class MachineTypeController {

    private final MachineTypeRepository repository;

    public MachineTypeController() {
        repository = new MachineTypeRepository();
    }

    public List<MachineType> getMachineTypes() {
        return repository.findAll();
    }

    public void addMachineType(MachineType machineType) {
        repository.save(machineType);
    }

    public void deleteMachineType(MachineType machineType) {
        repository.delete(machineType);
    }

    public void updateMachineType(MachineType machineType) {
        repository.update(machineType);
    }
}