package ru.coursework.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.coursework.model.User;
import ru.coursework.service.AccessService;

public class MainView {

    private final BorderPane view;

    public MainView(User user) {

        view = new BorderPane();

        AccessService accessService =
                new AccessService();

        Label userLabel =
                new Label("Пользователь: " + user.getLogin());

        Button profileButton =
                new Button("Мой профиль");

        Button clientsButton =
                new Button("Клиенты");

        Button machineTypesButton =
                new Button("Типы станков");

        Button machinesButton =
                new Button("Станки");

        Button repairTypesButton =
                new Button("Виды ремонта");

        Button repairsButton =
                new Button("Ремонты");

        Button usersButton =
                new Button("Пользователи");

        VBox menu =
                new VBox(10);

        menu.setPadding(
                new Insets(15)
        );

        menu.getChildren().add(
                profileButton
        );

        if (accessService.isAdmin(user)) {

            menu.getChildren().addAll(
                    clientsButton,
                    machineTypesButton,
                    machinesButton,
                    repairTypesButton,
                    repairsButton,
                    usersButton,
                    new Label("Режим администратора")
            );

        } else if (accessService.isClient(user)) {

            menu.getChildren().addAll(
                    machinesButton,
                    repairsButton,
                    new Label("Режим клиента")
            );

        } else {

            menu.getChildren().addAll(
                    repairsButton,
                    new Label("Режим просмотра")
            );
        }

        view.setTop(
                new HBox(
                        15,
                        userLabel
                )
        );

        view.setLeft(menu);

        profileButton.setOnAction(e ->
                view.setCenter(
                        new ProfileView(user).getView()
                )
        );

        clientsButton.setOnAction(e ->
                view.setCenter(
                        new ClientView().getView()
                )
        );

        machineTypesButton.setOnAction(e ->
                view.setCenter(
                        new MachineTypeView().getView()
                )
        );

        machinesButton.setOnAction(e -> {

            boolean canEdit =
                    accessService.isAdmin(user);

            view.setCenter(
                    new MachineView(user, canEdit).getView()
            );
        });

        repairTypesButton.setOnAction(e ->
                view.setCenter(
                        new RepairTypeView().getView()
                )
        );

        repairsButton.setOnAction(e -> {
            boolean canEdit = accessService.isAdmin(user);
            view.setCenter(
                    new RepairView(user, canEdit).getView()
            );
        });

        usersButton.setOnAction(e ->
                view.setCenter(
                        new UserView().getView()
                )
        );
    }

    public BorderPane getView() {
        return view;
    }
}