package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.coursework.controller.RepairTypeController;
import ru.coursework.model.RepairType;

import java.math.BigDecimal;

public class RepairTypeView {

    private final RepairTypeController controller;
    private final TableView<RepairType> table;

    public RepairTypeView() {
        controller = new RepairTypeController();
        table = new TableView<>();

        TableColumn<RepairType, String> nameColumn =
                new TableColumn<>("Название ремонта");

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        TableColumn<RepairType, String> durationColumn =
                new TableColumn<>("Длительность");

        durationColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                data.getValue().getDuration()
                        )
                )
        );

        TableColumn<RepairType, String> costColumn =
                new TableColumn<>("Стоимость");

        costColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                data.getValue().getCost()
                        )
                )
        );

        table.getColumns().addAll(
                nameColumn,
                durationColumn,
                costColumn
        );

        loadRepairTypes();
    }

    private void loadRepairTypes() {
        table.setItems(
                FXCollections.observableArrayList(
                        controller.getRepairTypes()
                )
        );
    }

    private void showRepairTypeDialog(RepairType repairType) {

        boolean editMode = repairType != null;

        Dialog<RepairType> dialog = new Dialog<>();

        dialog.setTitle(
                editMode
                        ? "Изменение вида ремонта"
                        : "Добавление вида ремонта"
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

        TextField nameField = new TextField();
        TextField durationField = new TextField();
        TextField costField = new TextField();

        if (editMode) {

            nameField.setText(
                    repairType.getName()
            );

            durationField.setText(
                    String.valueOf(
                            repairType.getDuration()
                    )
            );

            costField.setText(
                    String.valueOf(
                            repairType.getCost()
                    )
            );
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(
                new Label("Название ремонта:"),
                0,
                0
        );

        grid.add(
                nameField,
                1,
                0
        );

        grid.add(
                new Label("Длительность, дней:"),
                0,
                1
        );

        grid.add(
                durationField,
                1,
                1
        );

        grid.add(
                new Label("Стоимость:"),
                0,
                2
        );

        grid.add(
                costField,
                1,
                2
        );

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == saveButtonType) {

                if (nameField.getText().isBlank()
                        || durationField.getText().isBlank()
                        || costField.getText().isBlank()) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Заполните все поля."
                    );

                    alert.showAndWait();

                    return null;
                }

                try {

                    int duration =
                            Integer.parseInt(
                                    durationField.getText()
                            );

                    BigDecimal cost =
                            new BigDecimal(
                                    costField.getText().replace(",", ".")
                            );

                    if (duration <= 0) {

                        Alert alert = new Alert(
                                Alert.AlertType.WARNING,
                                "Длительность должна быть больше 0."
                        );

                        alert.showAndWait();

                        return null;
                    }

                    if (cost.compareTo(BigDecimal.ZERO) < 0) {

                        Alert alert = new Alert(
                                Alert.AlertType.WARNING,
                                "Стоимость не может быть отрицательной."
                        );

                        alert.showAndWait();

                        return null;
                    }

                    if (editMode) {

                        repairType.setName(
                                nameField.getText()
                        );

                        repairType.setDuration(
                                duration
                        );

                        repairType.setCost(
                                cost
                        );

                        return repairType;
                    }

                    RepairType newRepairType =
                            new RepairType();

                    newRepairType.setName(
                            nameField.getText()
                    );

                    newRepairType.setDuration(
                            duration
                    );

                    newRepairType.setCost(
                            cost
                    );

                    return newRepairType;

                } catch (NumberFormatException e) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Длительность должна быть целым числом, а стоимость — числом."
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
                    controller.updateRepairType(result);
                } else {
                    controller.addRepairType(result);
                }

                loadRepairTypes();

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
        showRepairTypeDialog(null);
    }

    private void showEditDialog() {

        RepairType repairType =
                table.getSelectionModel().getSelectedItem();

        if (repairType == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите вид ремонта для изменения."
            );

            alert.showAndWait();

            return;
        }

        showRepairTypeDialog(repairType);
    }

    private void deleteSelectedRepairType() {

        RepairType repairType =
                table.getSelectionModel().getSelectedItem();

        if (repairType == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите вид ремонта для удаления."
            );

            alert.showAndWait();

            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить вид ремонта "
                        + repairType.getName()
                        + "?"
        );

        confirm.showAndWait().ifPresent(button -> {

            if (button == ButtonType.OK) {

                try {

                    controller.deleteRepairType(repairType);
                    loadRepairTypes();

                } catch (Exception e) {

                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "Не удалось удалить вид ремонта."
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
                deleteSelectedRepairType());

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