package com.JGG.WeeklyScheduler.controller;

import com.JGG.WeeklyScheduler.dao.UserDAO;
import com.JGG.WeeklyScheduler.entity.User;
import com.JGG.WeeklyScheduler.model.Utilities;
import com.JGG.WeeklyScheduler.model.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.persistence.NoResultException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField txtUser;
    public PasswordField txtPass;
    public Button btnLogin;
    public Button btnCancel;


    public void login(){
        String userName = this.txtUser.getText().toUpperCase();
        String pass = this.txtPass.getText();

        User user = new User(userName, pass);

        boolean checkLogin =false;

        // check login
        try{
            User tempUser = UserDAO.getInstance().getUserbyUserName(user.getUser());
            if(user.getPass().equals(tempUser.getPass())) checkLogin=true;
        } catch (NoResultException ignore){
            System.out.println("User not found");
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
        if(keyEvent.getCode().toString().equals("ENTER")){
            login();
        }
    }
}
