package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.WorkScheduleError;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShowErrorsController implements MyInitializable {
    public TableView<WorkScheduleError> tblTable;

    public TableColumn<WorkScheduleError, WorkScheduleError.errorType> colErrorType;
    public TableColumn<WorkScheduleError, LocalDate> colDate;
    public TableColumn<WorkScheduleError, String> colUser;
    public TableColumn<WorkScheduleError, String> colDesc;
    public BorderPane rootPane;
    private ArrayList<WorkScheduleError> errors;

    // todo create an abstract class or an interface for the similar classes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellValueFactories();


    }

    public void initData(ArrayList<WorkScheduleError> errors) {
        this.errors=errors;
        loadTable();
    }

    @Override
    public void initData() {

    }

    private void setCellValueFactories() {
        this.colErrorType.setCellValueFactory(new PropertyValueFactory<>("errorType"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        this.colUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
    }

    private void loadTable() {
        ObservableList<WorkScheduleError> errorObservableList = FXCollections.observableList(errors);
        this.tblTable.setItems(errorObservableList);
    }

    
}
