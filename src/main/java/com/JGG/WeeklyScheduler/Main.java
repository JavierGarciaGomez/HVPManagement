package com.JGG.WeeklyScheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/Welcome.fxml")));
        stage.setTitle("Calendar");
        stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
        stage.setScene(new Scene(root));
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }
}
