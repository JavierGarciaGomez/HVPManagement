package com.JGG.HVPManagement.controller;

import com.JGG.HVPManagement.entity.TimeRegister;
import com.JGG.HVPManagement.entity.User;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReviewRegistersController implements Initializable {
    public TableView <TimeRegister> tblTable;
    public TableColumn <TimeRegister, Integer> colId;
    public TableColumn <TimeRegister, String> colUserName;
    public TableColumn <TimeRegister, String> colBranch;
    public TableColumn <TimeRegister, String> colAction;
    public TableColumn <TimeRegister, String> colTime;
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
            TimeRegister timeRegister = new TimeRegister(user.getUser(), "", "");

            ObservableList<TimeRegister> timeRegisters = timeRegister.getTimeRegistersforUser();
            this.tblTable.setItems(timeRegisters);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
