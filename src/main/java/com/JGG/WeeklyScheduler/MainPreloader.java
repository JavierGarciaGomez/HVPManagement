package com.JGG.WeeklyScheduler;

import com.JGG.WeeklyScheduler.entity.Utilities;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        this.preloadedStage = stage;
        Utilities.getInstance().loadWindow("view/main/Welcome.fxml", preloadedStage,
                "Welcome", StageStyle.UNDECORATED, false);
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
