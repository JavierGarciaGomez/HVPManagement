package com.JGG.HVPManagement.main;

import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainTesting extends Application {
    public void start(Stage stage) throws Exception {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/CalendarTes.fxml")));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/schedule/Calendar.fxml")));
        stage.setTitle("Calendar");
        stage.setScene(new Scene(root));
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
