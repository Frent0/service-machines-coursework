package ru.coursework.service;

import ru.coursework.model.Role;
import ru.coursework.repository.UserRoleRepository;

import java.util.List;

public class RoleService {

    private final UserRoleRepository repository;

    public RoleService() {
        repository = new UserRoleRepository();
    }

    public List<Role> getUserRoles(Integer userId) {
        return repository.findRolesByUserId(userId);
    }

    public boolean hasRole(Integer userId, String roleName) {
        return getUserRoles(userId).stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }
}