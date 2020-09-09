package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
    List<String> missingRegisters;

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
        collaborator = model.loggedUser.getCollaborator();
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
        int tardies = 0;
        boolean hasBonus = true;
        int minutesLate;
        for (AttendanceRegister attendanceRegister : attendanceRegisters) {
            if (attendanceRegister.getMinutesLate() != null) {
                minutesLate = attendanceRegister.getMinutesLate();
                if (minutesLate > 6) {
                    hasBonus = false;
                    if (minutesLate > 15 && minutesLate < 31) {
                        tardies += 1;
                    } else if (minutesLate > 31) {
                        tardies += 2;
                    } else {
                        tardies += 3;
                    }
                }
            }
        }
        if (registersMissing) {
            hasBonus = false;
        }
        lblTardies.setText(String.valueOf(tardies));
        if (tardies > 3) {
            lblTardies.setStyle("-fx-background-color: indianred");
        }
        lblBonus.setText(String.valueOf(hasBonus));
        if (hasBonus) {
            lblBonus.setStyle("-fx-background-color: greenyellow");
        } else {
            lblBonus.setStyle("-fx-background-color: indianred");
        }
    }

    private void setRegistersMissing() {
        LocalDate endDate = dtpEnd.getValue();
        endDate = LocalDate.now().isBefore(endDate) ? LocalDate.now() : endDate;
        List<WorkSchedule> workSchedules = utilities.getWorkSchedulesByCollaboratorAndDate(collaborator, dtpStart.getValue(), endDate);
        workSchedules.sort(Comparator.comparing(WorkSchedule::getLocalDate));
        List<AttendanceRegister> attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(collaborator, dtpStart.getValue(), endDate);
        missingRegisters = new ArrayList<>();
        for (WorkSchedule workSchedule : workSchedules) {
            boolean entranceFound = false;
            boolean exitFound = false;
            for (AttendanceRegister attendanceRegister : attendanceRegisters) {
                LocalDate mxDate = utilities.getMexicanDate(attendanceRegister.getLocalDateTime());
                if (workSchedule.getLocalDate().equals(mxDate)) {
                    if (attendanceRegister.getAction().equals("Entrada")) {
                        entranceFound = true;
                    }
                    if (attendanceRegister.getAction().equals("Salida")) {
                        exitFound = true;
                    }
                }
                if (entranceFound && exitFound) {
                    break;
                }
            }
            if (!entranceFound) {
                missingRegisters.add("\nEntrance of " + workSchedule.getCollaborator()+" "+workSchedule.getLocalDate()+" "+workSchedule.getBranch());
            }
            if (!exitFound) {
                missingRegisters.add("\nExit of " + workSchedule.getCollaborator()+" "+workSchedule.getLocalDate()+" "+workSchedule.getBranch());
            }
        }

        int missing = missingRegisters.size();
        lblRegistersMissing.setText(String.valueOf(missing));
        if (missing > 0) {
            registersMissing = true;
            lblRegistersMissing.setStyle("-fx-background-color: #ff0000");
        }

    }

    public void filterByDate() {
        loadTable();
        setRegistersMissing();
        setTardiesAndBonus();

    }

    // todo
    public void showMissingRegisters() {
        utilities.showAlert(Alert.AlertType.INFORMATION, "MISSING REGISTERS", missingRegisters.toString());
    }

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
