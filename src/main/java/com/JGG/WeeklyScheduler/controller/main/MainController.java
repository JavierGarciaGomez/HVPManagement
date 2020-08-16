package com.JGG.WeeklyScheduler.controller;

import com.JGG.WeeklyScheduler.entity.HibernateConnection;
import com.JGG.WeeklyScheduler.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public AnchorPane rootPane;
    private HibernateConnection hibernateConnection;

    public void login() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login Window");
            stage.show();
            Stage thisStage = (Stage) rootPane.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            System.out.println("***********************NOT FOUNDED IO");
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hibernateConnection = HibernateConnection.getInstance();
        Model.getInstance().hibernateLoaded = true;
    }
}
