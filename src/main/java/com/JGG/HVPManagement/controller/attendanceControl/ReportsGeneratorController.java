package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.User;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsGeneratorController implements Initializable {
    public TableView <AttendanceRegister> tblTable;
    public TableColumn<AttendanceRegister, Integer> colId;
    public TableColumn <AttendanceRegister, String> colUserName;
    public TableColumn <AttendanceRegister, String> colBranch;
    public TableColumn <AttendanceRegister, String> colAction;
    public TableColumn <AttendanceRegister, String> colTime;
    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(User user) {
        this.user = user;
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));

        loadTable();
    }

    private void loadTable() {
        try {
            AttendanceRegister attendanceRegister = new AttendanceRegister(user.getUserName(), "", "");

            ObservableList<AttendanceRegister> attendanceRegisters = attendanceRegister.getTimeRegistersObservableList();
            this.tblTable.setItems(attendanceRegisters);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
