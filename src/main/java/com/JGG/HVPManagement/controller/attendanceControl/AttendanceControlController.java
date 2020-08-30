package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AttendanceControlController implements Initializable {
    public Button btnRegister;
    public Button btnIncidences;
    public Button btnModify;
    public Button btnReview;
    public Button btnReports;
    public Button btnChangeUser;
    public Button btnManageUsers;
    public Label lblName;
    public ImageView imgPicture;
    private User user;

    public void registerTime(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl/Register.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Manage users");

            RegisterController controller = fxmlLoader.getController();
            controller.initData(this.user);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reviewRegisters(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl;ReviewRegisters.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Review registers");

            ReviewRegistersController controller = fxmlLoader.getController();
            controller.initData(this.user);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateReports(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl;ReportsGenerator.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Review registers");

            ReportsGeneratorController controller = fxmlLoader.getController();
            controller.initData(this.user);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeRegisters(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl;ChangeRegisters.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Review registers");

            ChangeRegistersController controller = fxmlLoader.getController();
            controller.initData(this.user);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openIncidences(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl;Incidences.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Incidences Window");

            IncidencesController controller = fxmlLoader.getController();
            controller.initData(this.user);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageUsers(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/attendanceControl;ManageUser.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Manage users");
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(User user) {
        this.user = user;
        //lblName.setText(user.getName() + " " + user.getLastName());
        setPicture();

    }

    private void setPicture() {
        String userName = user.getUserName();
        try {
            File file = new File("res\\" + userName + ".png");
            if (!file.exists()) {
                file = new File("res\\" + "Morgana" + ".png");
            }
            Image image = new Image(new FileInputStream(file));
            imgPicture.setImage(image);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void changeUser(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/main/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login Window");
            stage.show();
            Stage thisStage = (Stage) btnChangeUser.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            System.out.println("***********************NOT FOUNDED IO");
            e.printStackTrace();
        }

    }
}

