package com.JGG.HVPManagement.main;

import com.JGG.HVPManagement.model.Utilities;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;

public class MainPreloader extends Preloader {
    private Stage preloadedStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("STARTING TO SHOW THE PRELOADER "+ LocalTime.now());
        this.preloadedStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/main/Welcome.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Welcome");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
        stage.setResizable(false);
        stage.show();
        System.out.println("FINISHED TO SHOW THE PRELOADER "+ LocalTime.now());
    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
                System.out.println("BEFORE_START");
                preloadedStage.hide();
                break;
        }


    }

}
