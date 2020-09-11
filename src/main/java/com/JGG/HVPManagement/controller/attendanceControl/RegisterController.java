package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.dao.AttendanceRegisterDAO;
import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public static final Model model = Model.getInstance();
    public static final Utilities utilities = Utilities.getInstance();
    private Collaborator collaborator;
    private AttendanceRegister lastAttendanceRegister;
    private WorkSchedule nextWorkSchedule;
    private final AttendanceRegisterDAO attendanceRegisterDAO = AttendanceRegisterDAO.getInstance();
    private final Runnables runnables = Runnables.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshVariables();
        loadComboBoxes();
        loadData();
        setRoleView();
    }

    private void refreshVariables() {
        Thread branchesThread = runnables.runBranches();
        LocalDate startDate = utilities.getFirstDayOfTheFortNight(LocalDate.now());
        LocalDate endDate = utilities.getLastDayOfTheFortNight(LocalDate.now());
        collaborator = model.loggedUser.getCollaborator();
        Thread attendanceRegistersThread = runnables.runAttendanceRegistersBetweenDatesByCollaborator(startDate, endDate, collaborator);
        Thread workSchedulesThread = runnables.runWorkSchedulesBetweenDatesByCollaborator(startDate, endDate, collaborator);

        try {
            branchesThread.join();
            attendanceRegistersThread.join();
            workSchedulesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lastAttendanceRegister = utilities.getLastAttendanceRegisterByCollaborator(collaborator);
        if (lastAttendanceRegister != null) {
            lastActionRegistered = lastAttendanceRegister.getAction();
        }

        nextWorkSchedule=utilities.getNextWorkScheduleByLastAttendanceRegister(lastAttendanceRegister, collaborator);
    }

    private void loadComboBoxes() {

        cboBranch.getItems().addAll(model.branches);
        cboAction.getItems().addAll("Entrada", "Salida");
    }

    private void load() {
        refreshVariables();
        loadData();
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
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: entrance. Hour: " + nextWorkSchedule.getStartingLDT().toLocalTime();
            } else {
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: exit. Hour: " + nextWorkSchedule.getEndingLDT().toLocalTime();
            }

            action = "Entrada";
            if (lastAttendanceRegister != null) {
                if (utilities.setMexicanDate(lastAttendanceRegister.getLocalDateTime().toLocalDate()).equals(utilities.setMexicanDate(LocalDate.now()))) {
                    if (lastAttendanceRegister.getAction().equals("Entrada")) action = "Salida";
                }
            }
            status = getStatusText(action);
            branch = nextWorkSchedule.getBranch();
        }

        lblUser.setText(user);
        lblLastRegister.setText(lastRegister);
        lblNextRegister.setText(nextSchedule);
        lblStatus.setText(status);
        cboBranch.getSelectionModel().select(branch);
        cboAction.getSelectionModel().select(action);
        lblDateHour.setText(hour);
    }


    private String getStatusText(String action) {
        String status;

        if (nextWorkSchedule.getId() == null) {
            status = "You don't have to register today";
        } else {
            if (action.equals("Entrada")) {
                LocalDateTime realWorkScheduleLDT = nextWorkSchedule.getStartingLDT();
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
                LocalDateTime realWorkScheduleLDT = nextWorkSchedule.getEndingLDT();
                int minDifference = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), realWorkScheduleLDT);
                if (minDifference <= 0) {
                    status = "You can leave. Good luck";
                } else {
                    status = "You can leave in " + minDifference + " minutes. Good job!";
                }
                lblStatus.setStyle("");
            }
        }
        return status;
    }

    private void setRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            rootPane.getChildren().remove(btnBarManager);
        }
    }

    public void register() {
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        String action = cboAction.getSelectionModel().getSelectedItem();
        LocalDateTime localDateTime = LocalDateTime.now();
        String status = getStatus(action, nextWorkSchedule, localDateTime);
        Integer minutesDelay = getMinDelay(action, nextWorkSchedule, localDateTime);

/*        if(action!=null && nextWorkSchedule!=null){
            if (action.equals("Entrada")) {
                int minDifference = (int) ChronoUnit.MINUTES.between(nextWorkSchedule.getStartingLDT(), LocalDateTime.now());
                if (minDifference <=5) {
                    status = "On Time";
                } else if (minDifference < 16) {
                    status = "Tolerance";
                } else {
                    status = "Late";
                }
                minutesDelay=minDifference;
            } else {
                int minDifference = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), nextWorkSchedule.getEndingLDT());
                if (minDifference <= 0) {
                    status = "Exit on time";
                } else {
                    status = "Exit before time";
                }
            }
        }*/
        AttendanceRegister attendanceRegister = new AttendanceRegister(action, LocalDateTime.now(), status, minutesDelay, collaborator, branch);
        setErrorsAndWarnigs(attendanceRegister, nextWorkSchedule, lastActionRegistered);

        if (model.hasErrors) {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", model.errorList);
        } else {
            if (model.hasWarnings) {
                boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", model.warningList);
                if (!answer) {
                    return;
                }
            }
            attendanceRegisterDAO.createAttendanceRegister(attendanceRegister);
            utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "The attendance register was saved successfully");
            //model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
            load();
        }
    }

    public static String getStatus(String action, WorkSchedule workSchedule, LocalDateTime localDateTime) {
        String status;
        if (action != null && workSchedule != null) {
            if (action.equals("Entrada")) {
                int minDifference = (int) ChronoUnit.MINUTES.between(workSchedule.getStartingLDT(), localDateTime);
                if (minDifference <= 5) {
                    status = "On time";
                } else if (minDifference < 16) {
                    status = "Tolerance";
                } else {
                    status = "Late";
                }
            } else {
                int minDifference = (int) ChronoUnit.MINUTES.between(localDateTime, workSchedule.getEndingLDT());
                if (minDifference <= 0) {
                    status = "Exit on time";
                } else {
                    status = "Exit before time";
                }
            }
        } else {
            status = null;
        }
        return status;
    }

    public static Integer getMinDelay(String action, WorkSchedule workSchedule, LocalDateTime localDateTime) {
        if (action != null && workSchedule != null && action.equals("Entrada")) {
            return (int) ChronoUnit.MINUTES.between(workSchedule.getStartingLDT(), localDateTime);
        } else {
            return null;
        }
    }

    public static void setErrorsAndWarnigs(AttendanceRegister attendanceRegister, WorkSchedule nextWorkSchedule, String lastActionRegistered) {
        model.hasErrors = false;
        model.hasWarnings = false;
        model.errorList = "It couldn't be registered because of the following errors:\n";
        model.warningList = "DO YOU STILL WANT TO SAVE THE REGISTER? \n This register has warnings, you can register it, but We recommend you to create an incident. The warnings found are the following:\n";

        if (attendanceRegister.getAction() == null) {
            model.errorList += "An action must be selected\n";
            model.hasErrors = true;
        } else if (attendanceRegister.getAction().equals(lastActionRegistered)) {
            model.warningList += "Your last registered action was also a " + attendanceRegister.getAction() + "\n";
            model.hasWarnings = true;
        }

        // if the temp attendanceRegister has an id is an update, so the already register check is omitted
        if (attendanceRegister.getId() == null) {
            if (utilities.checkIfAttendanceRegisterExists(attendanceRegister)) {
                model.errorList += "There is already a register of " + attendanceRegister.getAction() + " in the selected date\n";
                model.hasErrors = true;
            }
        }

        if (nextWorkSchedule == null) {
            model.warningList += "You don't have a workschedule in the selected date\n";
            model.hasWarnings = true;
        } else {
            LocalDate mxDate = utilities.getMexicanDate(attendanceRegister.getLocalDateTime());
            if (!nextWorkSchedule.getLocalDate().equals(mxDate)) {
                model.warningList += "You don't have a workschedule for the selected date\n";
                model.hasWarnings = true;
            }
            if (!nextWorkSchedule.getBranch().equals(attendanceRegister.getBranch())) {
                model.warningList += "The branch doesn't correspond to your workschedule. You are supposed to go to: " + nextWorkSchedule.getBranch() + "\n";
                model.hasWarnings = true;
            }
        }
    }

    public void cancel() {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();
    }

    public void changeUser() {
        Model.role originalRole = model.roleView;
        model.openMainAfterLogin = false;
        utilities.loadWindowWithInitData("view/main/Login.fxml", new Stage(), "Login", StageStyle.DECORATED, false, true);
        model.openMainAfterLogin = true;
        load();
        Model.role newRole = model.roleView;
        if (originalRole != newRole) {
            changeRoleView();
        }
    }

    private void changeRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            rootPane.getChildren().remove(btnBarManager);
        } else {
            rootPane.getChildren().add(btnBarManager);
        }
    }

    public void reviewRegisters() {
        utilities.loadWindow("view/attendanceControl/ReviewRegisters.fxml", new Stage(), "Review Registers", StageStyle.DECORATED, true, false);
    }

    public void createAnIncident() {
        model.incidentType = Incident.incidentTypes.ATTENDANCE_REGISTER;
        utilities.loadWindow("view/incident/Incident.fxml", new Stage(), "Create a new incident", StageStyle.DECORATED, false, true);
    }

    public void editRegisters() {
        utilities.loadWindow("view/attendanceControl/ChangeRegisters.fxml", new Stage(), "Review Registers", StageStyle.DECORATED, true, false);
    }

    public void reviewIncidents() {
        utilities.loadWindow("view/incident/ManageIncidents.fxml", new Stage(), "Manage Incidents", StageStyle.DECORATED, true, true);
    }
}
