package ru.coursework.controller;

import ru.coursework.model.Machine;
import ru.coursework.repository.MachineRepository;

import java.util.List;

public class MachineController {

    private final MachineRepository repository;

    public MachineController() {
        repository = new MachineRepository();
    }

    public List<Machine> getMachines() {
        return repository.findAll();
    }

    public void addMachine(Machine machine) {
        repository.save(machine);
    }

    public void deleteMachine(Machine machine) {
        repository.delete(machine);
    }

    public void updateMachine(Machine machine) {
        repository.update(machine);
    }
}