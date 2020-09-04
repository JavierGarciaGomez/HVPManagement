package com.JGG.HVPManagement.controller.collaborator;

import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

// todo delete
public class showCollaboratorController implements Initializable {
    public HBox rootPane;
    public Label txtFirstName;
    public Label txtEmail;
    public Label txtPosition;
    public ComboBox cboUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addCollaborator(ActionEvent actionEvent) {
        Utilities.getInstance().loadWindow("view/collaborator/addCollaborator.fxml", new Stage(),
                "Add a new collaborator", StageStyle.DECORATED, false, false);
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
    }
}
