package com.JGG.HVPManagement.controller.main;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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
    private User loggedUser;
    public BorderPane rootPane;
    public ImageView imageView;
    private HibernateConnection hibernateConnection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hibernateConnection = HibernateConnection.getInstance();
        this.loggedUser = Model.getInstance().loggedUser;
        setImage();
        if (loggedUser != null) txtUserName.setText(loggedUser.getName() + "\n" + loggedUser.getLastName());


    }

    private void setImage() {
        try {
            File file = new File("res\\unknown.png");
            if (loggedUser != null) {
                File tempFile = new File("res\\" + loggedUser.getUser() + ".png");
                if (tempFile.exists()) file = tempFile;
            }
            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogin() {
        Utilities.getInstance().loadWindow("view/main/Login.fxml", new Stage(), "Login Window", StageStyle.DECORATED,
                false, false);
    }


    public void showAttendanceControl() {
        Utilities.getInstance().loadWindow("view/AttendanceControl.fxml", new Stage(), "Attendance Control",
                StageStyle.DECORATED, true, true);
    }

    public void showManageUser() {
        Model.getInstance().selectedColaborator=CollaboratorDAO.getInstance().getCollaboratorbyId(1);
        Utilities.getInstance().loadWindow("view/collaborator/showCollaborator.fxml", new Stage(), "Attendance Control",
                StageStyle.DECORATED, true, true);

    }
}
