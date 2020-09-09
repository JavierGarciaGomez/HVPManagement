package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MissingReportsController implements Initializable {
    public Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setText(ChangeRegistersController.missingRegisters.toString());
    }
}
