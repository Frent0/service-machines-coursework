package ru.coursework.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.coursework.model.User;
import ru.coursework.service.AuthService;

public class LoginView {

    private final VBox view;
    private final TextField loginField;
    private final PasswordField passwordField;
    private final Label messageLabel;
    private final AuthService authService;

    public LoginView() {
        authService = new AuthService();

        loginField = new TextField();
        loginField.setPromptText("Логин");

        passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button loginButton = new Button("Войти");
        Button registerButton = new Button("Регистрация");

        messageLabel = new Label();

        loginButton.setOnAction(e -> login());
        registerButton.setOnAction(e -> openRegisterWindow());

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(loginButton, registerButton);

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(
                new Label("Авторизация"),
                loginField,
                passwordField,
                buttons,
                messageLabel
        );
    }

    private void login() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        User user = authService.login(login, password);

        if (user != null) {
            openMainWindow(user);
        } else {
            messageLabel.setText("Неверный логин или пароль");
        }
    }

    private void openRegisterWindow() {
        Stage stage = new Stage();

        RegisterView registerView = new RegisterView();

        stage.setTitle("Регистрация");
        stage.setScene(new Scene(registerView.getView(), 450, 300));
        stage.show();
    }

    private void openMainWindow(User user) {
        Stage stage = (Stage) view.getScene().getWindow();

        MainView mainView = new MainView(user);

        stage.setTitle("Техническое обслуживание станков");
        stage.setScene(new Scene(mainView.getView(), 1000, 650));
        stage.show();
    }

    public VBox getView() {
        return view;
    }
}