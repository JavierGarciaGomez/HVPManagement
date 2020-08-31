package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HoursByDateByBranch;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewRegistersController implements Initializable {
    public TableView<AttendanceRegister> tblTable;
    public TableColumn<AttendanceRegister, Integer> colId;
    public TableColumn<AttendanceRegister, Branch> colBranch;
    public TableColumn<AttendanceRegister, String> colAction;
    public TableColumn<AttendanceRegister, String> colCollaborator;
    public TableColumn<AttendanceRegister, String> colDate;
    public TableColumn<AttendanceRegister, String> colStatus;
    public TableColumn<AttendanceRegister, Integer> colMinutesLate;
    public DatePicker dtpStart;
    public DatePicker dtpEnd;
    private Collaborator collaborator;
    private Model model;
    private Utilities utilities;
    private LocalDate startFilterDate;
    private LocalDate endFilterDate;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInstances();
        setCellValueFactories();
        setFilterDates();
        loadTable();
    }

    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        collaborator=model.loggedUser.getCollaborator();
    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colCollaborator.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.colMinutesLate.setCellValueFactory(new PropertyValueFactory<>("minutesLate"));
    }

    private void setFilterDates() {
         dtpStart.setValue(utilities.getFirstDayOfTheFortNight(LocalDate.now()));
         dtpEnd.setValue(utilities.getLastDayOfTheFortNight(LocalDate.now()));
    }

    public void initData() {

    }

    private void loadTable() {
        List<AttendanceRegister> attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(collaborator, dtpStart.getValue(), dtpEnd.getValue());
        attendanceRegisters.sort(Comparator.comparing(AttendanceRegister::getLocalDateTime));
        ObservableList<AttendanceRegister> attendanceRegisterObservableList = FXCollections.observableList(attendanceRegisters);
        this.tblTable.setItems(attendanceRegisterObservableList);
    }

    public void filterByDate() {
        loadTable();
    }

    // todo
    public void showIncidences() {
    }

    public void setLastFortnight(ActionEvent actionEvent) {
        LocalDate newLocalDate = dtpStart.getValue().minusDays(1);
        dtpStart.setValue(utilities.getFirstDayOfTheFortNight(newLocalDate));
        dtpEnd.setValue(utilities.getLastDayOfTheFortNight(newLocalDate));
    }

    public void setNextFortnight() {
        LocalDate newLocalDate = dtpEnd.getValue().plusDays(1);
        dtpStart.setValue(utilities.getFirstDayOfTheFortNight(newLocalDate));
        dtpEnd.setValue(utilities.getLastDayOfTheFortNight(newLocalDate));
    }
}
