package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Label txtUserName;
    public BorderPane rootPane;
    public ImageView imageView;
    private Model model;
    private Utilities utilities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        setImage();
        if (model.loggedUser != null) {
            txtUserName.setText(model.loggedUser.getUserName() + "\n"
                    + model.loggedUser.getCollaborator().getFirstName() + "\n" + model.loggedUser.getCollaborator().getLastName());
        } else{
            model.roleView= Model.role.GUEST_USER;
        }
    }

    private void setImage() {
        Image image = new Image("/images/unknown.png");
        if (model.loggedUser != null) {
            try{
                image = new Image("/images/" + model.loggedUser.getUserName() + ".png");
            } catch (IllegalArgumentException ignore){

            }
        }
        imageView.setImage(image);
    }

    public void showLogin() {
        model.openMainAfterLogin = true;
        utilities.loadWindowWithInitData("view/main/Login.fxml", new Stage(), "Login Window", StageStyle.DECORATED,
                false, false);
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.hide();
    }

    public void showAttendanceControl() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.loadWindow("view/attendanceControl/Register.fxml", new Stage(), "Attendance Control",
                    StageStyle.DECORATED, true, true);
            utilities.setNullTemporaryVariables();
        }

    }

    public void showManageUser() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.loadWindow("view/collaborator/addCollaborator.fxml", new Stage(), "Manage users",
                    StageStyle.DECORATED, true, true);
            utilities.setNullTemporaryVariables();
        }
    }

    public void showWorkSchedule() {
        utilities.loadWindowWithInitData("view/workSchedule/WorkSchedule.fxml", new Stage(), "Work Schedule",
                StageStyle.DECORATED, true, true);
        utilities.setNullTemporaryVariables();
    }

    public void showSchedule() {
        utilities.loadWindow("view/schedule/Calendar.fxml", new Stage(), "Schedule",
                StageStyle.DECORATED, true, false);
        utilities.setNullTemporaryVariables();
    }

    public void showConfiguration() {
        utilities.loadWindow("view/configuration/Configuration.fxml", new Stage(), "Configuration",
                StageStyle.DECORATED, true, true);
        utilities.setNullTemporaryVariables();
    }
}
