package com.JGG.WeeklyScheduler.controller.main;

import com.JGG.WeeklyScheduler.entity.HibernateConnection;
import com.JGG.WeeklyScheduler.entity.User;
import com.JGG.WeeklyScheduler.model.Utilities;
import com.JGG.WeeklyScheduler.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Label txtUserName;
    private User loggedUser;
    public BorderPane rootPane;
    public ImageView imageView;
    private HibernateConnection hibernateConnection;

    public void login() {
        System.out.println("Pushed");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/main/Login.fxml"));
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
        this.loggedUser = Model.getInstance().loggedUser;
        setImage();
        if(loggedUser!=null) txtUserName.setText(loggedUser.getName()+"\n"+loggedUser.getLastName());


    }

    private void setImage() {
        try {
            File file = new File("res\\unknown.png");
            if (loggedUser != null) {
                File tempFile = new File("res\\" + loggedUser.getUser() + ".png");
                if (tempFile.exists()) file = tempFile;
            }
            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void openAttendanceControl(ActionEvent actionEvent) {
        Utilities.getInstance().loadWindow("view/AttendanceControl.fxml", new Stage(), "Attendance Control",
                StageStyle.DECORATED, true, true);
    }
}
