package com.JGG.HVPManagement.controller.collaborator;

import com.JGG.HVPManagement.dao.JobPositionDAO;
import com.JGG.HVPManagement.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class addCollaboratorController implements Initializable {
    public CheckBox chkActive;
    public TextField txtUsername;
    public TextField txtLastName;
    public TextField txtFirstName;
    public TextField txtId;
    public TextField txtEmail;
    public TextField txtIMSSNumber;
    public TextField txtCurpNumber;
    public TextField txtRFCNumber;
    public TextField txtMobilePhoneNumber;
    public TextField txtPhoneNumber;
    public DatePicker dtpStartingDate;
    public DatePicker dtpEndingDate;
    public DatePicker dtpStartingIMSSDate;
    public Label lblWorkedDays;
    public Label lblQuartersWorked;
    public ImageView imgPicture;
    public Spinner<Integer> spinnerWeeklyWorkingHours;
    public Label lblWageProportion;
    public Label lblGrossWage;
    public Label lblComissionBonusPercentage;
    public Label lblAverageDailyWage;
    public ChoiceBox<String> cboPaymentForm;
    public PasswordField txtPassword;
    public ChoiceBox<String> cboJobPosition;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.cboJobPosition.setItems(JobPositionDAO.getInstance().getJobPositionsNames());
            this.cboPaymentForm.setItems(Model.getInstance().paymentForms);
            this.chkActive.setSelected(true);
            

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save() {

    }
}
