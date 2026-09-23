package ru.coursework.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.coursework.model.User;
import ru.coursework.service.AuthService;
import ru.coursework.service.ValidationService;

public class RegisterView {

    private final VBox view;

    private final TextField loginField;
    private final PasswordField passwordField;
    private final PasswordField repeatPasswordField;

    private final Label messageLabel;

    private final AuthService authService;

    public RegisterView() {
        authService = new AuthService();

        loginField = new TextField();
        loginField.setPromptText("Логин");

        passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        repeatPasswordField = new PasswordField();
        repeatPasswordField.setPromptText("Повторите пароль");

        Button registerButton = new Button("Зарегистрироваться");

        messageLabel = new Label();

        registerButton.setOnAction(e -> register());

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(
                new Label("Регистрация"),
                loginField,
                passwordField,
                repeatPasswordField,
                registerButton,
                messageLabel
        );
    }

    private void register() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (login.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        if (!password.equals(repeatPassword)) {
            messageLabel.setText("Пароли не совпадают");
            return;
        }

        if (!ValidationService.isValidPassword(password)) {
            messageLabel.setText(
                    "Пароль: минимум 8 символов, цифра, заглавная буква и спецсимвол"
            );
            return;
        }

        User user = new User();
        user.setLogin(login);

        boolean registered = authService.register(user, password);

        if (registered) {
            messageLabel.setText("Регистрация выполнена");
        } else {
            messageLabel.setText("Такой логин уже существует");
        }
    }

    public VBox getView() {
        return view;
    }
}