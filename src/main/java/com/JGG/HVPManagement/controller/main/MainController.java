package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    public BorderPane rootPane;
    public ImageView imageView;
    private HibernateConnection hibernateConnection;
    private Model model;
    private Utilities utilities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        // todo delete
        model.loggedUser= UserDAO.getInstance().getUserbyUserName("AGA");

        utilities = Utilities.getInstance();
        hibernateConnection = HibernateConnection.getInstance();
        setImage();
        if (model.loggedUser != null)
            txtUserName.setText(model.loggedUser.getUserName() + "\n"
                    + model.loggedUser.getCollaborator().getFirstName() + "\n" + model.loggedUser.getCollaborator().getLastName());
    }

    private void setImage() {
        try {
            File file = new File("res\\unknown.png");
            if (model.loggedUser != null) {
                File tempFile = new File("res\\" + model.loggedUser.getUserName() + ".png");
                System.out.println(tempFile.getAbsolutePath());
                if (tempFile.exists()) file = tempFile;
            }
            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private void setImage() {
        try {
            //File file = new File("res\\unknown.png");
            File file = new File(getClass().getClassLoader().getResource("images/unknown.png").getFile());
            if (model.loggedUser != null) {
                File tempFile = new File(getClass().getClassLoader().getResource("images/"+model.loggedUser.getUserName()+".png").getFile());
                System.out.println(tempFile.getAbsolutePath());
                if (tempFile.exists()) file = tempFile;
            }
            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void showLogin() {
        Utilities.getInstance().loadWindow("view/main/Login.fxml", new Stage(), "Login Window", StageStyle.DECORATED,
                false, false);
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.hide();
    }


    public void showAttendanceControl() {
        Utilities.getInstance().loadWindow("view/attendanceControl/AttendanceControl.fxml", new Stage(), "Attendance Control",
                StageStyle.DECORATED, true, true);
    }

    public void showManageUser() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.loadWindow("view/collaborator/addCollaborator.fxml", new Stage(), "Manage users",
                    StageStyle.DECORATED, true, true);

        }

    }

    public void showWorkSchedule(ActionEvent actionEvent) {
        utilities.loadWindow("view/workSchedule/WorkSchedule.fxml", new Stage(), "Work Schedule",
                StageStyle.DECORATED, true, true);
    }

    public void showSchedule(ActionEvent actionEvent) {
        utilities.loadWindow("view/schedule/Calendar.fxml", new Stage(), "Schedule",
                StageStyle.DECORATED, true, false);
    }

    public void showConfiguration(ActionEvent actionEvent) {
        utilities.loadWindow("view/configuration/Configuration.fxml", new Stage(), "Configuration",
                StageStyle.DECORATED, true, true);
    }
}
