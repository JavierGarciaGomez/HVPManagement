package com.JGG.HVPManagement.main;

import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalTime;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        Utilities.getInstance().loadWindow("view/main/Main.fxml", new Stage(), "Main Window", StageStyle.DECORATED,
         false, false);
    }


    @Override
    public void init() throws Exception {
        System.out.println("STARTING TO CONNECT TO HIBERNATE"+LocalTime.now());
        HibernateConnection.getInstance();
        System.out.println("FINISHED TO CONNECT TO HIBERNATE"+LocalTime.now());
        Thread thread = Runnables.getInstance().runCollaborators();
        thread.join();
        Runnables.getInstance().loadMainDatabases();
        System.out.println("FINISHED TO LOAD DATABASES"+LocalTime.now());

    }


    public static void main(String[] args) {
        //launch(args);
        System.out.println("BEFORE MAIN main");
        LauncherImpl.launchApplication(MainApplication.class, MainPreloader.class, args);
        System.out.println("AFTER Main main");
    }

}
