package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.coursework.controller.ClientController;
import ru.coursework.model.Client;

public class ClientView {

    private final ClientController controller;
    private final TableView<Client> table;

    public ClientView() {
        controller = new ClientController();
        table = new TableView<>();

        TableColumn<Client, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        TableColumn<Client, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAddress()
                )
        );

        TableColumn<Client, String> phoneColumn = new TableColumn<>("Телефон");
        phoneColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPhone()
                )
        );

        TableColumn<Client, String> contactColumn = new TableColumn<>("Контактное лицо");
        contactColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getContactPerson()
                )
        );

        table.getColumns().addAll(
                nameColumn,
                addressColumn,
                phoneColumn,
                contactColumn
        );

        loadClients();
    }

    private void loadClients() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getClients()
                )
        );
    }

    private void showAddDialog() {
        showClientDialog(null);
    }

    private void showEditDialog() {
        Client client = table.getSelectionModel().getSelectedItem();

        if (client == null) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите клиента для изменения."
            );
            alert.showAndWait();
            return;
        }

        showClientDialog(client);
    }

    private void showClientDialog(Client client) {
        boolean editMode = client != null;

        Dialog<Client> dialog = new Dialog<>();

        if (editMode) {
            dialog.setTitle("Изменение клиента");
        } else {
            dialog.setTitle("Добавление клиента");
        }

        ButtonType saveButtonType =
                new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButtonType,
                ButtonType.CANCEL
        );

        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneField = new TextField();
        TextField contactField = new TextField();

        if (editMode) {
            nameField.setText(client.getName());
            addressField.setText(client.getAddress());
            phoneField.setText(client.getPhone());
            contactField.setText(client.getContactPerson());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Адрес:"), 0, 1);
        grid.add(addressField, 1, 1);

        grid.add(new Label("Телефон:"), 0, 2);
        grid.add(phoneField, 1, 2);

        grid.add(new Label("Контактное лицо:"), 0, 3);
        grid.add(contactField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == saveButtonType) {

                if (nameField.getText().isBlank()
                        || addressField.getText().isBlank()
                        || phoneField.getText().isBlank()
                        || contactField.getText().isBlank()) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Заполните все поля."
                    );
                    alert.showAndWait();

                    return null;
                }

                if (editMode) {
                    client.setName(nameField.getText());
                    client.setAddress(addressField.getText());
                    client.setPhone(phoneField.getText());
                    client.setContactPerson(contactField.getText());

                    return client;
                }

                Client newClient = new Client();
                newClient.setName(nameField.getText());
                newClient.setAddress(addressField.getText());
                newClient.setPhone(phoneField.getText());
                newClient.setContactPerson(contactField.getText());

                return newClient;
            }

            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                if (editMode) {
                    controller.updateClient(result);
                } else {
                    controller.addClient(result);
                }

                loadClients();

            } catch (Exception e) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Не удалось сохранить изменения."
                );
                alert.showAndWait();
            }
        });
    }

    private void deleteSelectedClient() {
        Client client = table.getSelectionModel().getSelectedItem();

        if (client == null) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите клиента для удаления."
            );
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить клиента " + client.getName() + "?"
        );

        confirm.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    controller.deleteClient(client);
                    loadClients();
                } catch (Exception e) {
                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "Не удалось удалить клиента."
                    );
                    alert.showAndWait();
                }
            }
        });
    }

    public VBox getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button addButton = new Button("Добавить");
        addButton.setOnAction(event -> showAddDialog());

        Button editButton = new Button("Изменить");
        editButton.setOnAction(event -> showEditDialog());

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(event -> deleteSelectedClient());

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(
                addButton,
                editButton,
                deleteButton
        );

        root.getChildren().addAll(table, buttons);

        return root;
    }
}