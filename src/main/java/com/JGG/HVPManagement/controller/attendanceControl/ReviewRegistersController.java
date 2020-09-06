package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.model.HoursByDateByBranch;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import com.JGG.HVPManagement.model.WorkScheduleError;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public Label lblTardies;
    public Label lblBonus;
    public Label lblRegistersMissing;
    private Collaborator collaborator;
    private Model model;
    private Utilities utilities;
    private List<AttendanceRegister> attendanceRegisters;
    private boolean registersMissing;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInstances();
        setCellValueFactories();
        setFilterDates();
        loadTable();
        setRegistersMissing();
        setTardiesAndBonus();
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
        colStatus.setCellFactory(column -> new TableCell<AttendanceRegister, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { // if the cell is empty
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    AttendanceRegister attendanceRegister = getTableView().getItems().get(getIndex());

                    if (attendanceRegister.getStatus().equals("Late")) {
                        setStyle("-fx-background-color: yellow");
                    }
                }
            }
        });
    }

    private void setFilterDates() {
        dtpStart.setValue(utilities.getFirstDayOfTheFortNight(LocalDate.now()));
        dtpEnd.setValue(utilities.getLastDayOfTheFortNight(LocalDate.now()));
    }

    private void loadTable() {
        attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(collaborator, dtpStart.getValue(), dtpEnd.getValue());
        attendanceRegisters.sort(Comparator.comparing(AttendanceRegister::getLocalDateTime));
        ObservableList<AttendanceRegister> attendanceRegisterObservableList = FXCollections.observableList(attendanceRegisters);
        this.tblTable.setItems(attendanceRegisterObservableList);
    }

    private void setTardiesAndBonus() {
        int tardies=0;
        boolean hasBonus=true;
        int minutesLate;
        for(AttendanceRegister attendanceRegister:attendanceRegisters){
            if(attendanceRegister.getMinutesLate()!=null){
                minutesLate = attendanceRegister.getMinutesLate();
                if(minutesLate>6){
                    hasBonus=false;
                    if(minutesLate>15 && minutesLate<31){
                        tardies+=1;
                    } else if(minutesLate>31){
                        tardies+=2;
                    } else{
                        tardies+=3;
                    }
                }
            }
        }
        if(!registersMissing){
            hasBonus=false;
        }
        lblTardies.setText(String.valueOf(tardies));
        if(tardies>3){
            lblTardies.setStyle("-fx-background-color: indianred");
        }
        lblBonus.setText(String.valueOf(hasBonus));
        if(hasBonus){
            lblBonus.setStyle("-fx-background-color: greenyellow");
        } else{
            lblBonus.setStyle("-fx-background-color: indianred");
        }
    }

    private void setRegistersMissing() {
        LocalDate endDate = dtpEnd.getValue().isBefore(LocalDate.now().minusDays(1))?dtpEnd.getValue():LocalDate.now().minusDays(1);
        List<WorkSchedule> workSchedules = utilities.getWorkSchedulesByCollaboratorAndDate(collaborator, dtpStart.getValue(), endDate);
        List<AttendanceRegister> attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(collaborator, dtpStart.getValue(), endDate);
        int numWorkSchedules = workSchedules.size();
        int numAttendanceRegisters = attendanceRegisters.size();
        int missing = (numWorkSchedules*2)-numAttendanceRegisters;
        lblRegistersMissing.setText(String.valueOf(missing));
        if(missing>0){
            registersMissing=true;
            lblRegistersMissing.setStyle("-fx-background-color: #ff0000");
        }
    }

    public void filterByDate() {
        loadTable();
        setRegistersMissing();
        setTardiesAndBonus();

    }

    // todo
    public void showIncidents() {
    }

    public void setLastFortnight() {
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
