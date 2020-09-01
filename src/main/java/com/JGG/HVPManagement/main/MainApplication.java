package com.JGG.HVPManagement.main;

import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Utilities;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    private static final int COUNT_LIMIT = 10;

    @Override
    public void start(Stage stage) throws Exception {
        Utilities.getInstance().loadWindow("view/main/Main.fxml", new Stage(), "Main Window", StageStyle.DECORATED,
         false, false);
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
