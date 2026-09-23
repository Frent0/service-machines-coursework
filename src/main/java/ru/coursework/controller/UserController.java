package ru.coursework.controller;

import ru.coursework.model.User;
import ru.coursework.repository.UserRepository;

import java.util.List;

public class UserController {

    private final UserRepository repository;

    public UserController() {
        repository = new UserRepository();
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public void addUser(User user) {
        repository.save(user);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    public void updateUser(User user) {
        repository.update(user);
    }
}