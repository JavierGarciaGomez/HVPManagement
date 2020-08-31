package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.dao.AttendanceRegisterDAO;
import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public Label lblNextRegister;
    public Label lblStatus;
    public GridPane rootPane;
    private String lastActionRegistered;
    private Model model;
    private Utilities utilities;
    private Collaborator collaborator;
    private AttendanceRegister lastAttendanceRegister;
    private WorkSchedule nextWorkSchedule;
    private WorkSchedule realWorkSchedule;
    private AttendanceRegisterDAO attendanceRegisterDAO;
    private int tardies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        attendanceRegisterDAO = AttendanceRegisterDAO.getInstance();
        loadComboBoxes();
        initVariables();
        loadData();


        //cboBranch.getSelectionModel().select(0);
        //cboAction.getSelectionModel().select(0);
        //now = LocalDateTime.now();

        //lblDateHour.setText(DTF.format(now));
    }

    private void loadComboBoxes() {
        cboBranch.getItems().addAll(model.branches);
        cboAction.getItems().addAll("Entrada", "Salida");
    }

    private void initVariables() {
        collaborator = model.loggedUser.getCollaborator();
        lastAttendanceRegister = utilities.getLastAttendanceRegisterByCollaborator(collaborator);
        if (lastAttendanceRegister != null) {
            lastActionRegistered = lastAttendanceRegister.getAction();
        }
        nextWorkSchedule = utilities.getWorkScheduleByLastAttendanceRegister(lastAttendanceRegister, collaborator);
        realWorkSchedule = nextWorkSchedule;
        if (nextWorkSchedule != null && nextWorkSchedule.getLocalDate().isBefore(LocalDate.now())) {
            realWorkSchedule = utilities.getWorkScheduleByCollaboratorAndDate(collaborator, LocalDate.now());
        }
    }

    private void loadData() {
        String user = model.loggedUser.getUserName();
        String lastRegister = "The collaborator has no previous registers";
        String nextSchedule = "The collaborator has no pending work schedules";
        String status = "There is no status";
        Branch branch = null;
        String action = null;
        String hour = DTF.format(LocalDateTime.now());

        if (lastAttendanceRegister != null) {
            lastRegister = String.valueOf(lastAttendanceRegister);
        }

        if (nextWorkSchedule != null) {
            if (lastActionRegistered == null || Objects.equals(lastActionRegistered, "Salida")) {
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: entrance. Hour: " + nextWorkSchedule.getStartingTime();
            } else {
                nextSchedule = "Date: " + nextWorkSchedule.getLocalDate() + ". Branch: " + nextWorkSchedule.getBranch() + ". Action: end. Hour: " + nextWorkSchedule.getEndingTime();
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

    private String getStatusText(String action) {
        String status;
        if (realWorkSchedule == null) {
            status = "You don't have to register today";
        } else {
            if (action.equals("Entrada")) {
                int minDifference = (int) ChronoUnit.MINUTES.between(realWorkSchedule.getStartingTime(), LocalTime.now());
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
                int minDifference = (int) ChronoUnit.MINUTES.between(LocalTime.now(), realWorkSchedule.getEndingTime());
                if (minDifference <= 0) {
                    status = "You can leave. Good luck";
                } else {
                    status = "You can leave in " + minDifference + ". Good work!";
                }
            }
        }
        return status;
    }

    public void register() {
        boolean isValid = true;
        String errorList = "It couldn't be registered because of the following errors:\n";
        String action = cboAction.getSelectionModel().getSelectedItem();
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        if(lastAttendanceRegister!=null && lastAttendanceRegister.getLocalDateTime().toLocalDate().equals(LocalDate.now())
                && lastAttendanceRegister.getAction().equals(action)){
            errorList += "There is already a register of " + action + " today";
            isValid = false;
        }

        if (action.equals(lastActionRegistered)) {
            String error = "Your last registered action was also a " + action + ". Are you sure that You want to register it?";
            boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", error);
            if (!answer) return;
        }

        if (isValid) {
            AttendanceRegister attendanceRegister = new AttendanceRegister(action, LocalDateTime.now(), collaborator, branch);
            attendanceRegisterDAO.createAttendanceRegister(attendanceRegister);
            new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "The attendance register was saved succesfully");
            model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
            initVariables();
            loadData();
        } else {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", errorList);
        }
    }

    public void cancel() {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();

    }

    public void changeUser() {
    }

    public void reviewRegisters() {
    }

    public void createAnIncidence() {
    }

    public void editRegisters() {
    }

    public void reviewIncidences() {
    }
}
