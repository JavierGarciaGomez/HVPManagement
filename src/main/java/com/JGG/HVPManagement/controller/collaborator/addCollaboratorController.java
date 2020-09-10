package com.JGG.HVPManagement.controller.collaborator;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class addCollaboratorController implements Initializable {
    public CheckBox chkActive;
    public TextField txtUsername;
    public TextField txtLastName;
    public TextField txtFirstName;
    public TextField txtCollaboratorId;
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
    public ComboBox<String> cboPaymentForm;
    public PasswordField txtPassword;
    public ComboBox<JobPosition> cboJobPosition;
    public TextField txtMonthlyMinimumIncome;
    public HBox rootPane;
    public Spinner<Integer> spinnerWeeklyWorkingHours;
    public Label lblDegreeBonus;
    public Label lblSeniorityPercentageWageBonus;
    public CheckBox chkPostgraduate;
    public CheckBox chkDegree;
    public Label lblWageBase;
    public TextField txtFixedWageBonus;
    public ComboBox<Model.role> cboRole;
    public TextField txtEmergencyPhoneNumber;
    public Label lblContributionBaseWage;
    public CheckBox chkHasImss;
    public HBox paneRole;
    public TextArea txaAddress;
    public HBox paneWageBase;
    public HBox panefixedWageBonus;
    public HBox paneDegreeBonus;
    public HBox paneSeniorityWageBonus;
    public HBox paneWageProportion;
    public HBox paneGrossWage;
    public HBox paneMonthlyMinimumIncome;
    public HBox paneContributionBaseWage;
    public HBox paneAverageDailyWage;
    public HBox paneComissionBonusPercentage;
    public HBox panePaymentForm;
    public HBox paneDegree;
    public ComboBox<String> cboUserNames;
    public Button btnCancelEditUpdate;
    public Button btnEditView;
    public Button btnAddNewView;
    public Button btnRefresh;
    public Button btnSave;
    public Button btnAddPicture;
    private final Utilities utilities = Utilities.getInstance();
    private final CollaboratorDAO collaboratorDAO = CollaboratorDAO.getInstance();
    private final Model model = Model.getInstance();
    private final Runnables runnables = Runnables.getInstance();
    private List<File> files;
    public enum actionTypes {UPDATE, ADD_NEW, SHOW}
    public actionTypes actionType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.selectedCollaborator = model.loggedUser.getCollaborator();
        Thread thread = runnables.runCollaborators();
        Thread thread1 = runnables.runJobPositions();

        actionType = actionTypes.SHOW;
        setToolTips();

        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {
            utilities.showAlert(Alert.AlertType.ERROR, "ERROR", "Error");
        }
        initComboBoxes();

        showViewShow();

        model.roleView=model.loggedUser.getRole();

        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            btnEditView.setVisible(false);
            btnAddNewView.setVisible(false);
            cboUserNames.setDisable(true);
        }
    }

    private void initComboBoxes() {
        this.cboUserNames.setItems(FXCollections.observableList(model.allUserNames));
        this.cboJobPosition.setItems(FXCollections.observableList(model.jobPositions));
        this.cboPaymentForm.setItems(model.paymentForms);
        this.cboRole.setItems(FXCollections.observableList(model.roles));
    }

    private void setToolTips() {
        Tooltip.install(paneRole, new Tooltip("Roles:\n " +
                "Admin-has root access to all the elements of the application\n" +
                "Manager-can access to all the application funcionalities\n" +
                "User-can access to some functionalities"));
        Tooltip.install(paneWageBase, new Tooltip("Is the wage that corresponds to the job position according to the salary scale"));
        Tooltip.install(panefixedWageBonus, new Tooltip("Fixed wage bonus: in some cases there is an extra bonus fixed to the collaborator. Normally because of an agreement before the contract"));
        Tooltip.install(paneDegreeBonus, new Tooltip("In some cases there is an extra bonus fixed to the collaborator.jornada laboral semanal Normally because of an agreement before the contract"));
        Tooltip.install(paneSeniorityWageBonus, new Tooltip("Is an yearly increment of the wage in recognition of the collaborator's seniority"));
        Tooltip.install(paneWageProportion, new Tooltip("Is the wage proportion that is paid to the collaborator according to the weekly working time"));
        Tooltip.install(paneGrossWage, new Tooltip("Is the wage actually paid to the collaborator without discounting the withholdings or adding the commissions"));
        Tooltip.install(paneMonthlyMinimumIncome, new Tooltip("Is the minimum wage paid to a collaborator according to the job position. In some cases this is superior because of an agreement with the collaborator before the contract"));
        Tooltip.install(paneContributionBaseWage, new Tooltip("NOT WORKING. Is the daily contribution base wage, that is used to calculate the social security contributions"));
        Tooltip.install(paneAverageDailyWage, new Tooltip("NOT WORKING. Is the average daily income of the last 12 months. It's used for calculations in cases of vacations, Christmas bonus and paid absences"));
        Tooltip.install(paneComissionBonusPercentage, new Tooltip("Is an six-monthly increment of the commissions in recognition of the collaborator's seniority"));
        Tooltip.install(panePaymentForm, new Tooltip("Payment forms: \n" +
                "Formal: is the normal way of payment. The collaborator is in the payroll and registered to the social security\n" +
                "Guarantedd: is the same as formal, but the attendance control is not used to calculate the income\n" +
                "Informal: the calculation of the income is the same as formal, but there isn't withholdings of the social security\n" +
                "Hourly: the collaborator is paid by the hour\n" +
                "Utilities: the collaborator is not in the payroll and is paid according to the utilities of the company utilities "));
        Tooltip.install(btnAddPicture, new Tooltip("Drag and drop a picture, then click this button to save it"));
    }


    public void showViewShow() {
        btnCancelEditUpdate.setVisible(false);
        btnEditView.setVisible(true);
        btnRefresh.setVisible(false);
        btnSave.setVisible(false);
        btnAddNewView.setVisible(true);

        actionType = actionTypes.SHOW;
        setImage();
        setEditables(false);
        cboUserNames.setVisible(true);
        cboUserNames.setDisable(false);

        txtUsername.setVisible(false);
        Collaborator collaborator = model.selectedCollaborator;
        cboUserNames.getSelectionModel().select(collaborator.getUser().getUserName());
        txtUsername.setText(collaborator.getUser().getUserName());
        txtFirstName.setText(collaborator.getFirstName());
        txtLastName.setText(collaborator.getLastName());
        cboJobPosition.getSelectionModel().select(collaborator.getJobPosition());
        txtCollaboratorId.setText(String.valueOf(collaborator.getCollaboratorId()));
        cboRole.getSelectionModel().select(collaborator.getUser().getRole());
        txtUsername.setText(collaborator.getUser().getUserName());
        txtPassword.setText(collaborator.getUser().getPass());
        chkActive.setSelected(collaborator.getActive());
        txtEmail.setText(collaborator.getDetailedCollaboratorInfo().getEmail());
        txtPhoneNumber.setText(collaborator.getDetailedCollaboratorInfo().getPhoneNumber());
        txtMobilePhoneNumber.setText(collaborator.getDetailedCollaboratorInfo().getMobilePhoneNumber());
        txtEmergencyPhoneNumber.setText(collaborator.getDetailedCollaboratorInfo().getEmergencyPhoneNumber());
        txtRFCNumber.setText(collaborator.getDetailedCollaboratorInfo().getRfcNumber());
        txtCurpNumber.setText(collaborator.getDetailedCollaboratorInfo().getCurpNumber());
        txtIMSSNumber.setText(collaborator.getDetailedCollaboratorInfo().getImssNumber());
        txaAddress.setText(collaborator.getDetailedCollaboratorInfo().getAddress());
        dtpStartingDate.setValue(collaborator.getWorkingConditions().getStartingDate());
        dtpStartingIMSSDate.setValue(collaborator.getWorkingConditions().getStartingIMSSDate());
        dtpEndingDate.setValue(collaborator.getWorkingConditions().getEndingDate());
        spinnerWeeklyWorkingHours.getValueFactory().setValue(collaborator.getWorkingConditions().getWeeklyWorkingHours());
        txtFixedWageBonus.setText(utilities.convertDoubleToString(collaborator.getWorkingConditions().getFixedWageBonus()));
        lblDegreeBonus.setText(String.valueOf(collaborator.getWorkingConditions().getDegreeBonus()));
        if (collaborator.getWorkingConditions().getDegreeBonus() >= model.degreeBonus) {
            chkDegree.setSelected(true);
            if (collaborator.getWorkingConditions().getDegreeBonus() >= model.degreeBonus * 2) {
                chkPostgraduate.setSelected(true);
            }
        }
        lblSeniorityPercentageWageBonus.setText(String.valueOf(collaborator.getWorkingConditions().getCommissionBonusPercentage()));
        lblWageProportion.setText(String.valueOf(collaborator.getWorkingConditions().getWageProportion()));
        lblGrossWage.setText(String.valueOf(collaborator.getWorkingConditions().getGrossWage()));
        txtMonthlyMinimumIncome.setText(utilities.convertDoubleToString(collaborator.getWorkingConditions().getMonthlyMinimumIncome()));
        lblContributionBaseWage.setText(String.valueOf(collaborator.getWorkingConditions().getContributionBaseWage()));
        lblAverageDailyWage.setText(String.valueOf(collaborator.getWorkingConditions().getAverageDailyWage()));
        lblComissionBonusPercentage.setText(String.valueOf(collaborator.getWorkingConditions().getCommissionBonusPercentage()));
        cboPaymentForm.getSelectionModel().select(collaborator.getWorkingConditions().getPaymentForm());
        refresh();
    }

    public void editView() {
        showViewShow();
        setEditables(true);
        btnCancelEditUpdate.setVisible(true);
        btnEditView.setVisible(false);
        btnAddNewView.setVisible(false);
        btnRefresh.setVisible(true);
        btnSave.setVisible(true);
        cboUserNames.setDisable(true);
        cboUserNames.getEditor().setDisable(true);
        actionType = actionTypes.UPDATE;
    }

    public void showViewAddNew() {
        actionType = actionTypes.ADD_NEW;
        setEditables(true);

        btnCancelEditUpdate.setVisible(true);
        btnEditView.setVisible(false);
        btnRefresh.setVisible(true);
        btnSave.setVisible(true);
        btnAddNewView.setVisible(false);

        cboUserNames.setVisible(false);
        txtUsername.setVisible(true);
        txtFirstName.setText("");
        txtLastName.setText("");
        cboJobPosition.getSelectionModel().select(utilities.getJobPositionByName("Asistente B"));
        txtCollaboratorId.setText(String.valueOf(utilities.getMaxCollaboratorId() + 1));
        cboRole.getSelectionModel().select(Model.role.USER);
        txtUsername.setText("");
        txtPassword.setText("");
        chkActive.setSelected(true);
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtMobilePhoneNumber.setText("");
        txtEmergencyPhoneNumber.setText("");
        txtRFCNumber.setText("");
        txtCurpNumber.setText("");
        txtIMSSNumber.setText("");
        txaAddress.setText("");
        dtpStartingDate.setValue(null);
        dtpStartingIMSSDate.setValue(null);
        dtpEndingDate.setValue(null);
        spinnerWeeklyWorkingHours.getValueFactory().setValue(48);
        txtFixedWageBonus.setText("");
        lblDegreeBonus.setText("");
        chkDegree.setSelected(false);
        chkPostgraduate.setSelected(false);
        lblSeniorityPercentageWageBonus.setText("");
        lblWageProportion.setText("");
        lblGrossWage.setText("");
        txtMonthlyMinimumIncome.setText("");
        lblContributionBaseWage.setText("");
        lblAverageDailyWage.setText("");
        lblComissionBonusPercentage.setText("");
        cboPaymentForm.getSelectionModel().select("Informal");
    }

    public void setEditables(boolean editable) {
        txtUsername.setVisible(editable);
        txtFirstName.setEditable(editable);
        txtLastName.setEditable(editable);
        txtCollaboratorId.setEditable(editable);
        txtPassword.setEditable(editable);
        chkActive.setDisable(!editable);
        txtEmail.setEditable(editable);
        txtPhoneNumber.setEditable(editable);
        txtMobilePhoneNumber.setEditable(editable);
        txtEmergencyPhoneNumber.setEditable(editable);
        txtRFCNumber.setEditable(editable);
        txtCurpNumber.setEditable(editable);
        txtIMSSNumber.setEditable(editable);
        txaAddress.setEditable(editable);
        dtpStartingDate.setDisable(!editable);
        dtpStartingIMSSDate.setDisable(!editable);
        dtpEndingDate.setDisable(!editable);
        spinnerWeeklyWorkingHours.setEditable(editable);
        txtFixedWageBonus.setEditable(editable);
        chkDegree.setSelected(false);
        chkPostgraduate.setSelected(false);
        txtMonthlyMinimumIncome.setEditable(editable);
        cboPaymentForm.getSelectionModel().select("Informal");
    }


    public void save() {
        refresh();

        boolean isValid = true;
        String errorList = "The collaborator can't be registered, because of the following errors:\n";
        //Collaborator values
        int collaboratorId = Integer.parseInt(txtCollaboratorId.getText());
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        boolean isActive = (dtpEndingDate.getValue() == null);
        // User values
        String userName = txtUsername.getText();
        String pass = txtPassword.getText();
        Model.role role = cboRole.getSelectionModel().getSelectedItem();
        // JobPosition
        JobPosition jobPosition = cboJobPosition.getSelectionModel().getSelectedItem();
        //DetailedCollaboratorInfo
        String curpNumber = txtCurpNumber.getText();
        String imssNumber = txtIMSSNumber.getText();
        String rfcNumber = txtRFCNumber.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String mobilePhoneNumber = txtMobilePhoneNumber.getText();
        String emergencyPhoneNumber = txtEmergencyPhoneNumber.getText();
        String email = txtEmail.getText();
        String address = txaAddress.getText();
        //Working conditions
        Integer weeklyWorkingHours = spinnerWeeklyWorkingHours.getValue();
        double wageProportion = utilities.getDoubleOrReturnZero(spinnerWeeklyWorkingHours.getValue()) / 48;
        double fixedWageBonus = utilities.convertStringToDoubleOrReturnZero((txtFixedWageBonus.getText()));
        double degreeBonus;
        double seniorityWageBonus;
        double grossWage;
        double monthlyMinimumIncome = utilities.convertStringToDouble(txtMonthlyMinimumIncome.getText()) * wageProportion;
        double comissionBonusPercentage;
        double averageDailyWage;
        double contributionBaseWage;
        String paymentForm = cboPaymentForm.getValue();
        boolean hasImss = false;
        LocalDate startingDate = dtpStartingDate.getValue(); // if not needed, because or is a correct date or is null
        LocalDate endingDate = dtpEndingDate.getValue();
        LocalDate startingIMSSDate = dtpStartingIMSSDate.getValue();


        /*
         CALCULATIONS                                      *
         */

        // WORKINGCONDITIONS
        degreeBonus = 0.0;
        if (chkDegree.isSelected()) degreeBonus += model.degreeBonus;
        if (chkPostgraduate.isSelected()) degreeBonus += model.degreeBonus;

        LocalDate fakeEndingDate = utilities.getLocalDateOrReturnToday(dtpEndingDate.getValue());
        seniorityWageBonus = utilities.getSeniorityWageBonus(jobPosition.getYearlyPercentageWageBonus(), startingDate, endingDate);
        double wageBase = jobPosition.getPositionWage();
        grossWage = utilities.getGrossWage(wageBase, wageProportion, seniorityWageBonus, degreeBonus, fixedWageBonus);
        int quartersWorked = utilities.getQuartersWorkedOrReturnZero(startingDate, fakeEndingDate);
        comissionBonusPercentage = Math.floor(quartersWorked * 0.5) * .05;
        averageDailyWage = 0.0;
        contributionBaseWage = 0.0;
        if (dtpStartingIMSSDate.getValue() != null) {
            hasImss = true;
        }

        /*
         *************VALIDATIONS**********
         * */
        // validation just when is a new collaborator
        if (actionType == actionTypes.ADD_NEW) {
            if (userName.length() != 3) {
                errorList += "The user must have three characters \n";
                isValid = false;
            }
            if (utilities.isCollaboratorIdUsed(collaboratorId)) {
                errorList += "Id already registered\n";
                isValid = false;
            }
            if (utilities.getCollaboratorFromUserName(userName) != null) {
                errorList += "The userName is already registered \n";
                isValid = false;
            }
        }

        // COLLABORATOR
        if (dtpStartingDate.getValue() == null) {
            errorList += "The starting date must be registered\n";
            isValid = false;
        }
        //test if the collaborator id is used
        if (firstName.length() <= 3) {
            errorList += "The first name hast to have at least three characters\n";
            isValid = false;
        }
        //test if first and last name are at least three characters
        if (lastName.length() <= 3) {
            errorList += "The last name hast to have at least three characters\n";
            isValid = false;
        }
        if (pass.length() < 4 || pass.length() > 11) {
            errorList += "The password must have between 4 and 10 characters \n";
            isValid = false;
        }


        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(errorList);
            alert.showAndWait();
            return;
        }

        // Instantiations
        Collaborator collaborator;
        User user;
        DetailedCollaboratorInfo detailedCollaboratorInfo;
        WorkingConditions workingConditions;

        if (actionType == actionTypes.UPDATE) {
            collaborator = model.selectedCollaborator;
            user = collaborator.getUser();
            detailedCollaboratorInfo = collaborator.getDetailedCollaboratorInfo();
            workingConditions = collaborator.getWorkingConditions();
        } else {
            collaborator = new Collaborator();
            user = new User();
            detailedCollaboratorInfo = new DetailedCollaboratorInfo();
            workingConditions = new WorkingConditions();
        }

        collaborator.setCollaboratorId(collaboratorId);
        collaborator.setFirstName(firstName);
        collaborator.setLastName(lastName);
        collaborator.setActive(isActive);

        user.setUserName(userName);
        user.setPass(pass);
        user.setRole(role);
        user.setCollaborator(collaborator);
        collaborator.setUser(user);

        collaborator.setJobPosition(jobPosition);

        detailedCollaboratorInfo.setCurpNumber(curpNumber);
        detailedCollaboratorInfo.setImssNumber(imssNumber);
        detailedCollaboratorInfo.setRfcNumber(rfcNumber);
        detailedCollaboratorInfo.setEmail(email);
        detailedCollaboratorInfo.setPhoneNumber(phoneNumber);
        detailedCollaboratorInfo.setMobilePhoneNumber(mobilePhoneNumber);
        detailedCollaboratorInfo.setEmergencyPhoneNumber(emergencyPhoneNumber);
        detailedCollaboratorInfo.setAddress(address);
        detailedCollaboratorInfo.setCollaborator(collaborator);
        collaborator.setDetailedCollaboratorInfo(detailedCollaboratorInfo);

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
        workingConditions.setCollaborator(collaborator);
        collaborator.setWorkingConditions(workingConditions);


        collaboratorDAO.createOrUpdateCollaborator(collaborator);

        if (actionType == actionTypes.ADD_NEW) {
            utilities.loadCollaborators();
        } else {
            Runnable runnable = utilities::loadCollaborators;
            new Thread(runnable).start();
        }

        utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "The collaborator was saved succesfully");

        model.selectedCollaborator = collaborator;
        if (actionType.equals(actionTypes.ADD_NEW)) {
            initComboBoxes();
        }

        cboUserNames.getSelectionModel().select(collaborator.getUser().getUserName());
        showViewShow();
    }

    public void refresh() {
        LocalDate startingDate = dtpStartingDate.getValue(); // if not needed, because or is a correct date or is null
        LocalDate endingDate = utilities.getLocalDateOrReturnToday(dtpEndingDate.getValue());
        long numberOfDaysWorked = utilities.getDaysBetweenOrReturnZero(startingDate, endingDate);
        int quartersWorked = utilities.getQuartersWorkedOrReturnZero(startingDate, endingDate);
        double degreeBonus = 0;
        if (chkDegree.isSelected()) degreeBonus += model.degreeBonus;
        if (chkPostgraduate.isSelected()) degreeBonus += model.degreeBonus;
        double wageProportion = utilities.getDoubleOrReturnZero(spinnerWeeklyWorkingHours.getValue()) / 48;
        JobPosition jobPosition = cboJobPosition.getSelectionModel().getSelectedItem();
        double seniorityWageBonus = utilities.getSeniorityWageBonus(jobPosition.getYearlyPercentageWageBonus(), startingDate, endingDate);
        double wageBase = jobPosition.getPositionWage();
        double fixedWageBonus = utilities.convertStringToDoubleOrReturnZero((txtFixedWageBonus.getText()));
        double grossWage = utilities.getGrossWage(wageBase, wageProportion, seniorityWageBonus, degreeBonus, fixedWageBonus);

        if ((txtMonthlyMinimumIncome.getText().equals(""))
                || (utilities.convertStringToDouble(txtMonthlyMinimumIncome.getText())
                < getMonthlyMinimumIncome() * wageProportion)) {
            txtMonthlyMinimumIncome.setText(String.format("%.2f", getMonthlyMinimumIncome() * wageProportion));
        }

        double comissionBonusPercentage = Math.floor(quartersWorked * 0.5) * .05;

        chkHasImss.setSelected(dtpStartingIMSSDate.getValue() != null);
        chkActive.setSelected(dtpEndingDate.getValue() == null);

        // Setting the labels
        lblWorkedDays.setText(String.valueOf(numberOfDaysWorked));
        lblQuartersWorked.setText(String.valueOf(quartersWorked));
        lblDegreeBonus.setText(String.valueOf(degreeBonus));
        lblWageBase.setText(String.format("$ " + "%.2f", wageBase));
        lblSeniorityPercentageWageBonus.setText(String.format("%.2f", seniorityWageBonus * 100) + "%");
        lblWageProportion.setText(String.format("%.2f", wageProportion * 100) + "%");
        lblGrossWage.setText("$ " + String.format("%.2f", grossWage));
        lblComissionBonusPercentage.setText(String.format("%.2f", comissionBonusPercentage * 100) + "%");
    }

    public double getMonthlyMinimumIncome() {
        JobPosition jobPosition = cboJobPosition.getSelectionModel().getSelectedItem();
        return jobPosition.getMinimumPositionIncome();
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
        if (actionType.equals(actionTypes.ADD_NEW)) {
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

    public void changeSelectedUser() {
        model.selectedCollaborator = utilities.getCollaboratorFromUserName(cboUserNames.getSelectionModel().getSelectedItem());
        if (actionType.equals(actionTypes.SHOW)) {
            showViewShow();
        }
    }


    private void setImage() {
        Image image = new Image("/images/unknown.png");
        if (model.selectedCollaborator != null) {
            try {
                image = new Image("/images/" + model.selectedCollaborator.getUser().getUserName() + ".png");
            } catch (IllegalArgumentException ignore) {
            }
        }
        imgPicture.setImage(image);
    }


    public void saveOrChangePicture() {
        File copy = new File("res\\" + model.selectedCollaborator.getUser().getUserName() + ".png");
        List<Integer> bytes = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(files.get(0));
            int stream;
            while ((stream = fileInputStream.read()) != -1) {
                bytes.add(stream);
            }
            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(copy, true);
            for (Integer b : bytes) {
                fileOutputStream.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    public void handleOnDragDropped(DragEvent dragEvent) throws FileNotFoundException {
        files = dragEvent.getDragboard().getFiles();
        Image image = new Image(new FileInputStream(files.get(0)));
        imgPicture.setImage(image);
    }

}
