package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.coursework.model.Client;
import ru.coursework.model.Role;
import ru.coursework.model.User;
import ru.coursework.model.UserRole;
import ru.coursework.repository.ClientRepository;
import ru.coursework.repository.RoleRepository;
import ru.coursework.repository.UserRepository;
import ru.coursework.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleManagementView {

    private final VBox view;

    private final User user;
    private final List<Role> roles;

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    private final ListView<Role> roleList;
    private final ComboBox<Client> clientComboBox;

    public RoleManagementView(User user) {
        this.user = user;

        roleRepository = new RoleRepository();
        userRoleRepository = new UserRoleRepository();
        userRepository = new UserRepository();
        clientRepository = new ClientRepository();

        roles = roleRepository.findAll();

        roleList = new ListView<>();
        roleList.setItems(FXCollections.observableArrayList(roles));
        roleList.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        loadUserRoles();

        // Получаем список предприятий-клиентов
        List<Client> clients = clientRepository.findAll();

        clientComboBox = new ComboBox<>();
        clientComboBox.setItems(
                FXCollections.observableArrayList(clients)
        );

        clientComboBox.setPromptText("Выберите предприятие");

        // Показываем название предприятия вместо Client@12345
        clientComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);

                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getName());
                }
            }
        });

        clientComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);

                if (empty || client == null) {
                    setText("Выберите предприятие");
                } else {
                    setText(client.getName());
                }
            }
        });

        // Если пользователь уже связан с предприятием,
        // показываем его в ComboBox
        if (user.getClient() != null) {
            for (Client client : clients) {
                if (client.getId().equals(user.getClient().getId())) {
                    clientComboBox.setValue(client);
                    break;
                }
            }
        }

        // Предприятие можно выбирать только при наличии роли CLIENT
        updateClientComboBoxState();

        // Следим за изменением выбранных ролей
        roleList.getSelectionModel()
                .getSelectedItems()
                .addListener((ListChangeListener<Role>) change ->
                        updateClientComboBoxState()
                );

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveRoles());

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(
                new Label("Роли пользователя: " + user.getLogin()),
                roleList,
                new Label("Предприятие клиента:"),
                clientComboBox,
                saveButton
        );
    }

    private void loadUserRoles() {
        List<Role> userRoles =
                userRoleRepository.findRolesByUserId(user.getId());

        Set<Integer> userRoleIds = new HashSet<>();

        for (Role role : userRoles) {
            userRoleIds.add(role.getId());
        }

        for (Role role : roles) {
            if (userRoleIds.contains(role.getId())) {
                roleList.getSelectionModel().select(role);
            }
        }
    }

    // Включает выбор предприятия только для CLIENT
    private void updateClientComboBoxState() {
        boolean clientRoleSelected = false;

        for (Role role :
                roleList.getSelectionModel().getSelectedItems()) {

            if ("CLIENT".equals(role.getName())) {
                clientRoleSelected = true;
                break;
            }
        }

        clientComboBox.setDisable(!clientRoleSelected);

        // Если CLIENT сняли, визуально убираем предприятие
        if (!clientRoleSelected) {
            clientComboBox.setValue(null);
        }
    }

    private void saveRoles() {
        List<Role> selectedRoles =
                roleList.getSelectionModel().getSelectedItems();

        // Проверяем, выбрана ли роль CLIENT
        boolean clientRoleSelected = false;

        for (Role role : selectedRoles) {
            if ("CLIENT".equals(role.getName())) {
                clientRoleSelected = true;
                break;
            }
        }

        // CLIENT обязательно должен быть связан с предприятием
        if (clientRoleSelected && clientComboBox.getValue() == null) {
            showError(
                    "Для роли CLIENT необходимо выбрать предприятие"
            );
            return;
        }

        List<Role> currentRoles =
                userRoleRepository.findRolesByUserId(user.getId());

        // Удаляем старые назначения ролей
        for (Role role : currentRoles) {
            userRoleRepository.delete(
                    new UserRole(user.getId(), role.getId())
            );
        }

        // Сохраняем выбранные роли
        for (Role role : selectedRoles) {
            userRoleRepository.save(
                    new UserRole(user.getId(), role.getId())
            );
        }

        // CLIENT связан с выбранным предприятием.
        // Если CLIENT нет, связь с предприятием удаляется.
        if (clientRoleSelected) {
            user.setClient(clientComboBox.getValue());
        } else {
            user.setClient(null);
        }

        userRepository.update(user);

        showMessage("Роли и предприятие сохранены");
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}