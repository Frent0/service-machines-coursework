package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import org.hibernate.Session;
import ru.coursework.controller.MachineController;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Client;
import ru.coursework.model.Machine;
import ru.coursework.model.MachineType;

import java.util.List;

public class MachineView {

    private final MachineController controller;
    private final TableView<Machine> table;
    private final boolean editable;

    public MachineView() {
        this(true);
    }

    public MachineView(boolean editable) {
        this.editable = editable;

        controller = new MachineController();
        table = new TableView<>();

        TableColumn<Machine, String> inventoryColumn =
                new TableColumn<>("Инвентарный номер");

        inventoryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getInventoryNumber()
                )
        );

        TableColumn<Machine, String> clientColumn =
                new TableColumn<>("Клиент");

        clientColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getClient().getName()
                )
        );

        TableColumn<Machine, String> typeColumn =
                new TableColumn<>("Тип станка");

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getType().getBrand()
                )
        );

        table.getColumns().addAll(
                inventoryColumn,
                clientColumn,
                typeColumn
        );

        loadMachines();
    }

    private void loadMachines() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getMachines()
                )
        );
    }

    private List<Client> getClients() {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("from Client", Client.class)
                    .getResultList();
        }
    }

    private List<MachineType> getMachineTypes() {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery("from MachineType", MachineType.class)
                    .getResultList();
        }
    }

    private void showMachineDialog(Machine machine) {

        boolean editMode = machine != null;

        Dialog<Machine> dialog = new Dialog<>();

        dialog.setTitle(
                editMode
                        ? "Изменение станка"
                        : "Добавление станка"
        );

        ButtonType saveButtonType =
                new ButtonType(
                        "Сохранить",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButtonType,
                ButtonType.CANCEL
        );

        TextField inventoryField = new TextField();

        ComboBox<Client> clientBox = new ComboBox<>();

        clientBox.setItems(
                FXCollections.observableArrayList(getClients())
        );

        clientBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(Client client) {
                return client == null ? "" : client.getName();
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });

        ComboBox<MachineType> typeBox = new ComboBox<>();

        typeBox.setItems(
                FXCollections.observableArrayList(
                        getMachineTypes()
                )
        );

        typeBox.setConverter(new StringConverter<>() {

            @Override
            public String toString(MachineType type) {
                return type == null ? "" : type.getBrand();
            }

            @Override
            public MachineType fromString(String string) {
                return null;
            }
        });

        if (editMode) {

            inventoryField.setText(
                    machine.getInventoryNumber()
            );

            clientBox.setValue(
                    machine.getClient()
            );

            typeBox.setValue(
                    machine.getType()
            );
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(
                new Label("Инвентарный номер:"),
                0,
                0
        );

        grid.add(
                inventoryField,
                1,
                0
        );

        grid.add(
                new Label("Клиент:"),
                0,
                1
        );

        grid.add(
                clientBox,
                1,
                1
        );

        grid.add(
                new Label("Тип станка:"),
                0,
                2
        );

        grid.add(
                typeBox,
                1,
                2
        );

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == saveButtonType) {

                if (inventoryField.getText().isBlank()
                        || clientBox.getValue() == null
                        || typeBox.getValue() == null) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Заполните все поля."
                    );

                    alert.showAndWait();

                    return null;
                }

                if (editMode) {

                    machine.setInventoryNumber(
                            inventoryField.getText()
                    );

                    machine.setClient(
                            clientBox.getValue()
                    );

                    machine.setType(
                            typeBox.getValue()
                    );

                    return machine;
                }

                Machine newMachine = new Machine();

                newMachine.setInventoryNumber(
                        inventoryField.getText()
                );

                newMachine.setClient(
                        clientBox.getValue()
                );

                newMachine.setType(
                        typeBox.getValue()
                );

                return newMachine;
            }

            return null;
        });

        dialog.showAndWait().ifPresent(result -> {

            try {

                if (editMode) {
                    controller.updateMachine(result);
                } else {
                    controller.addMachine(result);
                }

                loadMachines();

            } catch (Exception e) {

                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Не удалось сохранить изменения."
                );

                alert.showAndWait();
            }
        });
    }

    private void showAddDialog() {
        showMachineDialog(null);
    }

    private void showEditDialog() {

        Machine machine =
                table.getSelectionModel().getSelectedItem();

        if (machine == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите станок для изменения."
            );

            alert.showAndWait();

            return;
        }

        showMachineDialog(machine);
    }

    private void deleteSelectedMachine() {

        Machine machine =
                table.getSelectionModel().getSelectedItem();

        if (machine == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите станок для удаления."
            );

            alert.showAndWait();

            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить станок "
                        + machine.getInventoryNumber()
                        + "?"
        );

        confirm.showAndWait().ifPresent(button -> {

            if (button == ButtonType.OK) {

                try {

                    controller.deleteMachine(machine);
                    loadMachines();

                } catch (Exception e) {

                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "Не удалось удалить станок."
                    );

                    alert.showAndWait();
                }
            }
        });
    }

    public VBox getView() {

        VBox root = new VBox(10);

        root.setPadding(new Insets(10));

        if (editable) {

            Button addButton =
                    new Button("Добавить");

            addButton.setOnAction(event ->
                    showAddDialog());

            Button editButton =
                    new Button("Изменить");

            editButton.setOnAction(event ->
                    showEditDialog());

            Button deleteButton =
                    new Button("Удалить");

            deleteButton.setOnAction(event ->
                    deleteSelectedMachine());

            HBox buttons = new HBox(10);

            buttons.getChildren().addAll(
                    addButton,
                    editButton,
                    deleteButton
            );

            root.getChildren().addAll(
                    table,
                    buttons
            );

        } else {

            root.getChildren().add(table);
        }

        return root;
    }
}