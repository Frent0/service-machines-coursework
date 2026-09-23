package ru.coursework.service;

import ru.coursework.model.User;

public class AccessService {

    private final RoleService roleService;

    public AccessService() {
        roleService = new RoleService();
    }

    public boolean isAdmin(User user) {
        return roleService.hasRole(user.getId(), "ADMIN");
    }

    public boolean isClient(User user) {
        return roleService.hasRole(user.getId(), "CLIENT");
    }

    public boolean isGuest(User user) {
        return roleService.hasRole(user.getId(), "GUEST");
    }
}