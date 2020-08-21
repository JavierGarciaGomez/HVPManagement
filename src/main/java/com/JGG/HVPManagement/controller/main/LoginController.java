package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField txtUser;
    public PasswordField txtPass;
    public Button btnLogin;
    public Button btnCancel;


    public void login() {
        String userName = this.txtUser.getText().toUpperCase();
        String pass = this.txtPass.getText();

        User user = new User(userName, pass);

        boolean checkLogin = false;

        // check showLogin

        User tempUser = UserDAO.getInstance().getUserbyUserName(user.getUserName());
        if (tempUser != null) {
            if (user.getPass().equals(tempUser.getPass())) checkLogin = true;
        }

        if (checkLogin) {
            Model.getInstance().loggedUser = UserDAO.getInstance().getUserbyUserName(userName);
            Utilities.getInstance().loadWindow("view/main/Main.fxml", new Stage(), "Main Window", StageStyle.DECORATED, false, false);
            Stage thisStage = (Stage) btnCancel.getScene().getWindow();
            thisStage.hide();
        } else {
            Utilities.getInstance().showAlert(Alert.AlertType.ERROR, "Non-existent user", "The user and the password doesn't match");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            login();
        }
    }
}