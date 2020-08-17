package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.model.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CollaboratorController implements Initializable {
    public HBox rootPane;
    public Label txtFirstName;
    public Label txtEmail;
    public Label txtPosition;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtFirstName.setText(Model.getInstance().selectedColaborator.getFirstName());
        txtEmail.setText(Model.getInstance().selectedColaborator.getDetailedCollaboratorInfo().getEmail());
        txtPosition.setText(Model.getInstance().selectedColaborator.getJobPosition().getName());
    }
}
