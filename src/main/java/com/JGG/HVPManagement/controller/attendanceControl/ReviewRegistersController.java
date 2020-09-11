package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
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
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    private boolean registersMissing;
    private List<String> missingRegisters;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        collaborator = model.loggedUser.getCollaborator();
        setCellValueFactories();
        setFilterDates();
        loadTable();
        setRegistersMissing();
        setTardiesAndBonus();
    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colCollaborator.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.colMinutesLate.setCellValueFactory(new PropertyValueFactory<>("minutesLate"));
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { // if the cell is empty
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
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
        LocalDate endDate = dtpEnd.getValue();
        endDate = LocalDate.now().isBefore(endDate) ? LocalDate.now() : endDate;
        model.tempAttendanceRegisters= utilities.getAttendanceRegistersByCollaboratorAndDates(collaborator, dtpStart.getValue(), endDate);
        model.tempAttendanceRegisters.sort(Comparator.comparing(AttendanceRegister::getLocalDateTime));
        ObservableList<AttendanceRegister> attendanceRegisterObservableList = FXCollections.observableList(model.tempAttendanceRegisters);
        this.tblTable.setItems(attendanceRegisterObservableList);
    }

    private void setTardiesAndBonus() {
        int tardies = 0;
        boolean hasBonus = true;
        int minutesLate;
        for (AttendanceRegister attendanceRegister : model.tempAttendanceRegisters) {
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
        List<WorkSchedule> workSchedules = utilities.getWorkSchedulesByCollaborator(dtpStart.getValue(), endDate, collaborator);
        workSchedules.sort(Comparator.comparing(WorkSchedule::getLocalDate));

        missingRegisters = ChangeRegistersController.getMissingRegisters(workSchedules);

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
        utilities.loadWindow("view/incident/ManageIncidents.fxml", new Stage(), "Manage Incidents", StageStyle.DECORATED, true, true);
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
