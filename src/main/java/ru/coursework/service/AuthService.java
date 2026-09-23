package ru.coursework.service;

import org.mindrot.jbcrypt.BCrypt;
import ru.coursework.model.Role;
import ru.coursework.model.User;
import ru.coursework.model.UserRole;
import ru.coursework.repository.RoleRepository;
import ru.coursework.repository.UserRepository;
import ru.coursework.repository.UserRoleRepository;

public class AuthService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public AuthService() {
        repository = new UserRepository();
        roleRepository = new RoleRepository();
        userRoleRepository = new UserRoleRepository();
    }

    public boolean register(User user, String password) {
        if (repository.findByLogin(user.getLogin()) != null) {
            return false;
        }

        if (!ValidationService.isValidPassword(password)) {
            return false;
        }

        String hash = BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );

        user.setPasswordHash(hash);

        repository.save(user);

        Role guestRole = roleRepository.findByName("GUEST");

        if (guestRole != null) {
            userRoleRepository.save(
                    new UserRole(
                            user.getId(),
                            guestRole.getId()
                    )
            );
        }

        return true;
    }

    public User login(String login, String password) {
        User user = repository.findByLogin(login);

        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }

    public boolean changePassword(User user, String newPassword) {
        if (!ValidationService.isValidPassword(newPassword)) {
            return false;
        }

        user.setPasswordHash(
                BCrypt.hashpw(
                        newPassword,
                        BCrypt.gensalt()
                )
        );

        repository.update(user);

        return true;
    }

    public boolean changeLogin(User user, String newLogin) {
        User existing = repository.findByLogin(newLogin);

        if (existing != null
                && !existing.getId().equals(user.getId())) {
            return false;
        }

        user.setLogin(newLogin);

        repository.update(user);

        return true;
    }
}