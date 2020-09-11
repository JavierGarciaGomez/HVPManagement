package com.JGG.HVPManagement.main;

import com.JGG.HVPManagement.dao.LogDAO;
import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/main/Main.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Main Window");
            stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
            stage.setResizable(false);
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init() throws Exception {
        HibernateConnection.getInstance();
/*        Thread thread = Runnables.getInstance().runCollaborators();
        thread.join();
        Runnables.getInstance().loadMainDatabases();*/
    }


    public static void main(String[] args) {
        //launch(args);
        System.out.println("BEFORE MAIN main");
        LauncherImpl.launchApplication(MainApplication.class, MainPreloader.class, args);
        System.out.println("AFTER Main main");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LogDAO.getInstance().exitSession(Model.getInstance().loggedUser);
        System.out.println("Application is closing");
    }
}
