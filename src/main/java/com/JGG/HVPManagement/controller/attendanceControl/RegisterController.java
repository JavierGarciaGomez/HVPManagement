package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private LocalDateTime now;
    private Model model;
    private Utilities utilities;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        loadComboBoxes();
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

    private void loadData() {
        Collaborator collaborator = model.loggedUser.getCollaborator();
        lblUser.setText(model.loggedUser.getUserName());
        AttendanceRegister lastAttendanceRegister = utilities.getLastAttendanceRegisterByCollaborator(collaborator);
        if (lastAttendanceRegister != null) {
            lblLastRegister.setText(String.valueOf(lastAttendanceRegister));
        } else {
            lblLastRegister.setText("The collaborator has no previous registers");
            lastAttendanceRegister = new AttendanceRegister();
            lastAttendanceRegister.setCollaborator(collaborator);
        }
        WorkSchedule nextWorkSchedule = utilities.getWorkScheduleByLastAttendanceRegister(lastAttendanceRegister);
        if(nextWorkSchedule==null){
            lblLastRegister.setText("No previous registers");
            lblNextRegister.setText("No next schedules");
            lblStatus.setText("The status can't be retrieved");
            lblDateHour.setText("No hour to register");
        } else{

            String nextSchedule = null;
            if (lastAttendanceRegister.getAction() == null) {
                nextSchedule = "Date: "+nextWorkSchedule.getLocalDate()+". Branch: " + nextWorkSchedule.getBranch() + ". Action: entrance. Hour: " + nextWorkSchedule.getStartingTime();
            } else {
                if (lastAttendanceRegister.getAction().equals("Entrada")) {
                    nextSchedule = "Date: "+nextWorkSchedule.getLocalDate()+". Branch: " + nextWorkSchedule.getBranch() + ". Action: end. Hour: " + nextWorkSchedule.getEndingTime();
                } else {
                    nextSchedule = "Date: "+nextWorkSchedule.getLocalDate()+". Branch: " + nextWorkSchedule.getBranch() + ". Action: entrance. Hour: " + nextWorkSchedule.getStartingTime();
                }
            }
            lblNextRegister.setText(nextSchedule);

            String lblStatusText = null;
            String actionToRegister = null;
            // if the nextWorkSchedule is not today

            WorkSchedule realWorkSchedule;
            if (nextWorkSchedule.getLocalDate().isBefore(LocalDate.now())) {
                realWorkSchedule = utilities.getWorkScheduleByCollaboratorAndDate(collaborator, LocalDate.now());
            } else {
                realWorkSchedule = nextWorkSchedule;
            }
            if (realWorkSchedule == null) {
                lblStatusText = "You don't have to register today";
            } else {

                lastActionRegistered = lastAttendanceRegister.getAction();
                if (lastActionRegistered == null) {
                    actionToRegister = "Entrada";
                } else {
                    actionToRegister = "Salida";
                }
                if (actionToRegister.equals("Entrada")) {
                    int minDifference = (int) ChronoUnit.MINUTES.between(realWorkSchedule.getStartingTime(), LocalTime.now());
                    if (minDifference < 5) {
                        lblStatusText = "You are on time";
                    } else if (minDifference < 16) {
                        lblStatusText = "You are " + minDifference + " minutes late, but in the tolerance time";
                    } else if (minDifference < 31) {
                        lblStatusText = "You are " + minDifference + " minutes late: 1 tardy";
                    } else if (minDifference < 120) {
                        lblStatusText = "You are " + minDifference + " minutes late: 2 tardies";
                    } else {
                        lblStatusText = "You are " + minDifference + " minutes late: 3 tardies";
                    }
                } else {
                    int minDifference = (int) ChronoUnit.MINUTES.between(LocalTime.now(), realWorkSchedule.getEndingTime());
                    if (minDifference <= 0) {
                        lblStatusText = "You can leave. Good luck";
                    } else {
                        lblStatusText = "You can leave in " + minDifference + ". Good work!";
                    }
                }
            }
            lblStatus.setText(lblStatusText);
            if(realWorkSchedule!=null){
                cboBranch.getSelectionModel().select(realWorkSchedule.getBranch());
            }
            if(realWorkSchedule!=null){
                cboAction.getSelectionModel().select(actionToRegister);
            }
            lblDateHour.setText(String.valueOf(LocalDateTime.now()));

        }
    }





/*    public void initData(User user) {
        lblUser.setText(model.loggedUser.getUserName());

        try {
            AttendanceRegister lastAttendanceRegister = new AttendanceRegister().getLastTimeRegister(user.getUserName());
            lblLastRegister.setText("Último registro: " + lastAttendanceRegister.toString());
            //cboBranch.getSelectionModel().select(lastAttendanceRegister.getBranch());
            String action = lastAttendanceRegister.getAction();
            action = action.equalsIgnoreCase("Entrada") ? "Salida" : "Entrada";
            cboAction.getSelectionModel().select(action);
            lastActionRegistered = lastAttendanceRegister.getAction();
        } catch (SQLException | NullPointerException throwables) {
            lblLastRegister.setText("Último registro: " + "No se tienen registros previos");
        }


    }*/

    public void register() {
        try {
            // Fields
            boolean isValid = true;
            String errorList = "No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";
            // Getting the data
            String user = lblUser.getText();
            //String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();

            AttendanceRegister attendanceRegister = new AttendanceRegister(-1, user, null, action, now);
            if (attendanceRegister.isDateAndActionRegistered()) {
                errorList += "Ya se cuenta con un registro de " + action + " de " + user + " con fecha de hoy";
                isValid = false;
            }
            // Check if the action is correct
            if (action.equals(lastActionRegistered)) {
                String error = "Tú última acción registrada fue también una " + action + ". ¿Estás seguro que quieres " +
                        "registrarlo?";
                boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", error);
                if (!answer) return;
            } else {
                System.out.println("No coincide la fecha y acción");
            }

            if (isValid) {
                attendanceRegister.createTimeRegister();
                new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Información guardada con éxito");
                Stage thisStage = (Stage) btnCancel.getScene().getWindow();
                thisStage.close();

            } else {
                new Utilities().showAlert(Alert.AlertType.ERROR, "Error de registro", errorList);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void cancel() {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();

    }

    public void changeUser(ActionEvent actionEvent) {
    }

    public void reviewRegisters(ActionEvent actionEvent) {
    }

    public void createAnIncidence(ActionEvent actionEvent) {
    }

    public void editRegisters(ActionEvent actionEvent) {
    }

    public void reviewIncidences(ActionEvent actionEvent) {
    }
}
