package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.dao.AttendanceRegisterDAO;
import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public Label lblUser;
    public ComboBox<Branch> cboBranch;
    public ComboBox<String> cboAction;
    public Label lblDateHour;
    public Label lblLastRegister;
    public Label lblNextRegister;
    public Label lblStatus;
    public GridPane rootPane;
    public ButtonBar btnBarManager;
    private String lastActionRegistered;
    private Model model;
    private Utilities utilities;
    private Collaborator collaborator;
    private AttendanceRegister lastAttendanceRegister;
    private WorkSchedule nextWorkSchedule;
    private WorkSchedule realWorkSchedule;
    private AttendanceRegisterDAO attendanceRegisterDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        attendanceRegisterDAO = AttendanceRegisterDAO.getInstance();
        loadComboBoxes();
        refreshVariables();
        loadData();
        setRoleView();
    }

    private void loadComboBoxes() {
        cboBranch.getItems().addAll(model.branches);
        cboAction.getItems().addAll("Entrada", "Salida");
    }

    private void load() {
        refreshVariables();
        loadData();
    }

    private void refreshVariables() {
        collaborator = model.loggedUser.getCollaborator();
        lastAttendanceRegister = utilities.getLastAttendanceRegisterByCollaborator(collaborator);
        if (lastAttendanceRegister != null) {
            lastActionRegistered = lastAttendanceRegister.getAction();
        }
        nextWorkSchedule = utilities.getWorkScheduleByLastAttendanceRegister(lastAttendanceRegister, collaborator);
        realWorkSchedule = nextWorkSchedule;
        if (nextWorkSchedule != null && nextWorkSchedule.getLocalDate().isBefore(LocalDate.now())) {
            realWorkSchedule = utilities.getWorkScheduleWithHoursByCollaboratorAndDate(collaborator, LocalDate.now());
        }
    }

    private void loadData() {
        String user = model.loggedUser.getUserName();
        String lastRegister = "The collaborator has no previous registers";
        String nextSchedule = "The collaborator has no pending work schedules";
        String status = "There is no status";
        Branch branch = null;
        String action = null;
        String hour = model.DTF.format(LocalDateTime.now());

        if (lastAttendanceRegister != null) {
            lastRegister = String.valueOf(lastAttendanceRegister);
        }

        if (nextWorkSchedule != null) {
            if (lastActionRegistered == null || Objects.equals(lastActionRegistered, "Salida")) {
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: entrance. Hour: " + nextWorkSchedule.getStartingTime();
            } else {
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: exit. Hour: " + nextWorkSchedule.getEndingTime();
            }
        }

        if (realWorkSchedule != null) {
            action = "Entrada";
            if (lastAttendanceRegister != null) {
                if (lastAttendanceRegister.getLocalDateTime().toLocalDate().equals(LocalDate.now())) {
                    if (lastAttendanceRegister.getAction().equals("Entrada")) action = "Salida";
                }
            }
            status = getStatusText(action);
            branch = realWorkSchedule.getBranch();
        }

        lblUser.setText(user);
        lblLastRegister.setText(lastRegister);
        lblNextRegister.setText(nextSchedule);
        lblStatus.setText(status);
        cboBranch.getSelectionModel().select(branch);
        cboAction.getSelectionModel().select(action);
        lblDateHour.setText(hour);
    }

    private void setRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            rootPane.getChildren().remove(btnBarManager);
        }
    }

    private String getStatusText(String action) {
        String status;
        if (realWorkSchedule == null) {
            status = "You don't have to register today";
        } else {
            if (action.equals("Entrada")) {
                LocalDateTime realWorkScheduleLDT = LocalDateTime.of(realWorkSchedule.getLocalDate(), realWorkSchedule.getStartingTime());
                int minDifference = (int) ChronoUnit.MINUTES.between(realWorkScheduleLDT, LocalDateTime.now());
                if (minDifference < 5) {
                    status = "You are on time";
                    lblStatus.setStyle("-fx-background-color: greenyellow");
                } else if (minDifference < 16) {
                    status = "You are " + minDifference + " minutes late, but in the tolerance time";
                    lblStatus.setStyle("-fx-background-color: yellow");
                } else if (minDifference < 31) {
                    status = "You are " + minDifference + " minutes late: 1 tardy";
                    lblStatus.setStyle("-fx-background-color: indianred");
                } else if (minDifference < 120) {
                    status = "You are " + minDifference + " minutes late: 2 tardies";
                    lblStatus.setStyle("-fx-background-color: indianred");
                } else {
                    status = "You are " + minDifference + " minutes late: 3 tardies";
                    lblStatus.setStyle("-fx-background-color: indianred");
                }
            } else {
                LocalDateTime realWorkScheduleLDT = LocalDateTime.of(realWorkSchedule.getLocalDate(), realWorkSchedule.getEndingTime());
                int minDifference = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), realWorkScheduleLDT);
                if (minDifference <= 0) {
                    status = "You can leave. Good luck";
                } else {
                    status = "You can leave in " + minDifference + " minutes. Good work!";
                }
                lblStatus.setStyle("");
            }
        }
        return status;
    }

    public void register() {
        boolean isValid = true;
        String errorList = "It couldn't be registered because of the following errors:\n";
        String action = cboAction.getSelectionModel().getSelectedItem();
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        String status;
        Integer minutesDelay = null;
        if (action.equals("Entrada")) {
            LocalDateTime realWorkScheduleLDT = LocalDateTime.of(realWorkSchedule.getLocalDate(), realWorkSchedule.getStartingTime());
            int minDifference = (int) ChronoUnit.MINUTES.between(realWorkScheduleLDT, LocalDateTime.now());
            if (minDifference <=5) {
                status = "On Time";
            } else if (minDifference < 16) {
                status = "Tolerance";
            } else {
                status = "Late";
            }
            minutesDelay=minDifference;
        } else {
            LocalDateTime realWorkScheduleLDT = LocalDateTime.of(realWorkSchedule.getLocalDate(), realWorkSchedule.getEndingTime());
            int minDifference = (int) ChronoUnit.MINUTES.between(LocalTime.now(), realWorkScheduleLDT);
            if (minDifference <= 0) {
                status = "Exit on time";
            } else {
                status = "Exit before time";
            }
        }

        boolean registerExists = utilities.checkIfRegisterExists(collaborator, action, LocalDate.now());
        if (registerExists) {
            errorList += "There is already a register of " + action + " today";
            isValid = false;
        }

        if (action.equals(lastActionRegistered)) {
            String error = "Your last registered action was also a " + action + ". Are you sure that You want to register it?";
            boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", error);
            if (!answer) return;
        }

        if (isValid) {
            AttendanceRegister attendanceRegister = new AttendanceRegister(action, LocalDateTime.now(), status, minutesDelay, collaborator, branch);
            attendanceRegisterDAO.createAttendanceRegister(attendanceRegister);
            new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "The attendance register was saved successfully");
            model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
            load();
        } else {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", errorList);
        }
    }

    public void cancel() {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();
    }

    public void changeUser() {
        Model.role originalRole = model.roleView;
        model.openMainAfterLogin=false;
        utilities.loadWindowWithInitData("view/main/Login.fxml", new Stage(), "Login", StageStyle.DECORATED, false, true);
        model.openMainAfterLogin=true;
        load();
        Model.role newRole = model.roleView;
        if(originalRole!=newRole){
            changeRoleView();
        }
    }

    private void changeRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            rootPane.getChildren().remove(btnBarManager);
        } else{
            rootPane.getChildren().add(btnBarManager);
        }
    }

    public void reviewRegisters() {
        utilities.loadWindow("view/attendanceControl/ReviewRegisters.fxml", new Stage(), "Review Registers", StageStyle.DECORATED, true, false);
    }

    public void createAnIncidence() {
        model.incidentType= Incident.incidentTypes.ATTENDANCE_REGISTER;
        utilities.loadWindow("view/attendanceControl/Incident.fxml", new Stage(), "Create a new incident", StageStyle.DECORATED, false, true);
    }

    public void editRegisters() {
        utilities.loadWindow("view/attendanceControl/ChangeRegisters.fxml", new Stage(), "Review Registers", StageStyle.DECORATED, true, false);
    }

    public void reviewIncidences() {
    }
}
