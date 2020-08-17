package com.JGG.WeeklyScheduler;

import com.JGG.WeeklyScheduler.model.HibernateConnection;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static final int COUNT_LIMIT = 10;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/main/Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void init() throws Exception {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    }


    public static void main(String[] args) {
        //launch(args);
        LauncherImpl.launchApplication(MainApplication.class, MainPreloader.class, args);
    }

}
