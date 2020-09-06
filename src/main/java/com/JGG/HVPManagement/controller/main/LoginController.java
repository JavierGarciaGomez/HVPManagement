package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.dao.LogDAO;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements MyInitializable {
    public TextField txtUser;
    public PasswordField txtPass;
    public Button btnLogin;
    public Button btnCancel;
    public GridPane rootPane;
    private LogDAO logDao;
    private Model model;
    private Utilities utilities;
    private Stage thisStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        logDao=LogDAO.getInstance();
        // todo delete
        txtUser.setText("JGG");
        txtPass.setText("password");
    }

    public void initData(){
        this.thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.setOnHiding(event -> {
            if(model.openMainAfterLogin){
                Utilities.getInstance().loadWindow("view/main/Main.fxml", new Stage(), "Main Window", StageStyle.DECORATED, false, false);
            }
        });
    }

    public void login() {
        String userName = this.txtUser.getText().toUpperCase();
        String pass = this.txtPass.getText();

        boolean checkLogin = false;

        User tempUser = utilities.getCollaboratorFromUserName(userName).getUser();
        User previousUser = model.loggedUser;
        if (tempUser != null) {
            if (pass.equals(tempUser.getPass())) checkLogin = true;
        }

        if (checkLogin) {
            Runnable runnable = () -> {
                if(previousUser!=null){
                    logDao.exitSession(previousUser);
                }

                logDao.createLog(tempUser);
            };
            new Thread(runnable).start();
            model.loggedUser = tempUser;
            model.roleView = model.loggedUser.getRole();
            closeAndReturn();
        } else {
            Utilities.getInstance().showAlert(Alert.AlertType.ERROR, "Non-existent user", "The user and the password doesn't match");
        }
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            login();
        }
    }

    public void changePassword() {
        Utilities.getInstance().loadWindow("view/main/ChangePass.fxml", new Stage(), "Change password", StageStyle.DECORATED, false, false);

    }

    public void cancel() {
        closeAndReturn();
    }

    private void closeAndReturn() {
        thisStage.hide();
    }
}
