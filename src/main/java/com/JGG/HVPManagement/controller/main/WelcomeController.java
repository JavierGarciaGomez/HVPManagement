package com.JGG.HVPManagement.controller.main;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    public BorderPane rootPane;
    public Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void closeStage() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
    }
}


