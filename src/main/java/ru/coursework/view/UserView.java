package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import ru.coursework.controller.UserController;
import ru.coursework.model.User;

public class UserView {

    private final BorderPane view;
    private final TableView<User> table;
    private final UserController controller;

    public UserView() {
        controller = new UserController();

        table = new TableView<>();

        TableColumn<User, Integer> idColumn =
                new TableColumn<>("ID");

        TableColumn<User, String> loginColumn =
                new TableColumn<>("Логин");

        idColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getId()
                )
        );

        loginColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getLogin()
                )
        );

        table.getColumns().addAll(idColumn, loginColumn);

        Button addButton = new Button("Добавить");
        Button editButton = new Button("Изменить логин");
        Button deleteButton = new Button("Удалить");
        Button rolesButton = new Button("Роли");
        Button refreshButton = new Button("Обновить");

        addButton.setOnAction(e -> addUser());
        editButton.setOnAction(e -> editUser());
        deleteButton.setOnAction(e -> deleteUser());
        rolesButton.setOnAction(e -> openRoles());
        refreshButton.setOnAction(e -> loadUsers());

        HBox buttons = new HBox(
                10,
                addButton,
                editButton,
                deleteButton,
                rolesButton,
                refreshButton
        );

        buttons.setPadding(new Insets(10));

        view = new BorderPane();
        view.setCenter(table);
        view.setBottom(buttons);

        loadUsers();
    }

    private void loadUsers() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getUsers()
                )
        );
    }

    private void addUser() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Добавление пользователя");

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        javafx.scene.layout.VBox box =
                new javafx.scene.layout.VBox(10);

        box.setPadding(new Insets(10));

        box.getChildren().addAll(
                new Label("Логин"),
                loginField,
                new Label("Пароль"),
                passwordField
        );

        dialog.getDialogPane().setContent(box);

        ButtonType saveButton =
                new ButtonType(
                        "Сохранить",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButton,
                ButtonType.CANCEL
        );

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return loginField.getText().trim()
                        + "\n"
                        + passwordField.getText();
            }

            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String[] data = result.split("\n", 2);

            if (data.length != 2
                    || data[0].isEmpty()
                    || data[1].isEmpty()) {

                showMessage("Заполните все поля");
                return;
            }

            User user = new User();
            user.setLogin(data[0]);

            ru.coursework.service.AuthService authService =
                    new ru.coursework.service.AuthService();

            if (!ru.coursework.service.ValidationService
                    .isValidPassword(data[1])) {

                showMessage(
                        "Пароль должен содержать минимум 8 символов, "
                                + "цифру, заглавную букву и спецсимвол"
                );

                return;
            }

            if (!authService.register(user, data[1])) {
                showMessage("Такой логин уже существует");
                return;
            }

            loadUsers();
        });
    }

    private void editUser() {
        User selected = table.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Выберите пользователя");
            return;
        }

        TextInputDialog dialog =
                new TextInputDialog(selected.getLogin());

        dialog.setTitle("Изменение пользователя");
        dialog.setHeaderText("Изменение логина");

        dialog.showAndWait().ifPresent(login -> {
            login = login.trim();

            if (login.isEmpty()) {
                showMessage("Логин не может быть пустым");
                return;
            }

            ru.coursework.service.AuthService authService =
                    new ru.coursework.service.AuthService();

            if (!authService.changeLogin(selected, login)) {
                showMessage("Такой логин уже существует");
                return;
            }

            loadUsers();
        });
    }

    private void deleteUser() {
        User selected = table.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Выберите пользователя");
            return;
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alert.setTitle("Удаление");
        alert.setHeaderText(null);

        alert.setContentText(
                "Удалить пользователя "
                        + selected.getLogin()
                        + "?"
        );

        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                controller.deleteUser(selected);
                loadUsers();
            }
        });
    }

    private void openRoles() {
        User selected = table.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showMessage("Выберите пользователя");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Управление ролями");

        dialog.setHeaderText(
                "Роли пользователя: "
                        + selected.getLogin()
        );

        RoleManagementView roleView =
                new RoleManagementView(selected);

        dialog.getDialogPane().setContent(
                roleView.getView()
        );

        dialog.getDialogPane().getButtonTypes().add(
                ButtonType.CLOSE
        );

        dialog.showAndWait();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return view;
    }
}