package com.JGG.HVPManagement.controller.collaborator;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.JobPositionDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
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
    public Spinner<Integer> spinnerWeeklyWorkingHours;
    public Label lblDegreeBonus;
    public Label lblSeniorityPercentageWageBonus;
    public CheckBox Degree;
    public CheckBox chkPostgraduate;
    public CheckBox chkDegree;
    public TextField txtWagePosition;
    public Label lblWageBase;
    public TextField lblFixedWageBonus;
    public ChoiceBox<String> cboRole;
    public TextField txtEmergencyPhoneNumber;
    public TextField txtAddress;
    public Label lblContributionBaseWage;
    public CheckBox chkHasImss;
    private Utilities utilities;
    private CollaboratorDAO collaboratorDAO;
    private Model model;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get instances
        collaboratorDAO = CollaboratorDAO.getInstance();
        model = Model.getInstance();
        utilities = Utilities.getInstance();

        try {
            this.cboJobPosition.setItems(JobPositionDAO.getInstance().getJobPositionsNames());
            this.cboPaymentForm.setItems(model.paymentForms);
            this.cboRole.setItems(model.roles);

            this.chkActive.setSelected(true);
            this.cboJobPosition.getSelectionModel().select("Asistente B");
            this.cboPaymentForm.getSelectionModel().select("Formal");
            this.cboRole.getSelectionModel().select("User");
            this.txtId.setText(String.valueOf(collaboratorDAO.getMaxID() + 1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save() {
        refresh();
        boolean isValid = true;
        String errorList = "The collaborator can't be registered, because of the following errors:\n";
        //Collaborator values
        int id = Integer.parseInt(txtId.getText());
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        Boolean isActive = false;
        // User values
        String userName = txtUsername.getText();
        String pass = txtPassword.getText();
        String role = cboRole.getSelectionModel().getSelectedItem();
        // JobPosition
        JobPosition jobPosition = JobPositionDAO.getInstance().getJobPositionbyName(cboJobPosition.getSelectionModel().getSelectedItem());
        //DetailedCollaboratorInfo
        String curpNumber = txtCurpNumber.getText();
        String imssNumber = txtIMSSNumber.getText();
        String rfcNumber = txtRFCNumber.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String mobilePhoneNumber = txtMobilePhoneNumber.getText();
        String emergencyPhoneNumber = txtEmergencyPhoneNumber.getText();
        String email = txtEmail.getText();
        String address = txtAddress.getText();
        //Working conditions
        Integer weeklyWorkingHours = spinnerWeeklyWorkingHours.getValue();
        Double wageProportion = utilities.getDoubleOrReturnZero(spinnerWeeklyWorkingHours.getValue()) / 48;
        Double fixedWageBonus = utilities.convertStringToDoubleOrReturnZero((lblFixedWageBonus.getText()));
        Double degreeBonus = 0.0;
        Double seniorityWageBonus = 0.0;
        Double grossWage = 0.0;
        Double monthlyMinimumIncome = utilities.convertStringToDouble(txtMonthlyMinimumIncome.getText());
        Double comissionBonusPercentage=0.0;
        Double averageDailyWage = 0.0;
        Double contributionBaseWage = 0.0;
        String paymentForm = cboPaymentForm.getValue();
        Boolean hasImss = false;
        LocalDate startingDate = dtpStartingDate.getValue(); // if not needed, because or is a correct date or is null
        LocalDate endingDate = dtpEndingDate.getValue();
        LocalDate startingIMSSDate = dtpStartingIMSSDate.getValue();

        /*****************************************************
         * CASTS, CONVERSIONS, CALCULATIONS, AND VALIDATIONS *
         * ***************************************************/
        // COLLABORATOR
        if (dtpStartingDate.getValue() == null) {
            errorList += "The starting date must be registered\n";
            isValid = false;
        } else {
            if (dtpEndingDate.getValue() == null) {
                isActive = true;
            }
        }
        //test if the collaborator id is used
        if (collaboratorDAO.getCollaboratorbyId(id) != null) {
            errorList += "Id already registered\n";
            isValid = false;
        }
        //test if first and last name are at least three characters
        if (firstName.length() <= 3) {
            errorList += "The first name hast to have at least three characters\n";
            isValid = false;
        }
        //test if first and last name are at least three characters
        if (lastName.length() <= 3) {
            errorList += "The last name hast to have at least three characters\n";
            isValid = false;
        }
        // USER
        if (userName.length() != 3) {
            errorList += "The user must have three characters \n";
            isValid = false;
        }
        if (UserDAO.getInstance().getUserbyUserName(userName) != null) {
            errorList += "The userName is already registered \n";
            isValid = false;
        }
        if (pass.length() < 4 || pass.length() > 11) {
            errorList += "The password must have between 4 and 10 characters \n";
            isValid = false;
        }
        // WORKINGCONDITIONS
        degreeBonus = 0.0;
        if (chkDegree.isSelected()) degreeBonus += 300;
        if (chkPostgraduate.isSelected()) degreeBonus += 300;

        LocalDate fakeEndingDate = utilities.getLocalDateOrReturnToday(dtpEndingDate.getValue());
        seniorityWageBonus = utilities.getSeniorityWageBonus(jobPosition.getYearlyPercentageWageBonus(), startingDate, endingDate);
        double wageBase = jobPosition.getPositionWage();
        grossWage = utilities.getGrossWage(wageBase, wageProportion, seniorityWageBonus, degreeBonus, fixedWageBonus);
        int quartersWorked = utilities.getQuartersWorkedOrReturnZero(startingDate, fakeEndingDate);
        comissionBonusPercentage=((int)quartersWorked/2)*.05;;
        averageDailyWage = 0.0;
        contributionBaseWage = 0.0;
        if (dtpStartingIMSSDate.getValue() != null) {
            hasImss=true;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(errorList);
            alert.showAndWait();
            // todo check
            return;
        } else {

        }

        // Instantiations
        Collaborator collaborator = new Collaborator();
        collaborator.setId(id);
        collaborator.setFirstName(firstName);
        collaborator.setLastName(lastName);
        collaborator.setActive(isActive);

        User user = new User();
        user.setUserName(userName);
        user.setPass(pass);
        user.setRole(role);
        System.out.println(user);
        collaborator.setUser(user);

        collaborator.setJobPosition(jobPosition);

        DetailedCollaboratorInfo detailedCollaboratorInfo = new DetailedCollaboratorInfo();
        detailedCollaboratorInfo.setCurpNumber(curpNumber);
        detailedCollaboratorInfo.setImssNumber(imssNumber);
        detailedCollaboratorInfo.setRfcNumber(rfcNumber);
        detailedCollaboratorInfo.setEmail(email);
        detailedCollaboratorInfo.setPhoneNumber(phoneNumber);
        detailedCollaboratorInfo.setMobilePhoneNumber(mobilePhoneNumber);
        detailedCollaboratorInfo.setEmergencyPhoneNumber(emergencyPhoneNumber);
        detailedCollaboratorInfo.setAddress(address);
        collaborator.setDetailedCollaboratorInfo(detailedCollaboratorInfo);

        WorkingConditions workingConditions = new WorkingConditions();
        workingConditions.setWeeklyWorkingHours(weeklyWorkingHours);
        workingConditions.setWageProportion(wageProportion);
        workingConditions.setFixedWageBonus(fixedWageBonus);
        workingConditions.setDegreeBonus(degreeBonus);
        workingConditions.setSeniorityWageBonus(seniorityWageBonus);
        workingConditions.setGrossWage(grossWage);
        workingConditions.setMonthlyMinimumIncome(monthlyMinimumIncome);
        workingConditions.setCommissionBonusPercentage(comissionBonusPercentage);
        workingConditions.setAverageDailyWage(averageDailyWage);
        workingConditions.setContributionBaseWage(contributionBaseWage);
        workingConditions.setPaymentForm(paymentForm);
        workingConditions.setHasIMSS(hasImss);
        workingConditions.setStartingDate(startingDate);
        workingConditions.setEndingDate(endingDate);
        workingConditions.setStartingIMSSDate(startingIMSSDate);
        collaborator.setWorkingConditions(workingConditions);

        System.out.println(collaborator);

        CollaboratorDAO.getInstance().createCollaborator(collaborator);
        //addPicture(user);




    }

    public void refresh() {
        LocalDate startingDate = dtpStartingDate.getValue(); // if not needed, because or is a correct date or is null
        LocalDate endingDate = utilities.getLocalDateOrReturnToday(dtpEndingDate.getValue());
        long numberOfDaysWorked = utilities.getDaysBetweenOrReturnZero(startingDate, endingDate);
        int quartersWorked = utilities.getQuartersWorkedOrReturnZero(startingDate, endingDate);
        double degreeBonus = 0;
        if (chkDegree.isSelected()) degreeBonus += 300;
        if (chkPostgraduate.isSelected()) degreeBonus += 300;
        double wageProportion = utilities.getDoubleOrReturnZero(spinnerWeeklyWorkingHours.getValue()) / 48;
        JobPosition jobPosition = JobPositionDAO.getInstance().getJobPositionbyName(cboJobPosition.getSelectionModel().getSelectedItem());
        double seniorityWageBonus = utilities.getSeniorityWageBonus(jobPosition.getYearlyPercentageWageBonus(), startingDate, endingDate);
        double wageBase = jobPosition.getPositionWage();
        double fixedWageBonus = utilities.convertStringToDoubleOrReturnZero((lblFixedWageBonus.getText()));
        double grossWage = utilities.getGrossWage(wageBase, wageProportion, seniorityWageBonus, degreeBonus, fixedWageBonus);
        if(txtMonthlyMinimumIncome.getText().equals("")) setMonthlyMinimumIncome();
        double comissionBonusPercentage=((int)(quartersWorked/2))*.05;
        System.out.println(dtpStartingIMSSDate.getValue());
        if (dtpStartingIMSSDate.getValue() != null) {
            chkHasImss.setSelected(true);
        } else{
            chkHasImss.setSelected(false);
        }
        if (dtpEndingDate.getValue() != null) {
            chkActive.setSelected(false);
        } else{
            chkActive.setSelected(true);
        }



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
        lblSeniorityPercentageWageBonus.setText((jobPosition.getPositionWage() * quartersWorked / 4) * 100 + "%");
        lblWageBase.setText(String.format("%.2f", wageBase));
        lblSeniorityPercentageWageBonus.setText(String.format("%.2f", seniorityWageBonus) + "%");
        lblWageProportion.setText(String.format("%.2f", wageProportion * 100) + "%");
        lblGrossWage.setText(String.format("%.2f", grossWage));
        lblComissionBonusPercentage.setText(String.format("%.2f", comissionBonusPercentage * 100) + "%");

    }

    public void setMonthlyMinimumIncome() {
        JobPosition jobPosition = JobPositionDAO.getInstance().getJobPositionbyName(cboJobPosition.getSelectionModel().getSelectedItem());
        txtMonthlyMinimumIncome.setText(String.format("%.2f", jobPosition.getMinimumPositionIncome()));
    }

    public void validateNumbers(KeyEvent keyEvent) {
        TextField textField = (TextField) keyEvent.getSource();
        DecimalFormat format = new DecimalFormat("#.0");
        textField.setTextFormatter(new TextFormatter<>(c ->
        {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            } else {
                return c;
            }
        }));
    }

    public void generateUserName() {
        try {
            String name = txtFirstName.getText();
            String lastName = txtLastName.getText();
            String fullName = name + " " + lastName;
            String[] nameElements = fullName.split("\\s+");

            char firstChar = nameElements[0].charAt(0);
            char secondChar = nameElements[nameElements.length - 2].charAt(0);
            char thirdChar = nameElements[nameElements.length - 1].charAt(0);
            String userName = (firstChar + Character.toString(secondChar) + thirdChar).toUpperCase();
            userName = utilities.normalizeText(userName);
            txtUsername.setText(userName);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }
}
