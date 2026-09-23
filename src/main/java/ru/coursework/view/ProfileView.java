package ru.coursework.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.coursework.model.User;
import ru.coursework.service.AuthService;
import ru.coursework.service.ValidationService;

public class ProfileView {

    private final VBox view;

    private final TextField loginField;
    private final PasswordField oldPasswordField;
    private final PasswordField newPasswordField;
    private final PasswordField repeatPasswordField;

    private final Label messageLabel;

    private final AuthService authService;
    private final User user;

    public ProfileView(User user) {
        this.user = user;
        authService = new AuthService();

        loginField = new TextField(user.getLogin());
        loginField.setPromptText("Логин");

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Текущий пароль");

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Новый пароль");

        repeatPasswordField = new PasswordField();
        repeatPasswordField.setPromptText("Повторите новый пароль");

        Button saveButton = new Button("Сохранить");

        messageLabel = new Label();

        saveButton.setOnAction(e -> save());

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(
                new Label("Мой профиль"),
                loginField,
                oldPasswordField,
                newPasswordField,
                repeatPasswordField,
                saveButton,
                messageLabel
        );
    }

    private void save() {
        String login = loginField.getText().trim();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String repeatPassword = repeatPasswordField.getText();

        if (login.isEmpty() || oldPassword.isEmpty()
                || newPassword.isEmpty() || repeatPassword.isEmpty()) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        User currentUser = authService.login(
                user.getLogin(),
                oldPassword
        );

        if (currentUser == null) {
            messageLabel.setText("Неверный текущий пароль");
            return;
        }

        if (!newPassword.equals(repeatPassword)) {
            messageLabel.setText("Новые пароли не совпадают");
            return;
        }

        if (!ValidationService.isValidPassword(newPassword)) {
            messageLabel.setText(
                    "Пароль: минимум 8 символов, цифра, заглавная буква и спецсимвол"
            );
            return;
        }

        if (!authService.changeLogin(currentUser, login)) {
            messageLabel.setText("Такой логин уже используется");
            return;
        }

        if (!authService.changePassword(currentUser, newPassword)) {
            messageLabel.setText("Не удалось изменить пароль");
            return;
        }

        user.setLogin(currentUser.getLogin());

        messageLabel.setText("Данные сохранены");

        oldPasswordField.clear();
        newPasswordField.clear();
        repeatPasswordField.clear();
    }

    public VBox getView() {
        return view;
    }
}