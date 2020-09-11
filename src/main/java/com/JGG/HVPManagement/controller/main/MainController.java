package com.JGG.HVPManagement.controller.main;

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

    public void showManageUser() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.setNullTemporaryVariables();
            utilities.loadModalWindow("view/collaborator/addCollaborator.fxml", "Manage users", true, true);
        }
    }

    public void showWorkSchedule() {
        utilities.setNullTemporaryVariables();
        utilities.loadModalWindowWithInitData("view/workSchedule/WorkSchedule.fxml", "Work Schedule", true, true);
    }

    public void showAttendanceControl() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.setNullTemporaryVariables();
            utilities.loadWindowWithInitData("view/attendanceControl/Register.fxml", new Stage(), "Attendance Control",
                    StageStyle.DECORATED, true, false);
            Stage thisStage = (Stage) rootPane.getScene().getWindow();
            thisStage.hide();
        }

    }

    public void showSchedule() {
        utilities.setNullTemporaryVariables();
        utilities.loadModalWindow("view/schedule/Calendar.fxml", "Schedule",
                true, false);
    }

    public void showIncidents() {
        if (model.loggedUser == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Login error", "To access this section you need to be logged in");
        } else {
            utilities.setNullTemporaryVariables();
            utilities.loadModalWindow("view/incident/manageIncidents.fxml", "Configuration", true, false);
        }
    }

    public void showConfiguration() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            utilities.showAlert(Alert.AlertType.ERROR, "ERROR", "You need to be an administrator or manager to enter this section");
        }
        utilities.setNullTemporaryVariables();
        utilities.loadModalWindow("view/configuration/Configuration.fxml", "Configuration", true, false);
    }
}
