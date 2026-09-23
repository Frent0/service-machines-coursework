package ru.coursework.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.coursework.model.Role;
import ru.coursework.model.User;
import ru.coursework.model.UserRole;
import ru.coursework.repository.RoleRepository;
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

    private final ListView<Role> roleList;

    public RoleManagementView(User user) {
        this.user = user;

        roleRepository = new RoleRepository();
        userRoleRepository = new UserRoleRepository();

        roles = roleRepository.findAll();

        roleList = new ListView<>();
        roleList.setItems(FXCollections.observableArrayList(roles));
        roleList.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        loadUserRoles();

        Button saveButton = new Button("Сохранить");

        saveButton.setOnAction(e -> saveRoles());

        view = new VBox(10);
        view.setPadding(new Insets(20));

        view.getChildren().addAll(
                new Label("Роли пользователя: " + user.getLogin()),
                roleList,
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

    private void saveRoles() {
        List<Role> selectedRoles =
                roleList.getSelectionModel().getSelectedItems();

        List<Role> currentRoles =
                userRoleRepository.findRolesByUserId(user.getId());

        for (Role role : currentRoles) {
            userRoleRepository.delete(
                    new UserRole(user.getId(), role.getId())
            );
        }

        for (Role role : selectedRoles) {
            userRoleRepository.save(
                    new UserRole(user.getId(), role.getId())
            );
        }

        showMessage("Роли сохранены");
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}