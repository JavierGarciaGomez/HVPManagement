package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.TimeRegister;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public Label lblUser;
    public ComboBox<String> cboBranch;
    public ComboBox<String> cboAction;
    public Label lblDateHour;
    public Label lblLastRegister;
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private String lastActionRegistered;
    private LocalDateTime now;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cboBranch.getSelectionModel().select(0);
        cboAction.getSelectionModel().select(0);
        now = LocalDateTime.now();
        System.out.println(now);
        lblDateHour.setText(DTF.format(now));
    }

    public void initData(User user) {
        lblUser.setText(user.getUserName());

        try {
            TimeRegister lastTimeRegister = new TimeRegister().getLastTimeRegister(user.getUserName());
            lblLastRegister.setText("Último registro: " + lastTimeRegister.toString());
            cboBranch.getSelectionModel().select(lastTimeRegister.getBranch());
            String action = lastTimeRegister.getAction();
            action = action.equalsIgnoreCase("Entrada") ? "Salida" : "Entrada";
            cboAction.getSelectionModel().select(action);
            lastActionRegistered=lastTimeRegister.getAction();
        } catch (SQLException throwables) {
            lblLastRegister.setText("Último registro: " + "No se tienen registros previos");
        }


    }

    public void register() {
        try {
            // Fields
            boolean isValid=true;
            String errorList ="No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";
            // Getting the data
            String user = lblUser.getText();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();

            TimeRegister timeRegister = new TimeRegister(-1, user, branch, action, now);
            if(timeRegister.isDateAndActionRegistered()){
                errorList+="Ya se cuenta con un registro de "+action+" de "+user+" con fecha de hoy";
                isValid=false;
            }
            // Check if the action is correct
            if(action.equals(lastActionRegistered)){
                String error = "Tú última acción registrada fue también una "+action+". ¿Estás seguro que quieres " +
                        "registrarlo?";
                boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", error);
                if(!answer) return;
            } else{
                System.out.println("No coincide la fecha y acción");
            }

            if(isValid){
                timeRegister.createTimeRegister();
                new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Información guardada con éxito");
                Stage thisStage = (Stage) btnCancel.getScene().getWindow();
                thisStage.close();

            }            else{
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
}
