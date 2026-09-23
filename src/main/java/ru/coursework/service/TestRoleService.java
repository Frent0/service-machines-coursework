package ru.coursework.service;

import ru.coursework.model.Role;

import java.util.List;

public class TestRoleService {

    public static void main(String[] args) {
        RoleService roleService = new RoleService();

        List<Role> roles = roleService.getUserRoles(3);

        for (Role role : roles) {
            System.out.println("Роль пользователя: " + role.getName());
        }

        System.out.println(
                "ADMIN: " + roleService.hasRole(3, "ADMIN")
        );
    }
}