package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePassController implements Initializable {
    public TextField txtUser;
    public PasswordField txtOldPass;
    public PasswordField txtConfirm;
    public PasswordField txtNewPass;
    public GridPane rootPane;
    private Utilities utilities;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        utilities=Utilities.getInstance();
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            save();
        }
    }

    public void save() {
        String userName = txtUser.getText();
        String oldPass = txtOldPass.getText();
        String newPass = txtNewPass.getText();
        String confirm = txtConfirm.getText();

        boolean checkLogin = false;

        User tempUser = CollaboratorDAO.getInstance().getCollaboratorbyUserName(userName).getUser();
        if (tempUser != null) {
            if (oldPass.equals(tempUser.getPass())) checkLogin = true;
        }

        if (!checkLogin) {
            Utilities.getInstance().showAlert(Alert.AlertType.ERROR, "Non-existent user", "The user and the old password doesn't match");
        } else {
            if (newPass.equals(confirm)) {
                tempUser.setPass(newPass);
                UserDAO.getInstance().updatePassword(tempUser);
                Utilities.getInstance().loadWindowWithInitData("view/main/Login.fxml", new Stage(), "Login Window", StageStyle.DECORATED, false, false);
                Stage thisStage = (Stage) rootPane.getScene().getWindow();
                thisStage.hide();
            } else {
                Utilities.getInstance().showAlert(Alert.AlertType.ERROR, "Password mismatch", "The new password and the cofirmation, doesn't match");
            }

        }


    }
}
