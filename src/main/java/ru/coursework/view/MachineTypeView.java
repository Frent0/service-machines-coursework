package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.coursework.controller.MachineTypeController;
import ru.coursework.model.MachineType;

public class MachineTypeView {

    private final MachineTypeController controller;
    private final TableView<MachineType> table;

    public MachineTypeView() {
        controller = new MachineTypeController();
        table = new TableView<>();

        TableColumn<MachineType, String> countryColumn =
                new TableColumn<>("Страна-производитель");

        countryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCountry()
                )
        );

        TableColumn<MachineType, String> yearColumn =
                new TableColumn<>("Год выпуска");

        yearColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                data.getValue().getProductionYear()
                        )
                )
        );

        TableColumn<MachineType, String> brandColumn =
                new TableColumn<>("Бренд");

        brandColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getBrand()
                )
        );

        table.getColumns().addAll(
                countryColumn,
                yearColumn,
                brandColumn
        );

        loadMachineTypes();
    }

    private void loadMachineTypes() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getMachineTypes()
                )
        );
    }

    private void showMachineTypeDialog(MachineType machineType) {

        boolean editMode = machineType != null;

        Dialog<MachineType> dialog = new Dialog<>();

        dialog.setTitle(
                editMode
                        ? "Изменение типа станка"
                        : "Добавление типа станка"
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

        TextField countryField = new TextField();
        TextField yearField = new TextField();
        TextField brandField = new TextField();

        if (editMode) {
            countryField.setText(
                    machineType.getCountry()
            );

            yearField.setText(
                    String.valueOf(
                            machineType.getProductionYear()
                    )
            );

            brandField.setText(
                    machineType.getBrand()
            );
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(
                new Label("Страна-производитель:"),
                0,
                0
        );

        grid.add(
                countryField,
                1,
                0
        );

        grid.add(
                new Label("Год выпуска:"),
                0,
                1
        );

        grid.add(
                yearField,
                1,
                1
        );

        grid.add(
                new Label("Бренд:"),
                0,
                2
        );

        grid.add(
                brandField,
                1,
                2
        );

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == saveButtonType) {

                if (countryField.getText().isBlank()
                        || yearField.getText().isBlank()
                        || brandField.getText().isBlank()) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Заполните все поля."
                    );

                    alert.showAndWait();

                    return null;
                }

                try {

                    short year = Short.parseShort(
                            yearField.getText()
                    );

                    if (year < 1900 || year > 2100) {

                        Alert alert = new Alert(
                                Alert.AlertType.WARNING,
                                "Год должен быть от 1900 до 2100."
                        );

                        alert.showAndWait();

                        return null;
                    }

                    if (editMode) {

                        machineType.setCountry(
                                countryField.getText()
                        );

                        machineType.setProductionYear(year);

                        machineType.setBrand(
                                brandField.getText()
                        );

                        return machineType;
                    }

                    MachineType newMachineType =
                            new MachineType();

                    newMachineType.setCountry(
                            countryField.getText()
                    );

                    newMachineType.setProductionYear(year);

                    newMachineType.setBrand(
                            brandField.getText()
                    );

                    return newMachineType;

                } catch (NumberFormatException e) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Год выпуска должен быть числом."
                    );

                    alert.showAndWait();

                    return null;
                }
            }

            return null;
        });

        dialog.showAndWait().ifPresent(result -> {

            try {

                if (editMode) {
                    controller.updateMachineType(result);
                } else {
                    controller.addMachineType(result);
                }

                loadMachineTypes();

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
        showMachineTypeDialog(null);
    }

    private void showEditDialog() {

        MachineType machineType =
                table.getSelectionModel().getSelectedItem();

        if (machineType == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите тип станка для изменения."
            );

            alert.showAndWait();

            return;
        }

        showMachineTypeDialog(machineType);
    }

    private void deleteSelectedMachineType() {

        MachineType machineType =
                table.getSelectionModel().getSelectedItem();

        if (machineType == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите тип станка для удаления."
            );

            alert.showAndWait();

            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить тип станка "
                        + machineType.getBrand()
                        + "?"
        );

        confirm.showAndWait().ifPresent(button -> {

            if (button == ButtonType.OK) {

                try {

                    controller.deleteMachineType(machineType);
                    loadMachineTypes();

                } catch (Exception e) {

                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "Не удалось удалить тип станка."
                    );

                    alert.showAndWait();
                }
            }
        });
    }

    public VBox getView() {

        VBox root = new VBox(10);

        root.setPadding(new Insets(10));

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
                deleteSelectedMachineType());

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

        return root;
    }
}