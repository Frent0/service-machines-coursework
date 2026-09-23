package ru.coursework;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.coursework.database.HibernateUtil;
import ru.coursework.view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate: подключение к БД успешно");
        } catch (Exception e) {
            System.out.println("Hibernate: ошибка подключения");
            e.printStackTrace();
        }

        LoginView loginView = new LoginView();

        Scene scene = new Scene(loginView.getView(), 400, 300);

        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}