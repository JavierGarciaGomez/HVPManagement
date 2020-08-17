package com.JGG.HVPManagement.controller.collaborator;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.JobPositionDAO;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
    
    public Label lblWageProportion;
    public Label lblGrossWage;
    public Label lblComissionBonusPercentage;
    public Label lblAverageDailyWage;
    public ChoiceBox<String> cboPaymentForm;
    public PasswordField txtPassword;
    public ChoiceBox<String> cboJobPosition;
    public Label seniorityPercentageWageBonus;
    public Button getMonthlyIncomeByPosition;
    public TextField txtMonthlyMinimumIncome;
    public HBox rootPane;
    public Spinner <Integer> spinnerWeeklyWorkingHours;
    public Label lblDegreeBonus;
    public Label lblSeniorityPercentageWageBonus;
    public CheckBox Degree;
    public CheckBox chkPostgraduate;
    public CheckBox chkDegree;
    public TextField txtWagePosition;
    public Label lblWageBase;
    public TextField lblFixedWageBonus;
    private Utilities utilities= Utilities.getInstance();
    private CollaboratorDAO collaboratorDAO=CollaboratorDAO.getInstance();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.cboJobPosition.setItems(JobPositionDAO.getInstance().getJobPositionsNames());
            this.cboPaymentForm.setItems(Model.getInstance().paymentForms);
            this.chkActive.setSelected(true);
            this.cboJobPosition.getSelectionModel().select("Asistente B");
            this.cboPaymentForm.getSelectionModel().select("Formal");
            this.txtId.setText(String.valueOf(collaboratorDAO.getMaxID()+1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save() {
        boolean isValid=true;
        String errorList="The collaborator can't be registered, because of the following errors: ";
        //getValues
        String StringId = txtId.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        Boolean isActive;

        // casts, conversions and validations
        if(dtpStartingDate==null){
            errorList+="The starting date must be registered";
            isValid=false;
        } else{
            if(dtpEndingDate==null){
                isActive=true;
            } else{
                isActive=false;
            }
        }
        




    }

    public void refresh() {
        LocalDate startingDate = dtpStartingDate.getValue(); // if not needed, because or is a correct date or is null
        LocalDate endingDate=utilities.getLocalDateOrReturnToday(dtpEndingDate.getValue());
        long numberOfDaysWorked=utilities.getDaysBetweenOrReturnZero(startingDate, endingDate);
        int quartersWorked=utilities.getQuartersWorkedOrReturnZero(startingDate, endingDate);
        double degreeBonus = 0;
        if (chkDegree.isSelected()) degreeBonus+=300;
        if (chkPostgraduate.isSelected()) degreeBonus+=300;
        double wageProportion = utilities.getDoubleOrReturnZero(spinnerWeeklyWorkingHours.getValue())/48;
        JobPosition jobPosition = JobPositionDAO.getInstance().getJobPositionbyName(cboJobPosition.getSelectionModel().getSelectedItem());
        double seniorityWageBonus=utilities.getSeniorityWageBonus(jobPosition.getYearlyPercentageWageBonus(), startingDate, endingDate);
        double wageBase = jobPosition.getPositionWage();
        double fixedWageBonus = utilities.convertStringToDouble((lblFixedWageBonus.getText()));
        double grossWage = utilities.getGrossWage(wageBase, wageProportion, seniorityWageBonus, degreeBonus, fixedWageBonus);

/*
        if(grossWage>0 && wageProportion>0){
            grossWage=wageBase*(1+seniorityWageBonus)*wageProportion+degreeBonus;
        }

        try{
            grossWage+=Double.parseDouble(lblFixedWageBonus.getText());
        } catch(Exception ignore){}
*/




        lblWorkedDays.setText(String.valueOf(numberOfDaysWorked));
        lblQuartersWorked.setText(String.valueOf(quartersWorked));

        lblDegreeBonus.setText(String.valueOf(degreeBonus));
        lblSeniorityPercentageWageBonus.setText((jobPosition.getPositionWage()*quartersWorked/4)*100+"%");
        lblWageBase.setText(String.format("%.2f", wageBase));
        lblSeniorityPercentageWageBonus.setText(String.format("%.2f", seniorityWageBonus)+"%");
        lblWageProportion.setText(String.format("%.2f", wageProportion*100)+"%");
        lblGrossWage.setText(String.format("%.2f", grossWage));

    }

    public void getMonthlyMinimumIncome() {
        JobPosition jobPosition = JobPositionDAO.getInstance().getJobPositionbyName(cboJobPosition.getSelectionModel().getSelectedItem());
        txtMonthlyMinimumIncome.setText(String.format("%.2f", jobPosition.getMinimumPositionIncome()));
    }
}
