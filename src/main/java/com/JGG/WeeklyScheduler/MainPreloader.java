package com.JGG.WeeklyScheduler;

import com.JGG.WeeklyScheduler.controller.WelcomeController;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Preloader {
    private Stage preloadedStage;
    private Scene scene;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/Welcome.fxml")));
        scene = new Scene(root);
    }

    @Override
    public void start(Stage welcomeStage) throws Exception {
        this.preloadedStage=welcomeStage;
        preloadedStage.setScene(scene);
        preloadedStage.setTitle("Welcome");
        preloadedStage.getIcons().add(new Image("/icon/HVPicon.jpg"));
        preloadedStage.show();
    }



    public Main() {
        super();
    }

    @Override
    public void handleProgressNotification(ProgressNotification info) {
        if(info instanceof ProgressNotification){
            WelcomeController.loadingLabel.setText("Loading "+((ProgressNotification) info).getProgress()+"%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {

    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        super.handleApplicationNotification(info);
    }

    @Override
    public boolean handleErrorNotification(ErrorNotification info) {
        return super.handleErrorNotification(info);
    }

}
