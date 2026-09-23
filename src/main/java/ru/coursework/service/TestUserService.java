package ru.coursework.service;

import ru.coursework.model.User;

public class TestUserService {

    public static void main(String[] args) {
        UserControllerTest();
    }

    private static void UserControllerTest() {
        AuthService authService = new AuthService();

        ru.coursework.controller.UserController controller =
                new ru.coursework.controller.UserController();

        User user = controller.findByLogin("admin");

        if (user == null) {
            System.out.println("Пользователь admin не найден");
            return;
        }

        String password = "Admin123!";

        user.setPasswordHash(
                org.mindrot.jbcrypt.BCrypt.hashpw(
                        password,
                        org.mindrot.jbcrypt.BCrypt.gensalt()
                )
        );

        controller.updateUser(user);

        System.out.println("Пароль admin обновлён");
    }
}