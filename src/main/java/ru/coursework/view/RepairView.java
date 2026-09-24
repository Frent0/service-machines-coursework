package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.hibernate.Session;
import ru.coursework.controller.RepairController;
import ru.coursework.database.HibernateUtil;
import ru.coursework.model.Machine;
import ru.coursework.model.Repair;
import ru.coursework.model.RepairType;
import ru.coursework.model.User;

import java.time.LocalDate;
import java.util.List;

public class RepairView {

    private final RepairController controller;
    private final TableView<Repair> table;
    private final boolean editable;
    private final User user;

    public RepairView() {
        this(null, true);
    }

    public RepairView(boolean editable) {
        this(null, editable);
    }

    public RepairView(User user, boolean editable) {
        this.user = user;
        this.editable = editable;

        controller = new RepairController();
        table = new TableView<>();

        TableColumn<Repair, String> machineColumn =
                new TableColumn<>("Станок");

        machineColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue()
                                .getMachine()
                                .getInventoryNumber()
                )
        );

        TableColumn<Repair, String> typeColumn =
                new TableColumn<>("Вид ремонта");

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue()
                                .getRepairType()
                                .getName()
                )
        );

        TableColumn<Repair, String> startColumn =
                new TableColumn<>("Дата начала");

        startColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(
                                data.getValue().getStartDate()
                        )
                )
        );

        TableColumn<Repair, String> endColumn =
                new TableColumn<>("Дата окончания");

        endColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEndDate() == null
                                ? ""
                                : String.valueOf(
                                data.getValue().getEndDate()
                        )
                )
        );

        TableColumn<Repair, String> statusColumn =
                new TableColumn<>("Статус");

        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus()
                )
        );

        table.getColumns().addAll(
                machineColumn,
                typeColumn,
                startColumn,
                endColumn,
                statusColumn
        );

        loadRepairs();
    }

    private void loadRepairs() {

        List<Repair> repairs;

        if (user != null
                && user.getClient() != null
                && !editable) {

            repairs = controller.getRepairsByClient(
                    user.getClient().getId()
            );

        } else {

            repairs = controller.getRepairs();
        }

        table.setItems(
                FXCollections.observableArrayList(
                        repairs
                )
        );
    }

    private List<Machine> getMachines() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery(
                            "from Machine",
                            Machine.class
                    )
                    .getResultList();
        }
    }

    private List<RepairType> getRepairTypes() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session
                    .createQuery(
                            "from RepairType",
                            RepairType.class
                    )
                    .getResultList();
        }
    }

    private void showRepairDialog(Repair repair) {

        boolean editMode = repair != null;

        Dialog<Repair> dialog = new Dialog<>();

        dialog.setTitle(
                editMode
                        ? "Изменение ремонта"
                        : "Добавление ремонта"
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

        ComboBox<Machine> machineBox =
                new ComboBox<>();

        machineBox.setItems(
                FXCollections.observableArrayList(
                        getMachines()
                )
        );

        machineBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(Machine machine) {
                        return machine == null
                                ? ""
                                : machine.getInventoryNumber();
                    }

                    @Override
                    public Machine fromString(String string) {
                        return null;
                    }
                }
        );

        ComboBox<RepairType> repairTypeBox =
                new ComboBox<>();

        repairTypeBox.setItems(
                FXCollections.observableArrayList(
                        getRepairTypes()
                )
        );

        repairTypeBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            RepairType repairType) {

                        return repairType == null
                                ? ""
                                : repairType.getName();
                    }

                    @Override
                    public RepairType fromString(
                            String string) {
                        return null;
                    }
                }
        );

        DatePicker startDatePicker =
                new DatePicker();

        DatePicker endDatePicker =
                new DatePicker();

        ComboBox<String> statusBox =
                new ComboBox<>(
                        FXCollections.observableArrayList(
                                "Запланирован",
                                "В процессе",
                                "Завершён",
                                "Отменён"
                        )
                );

        if (editMode) {

            machineBox.setValue(
                    repair.getMachine()
            );

            repairTypeBox.setValue(
                    repair.getRepairType()
            );

            startDatePicker.setValue(
                    repair.getStartDate()
            );

            endDatePicker.setValue(
                    repair.getEndDate()
            );

            statusBox.setValue(
                    repair.getStatus()
            );
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(
                new Label("Станок:"),
                0,
                0
        );

        grid.add(
                machineBox,
                1,
                0
        );

        grid.add(
                new Label("Вид ремонта:"),
                0,
                1
        );

        grid.add(
                repairTypeBox,
                1,
                1
        );

        grid.add(
                new Label("Дата начала:"),
                0,
                2
        );

        grid.add(
                startDatePicker,
                1,
                2
        );

        grid.add(
                new Label("Дата окончания:"),
                0,
                3
        );

        grid.add(
                endDatePicker,
                1,
                3
        );

        grid.add(
                new Label("Статус:"),
                0,
                4
        );

        grid.add(
                statusBox,
                1,
                4
        );

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == saveButtonType) {

                if (machineBox.getValue() == null
                        || repairTypeBox.getValue() == null
                        || startDatePicker.getValue() == null
                        || statusBox.getValue() == null) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Заполните обязательные поля."
                    );

                    alert.showAndWait();

                    return null;
                }

                LocalDate startDate =
                        startDatePicker.getValue();

                LocalDate endDate =
                        endDatePicker.getValue();

                if (endDate != null
                        && endDate.isBefore(startDate)) {

                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "Дата окончания не может быть раньше даты начала."
                    );

                    alert.showAndWait();

                    return null;
                }

                if (editMode) {

                    repair.setMachine(
                            machineBox.getValue()
                    );

                    repair.setRepairType(
                            repairTypeBox.getValue()
                    );

                    repair.setStartDate(
                            startDate
                    );

                    repair.setEndDate(
                            endDate
                    );

                    repair.setStatus(
                            statusBox.getValue()
                    );

                    return repair;
                }

                Repair newRepair =
                        new Repair();

                newRepair.setMachine(
                        machineBox.getValue()
                );

                newRepair.setRepairType(
                        repairTypeBox.getValue()
                );

                newRepair.setStartDate(
                        startDate
                );

                newRepair.setEndDate(
                        endDate
                );

                newRepair.setStatus(
                        statusBox.getValue()
                );

                return newRepair;
            }

            return null;
        });

        dialog.showAndWait().ifPresent(result -> {

            try {

                if (editMode) {
                    controller.updateRepair(result);
                } else {
                    controller.addRepair(result);
                }

                loadRepairs();

            } catch (Exception e) {

                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Не удалось сохранить ремонт."
                );

                alert.showAndWait();
            }
        });
    }

    private void showAddDialog() {
        showRepairDialog(null);
    }

    private void showEditDialog() {

        Repair repair =
                table.getSelectionModel()
                        .getSelectedItem();

        if (repair == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите ремонт для изменения."
            );

            alert.showAndWait();

            return;
        }

        showRepairDialog(repair);
    }

    private void deleteSelectedRepair() {

        Repair repair =
                table.getSelectionModel()
                        .getSelectedItem();

        if (repair == null) {

            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Выберите ремонт для удаления."
            );

            alert.showAndWait();

            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить выбранный ремонт?"
        );

        confirm.showAndWait().ifPresent(button -> {

            if (button == ButtonType.OK) {

                try {

                    controller.deleteRepair(repair);
                    loadRepairs();

                } catch (Exception e) {

                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "Не удалось удалить ремонт."
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
                    deleteSelectedRepair());

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