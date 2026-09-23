package ru.coursework.controller;

import ru.coursework.model.Role;
import ru.coursework.repository.RoleRepository;

import java.util.List;

public class RoleController {

    private final RoleRepository repository;

    public RoleController() {
        repository = new RoleRepository();
    }

    public List<Role> getRoles() {
        return repository.findAll();
    }

    public void addRole(Role role) {
        repository.save(role);
    }

    public void deleteRole(Role role) {
        repository.delete(role);
    }

    public void updateRole(Role role) {
        repository.update(role);
    }
}