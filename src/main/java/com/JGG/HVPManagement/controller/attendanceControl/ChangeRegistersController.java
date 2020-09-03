package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.dao.AttendanceRegisterDAO;
import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ChangeRegistersController implements Initializable {
    public TableView<AttendanceRegister> tblTable;
    public TableColumn<AttendanceRegister, Integer> colId;
    public TableColumn<AttendanceRegister, Branch> colBranch;
    public TableColumn<AttendanceRegister, String> colAction;
    public TableColumn<AttendanceRegister, String> colCollaborator;
    public TableColumn<AttendanceRegister, String> colDate;
    public TableColumn<AttendanceRegister, String> colStatus;
    public TableColumn<AttendanceRegister, Integer> colMinutesLate;
    public ComboBox<Branch> cboBranch;
    public ComboBox<String> cboAction;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public DatePicker dtpDatePicker;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public ComboBox<String> cboCollaborator;
    public TextField txtHour;
    public DatePicker dtpEnd;
    public DatePicker dtpStart;
    public ComboBox<String> cboCollaboratorFilter;
    public BorderPane rootPane;
    private Model model;
    private Utilities utilities;
    private TableView.TableViewSelectionModel<AttendanceRegister> defaultSelectionModel;
    private Collaborator selectedCollaborator;
    private LocalDate startDate;
    private LocalDate endDate;
    private AttendanceRegister selectedAttendanceRegister;
    private AttendanceRegisterDAO attendanceRegisterDAO;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInstances();
        initVariables();
        setDatePickers();
        loadComboBoxes();
        setCellValueFactories();
        loadTable();
        this.panVboxLeft.getChildren().remove(btnCancelAdd);
        defaultSelectionModel = tblTable.getSelectionModel();
        utilities.addChangeListenerToTimeField(txtHour);

    }

    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        attendanceRegisterDAO = AttendanceRegisterDAO.getInstance();
    }

    private void initVariables() {
        selectedCollaborator = null;
        startDate = utilities.getFirstDayOfTheFortNight(LocalDate.now());
        endDate = utilities.getLastDayOfTheFortNight(LocalDate.now());
    }

    private void setDatePickers() {
        dtpStart.setValue(startDate);
        dtpEnd.setValue(endDate);
    }

    private void loadComboBoxes() {
        cboCollaborator.getItems().addAll(model.activeAndWorkersUserNames);
        cboCollaboratorFilter.getItems().addAll(model.activeAndWorkersUserNames);
        cboBranch.getItems().addAll(model.branches);
        cboAction.getItems().addAll("Entrada", "Salida");
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

                    if (attendanceRegister.getStatus().equals("On time")) {
                        setStyle("-fx-background-color: greenyellow");
                    }
                    if (attendanceRegister.getStatus().equals("Late")) {
                        setStyle("-fx-background-color: yellow");
                    }
                }
            }
        });
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
    }

    private void loadTable() {
        List<AttendanceRegister> attendanceRegisters;
        if (selectedCollaborator == null) {
            attendanceRegisters = utilities.getAttendanceRegistersByDates(startDate, endDate);
        } else {
            attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(selectedCollaborator, startDate, endDate);
        }
        attendanceRegisters.sort(Comparator.comparing(AttendanceRegister::getLocalDateTime));
        ObservableList<AttendanceRegister> attendanceRegisterObservableList = FXCollections.observableList(attendanceRegisters);
        this.tblTable.setItems(attendanceRegisterObservableList);
    }

    /*
     * FILTER METHODS
     * */

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

    public void refreshView() {
        if (cboCollaboratorFilter.getSelectionModel().getSelectedItem() == null) {
            selectedCollaborator = null;
        } else {
            String userName = cboCollaboratorFilter.getSelectionModel().getSelectedItem();
            selectedCollaborator = utilities.getCollaboratorFromUserName(userName);
        }
        startDate = dtpStart.getValue();
        endDate = dtpEnd.getValue();
        loadTable();
    }


    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedAttendanceRegister = tblTable.getSelectionModel().getSelectedItem();
        cboCollaborator.getSelectionModel().select(selectedAttendanceRegister.getCollaborator().getUser().getUserName());
        cboAction.getSelectionModel().select(selectedAttendanceRegister.getAction());
        cboBranch.getSelectionModel().select(selectedAttendanceRegister.getBranch());
        dtpDatePicker.setValue(selectedAttendanceRegister.getLocalDateTime().toLocalDate());
        txtHour.setText(model.DTFHHMM.format(selectedAttendanceRegister.getLocalDateTime().toLocalTime()));
    }

    @FXML
    public void save() {
        AttendanceRegister attendanceRegister = new AttendanceRegister();
        if (selectedAttendanceRegister != null) {
            attendanceRegister = selectedAttendanceRegister;
        }

        boolean isValid = true;
        String errorList = "It couldn't be registered because of the following errors:\n";

        Collaborator collaborator = null;
        Branch branch = null;
        String action = null;
        LocalDateTime localDateTime = null;
        String status = null;
        Integer minutesDelay = null;

        try {
            String userName = cboCollaborator.getSelectionModel().getSelectedItem();
            collaborator = utilities.getCollaboratorFromUserName(userName);
            branch = cboBranch.getSelectionModel().getSelectedItem();
            action = cboAction.getSelectionModel().getSelectedItem();
            localDateTime = LocalDateTime.of(dtpDatePicker.getValue(), LocalTime.parse(txtHour.getText()));
            WorkSchedule workSchedule = utilities.getWorkScheduleWithHoursByCollaboratorAndDate(collaborator, localDateTime.toLocalDate());
            status = getStatus(action, workSchedule, localDateTime);
            minutesDelay = getMinDelay(action, workSchedule, localDateTime);

            if(workSchedule ==null){
                errorList+= "The collaborator hasn't a work schedule for that day";
            }

            boolean registerExists = utilities.checkIfRegisterExists(collaborator, action, LocalDate.now());
            if (registerExists) {
                errorList += "There is already a register of " + action + " in the " + localDateTime.toLocalDate();
                isValid = false;
            }
        } catch (
                NullPointerException e) {
            isValid = false;
            errorList += "You have to select a collaborator, a branch and an action; and set a valid hour";
        }

        if (isValid) {
            attendanceRegister.setCollaborator(collaborator);
            attendanceRegister.setAction(action);
            attendanceRegister.setBranch(branch);
            attendanceRegister.setLocalDateTime(localDateTime);
            attendanceRegister.setMinutesLate(minutesDelay);
            attendanceRegister.setStatus(status);

            attendanceRegisterDAO.createAttendanceRegister(attendanceRegister);
            new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "The attendance register was saved succesfully");
            model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
            refreshView();
        } else {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", errorList);
        }
    }

    private String getStatus(String action, WorkSchedule workSchedule, LocalDateTime localDateTime) {
        String status;
        if (action.equals("Entrada")) {
            LocalDateTime workScheduleStarting = LocalDateTime.of(workSchedule.getLocalDate(), workSchedule.getStartingTime());
            int minDifference = (int) ChronoUnit.MINUTES.between(workScheduleStarting, localDateTime);
            if (minDifference <= 5) {
                status = "On time";
            } else if (minDifference < 16) {
                status = "Tolerance";
            } else {
                status = "Late";
            }
        } else {
            LocalDateTime workScheduleEnding = LocalDateTime.of(workSchedule.getLocalDate(), workSchedule.getEndingTime());
            int minDifference = (int) ChronoUnit.MINUTES.between(localDateTime, workScheduleEnding);
            if (minDifference <= 0) {
                status = "Exit on time";
            } else {
                status = "Exit before time";
            }
        }
        return status;
    }

    private Integer getMinDelay(String action, WorkSchedule workSchedule, LocalDateTime localDateTime) {
        if (action.equals("Entrada")) {
            LocalDateTime workScheduleStarting = LocalDateTime.of(workSchedule.getLocalDate(), workSchedule.getStartingTime());
            return (int) ChronoUnit.MINUTES.between(workScheduleStarting, localDateTime);
        } else {
            return null;
        }
    }


    @FXML
    private void addNew() {

        showAddNewButtons(true);
        cboCollaborator.getSelectionModel().select(null);
        cboBranch.getSelectionModel().select(null);
        cboAction.getSelectionModel().select(null);
        txtHour.setText("");
        dtpDatePicker.setValue(null);
        //this.txtId.setText(String.valueOf(new AttendanceRegister().getMaxID() + 1));

    }

    private void showAddNewButtons(boolean show) {
        try{
            if (show) {
                this.tblTable.setSelectionModel(null);
                this.panVboxLeft.getChildren().remove(btnAddNew);
                this.panVboxLeft.getChildren().remove(btnDelete);
                this.panVboxLeft.getChildren().add(btnCancelAdd);
            } else {
                this.tblTable.setSelectionModel(defaultSelectionModel);
                this.panVboxLeft.getChildren().add(btnAddNew);
                this.panVboxLeft.getChildren().add(btnDelete);
                this.panVboxLeft.getChildren().remove(btnCancelAdd);
            }
        } catch (IllegalArgumentException ignore){

        }
    }

    @FXML
    public void delete() {
        String confirmationTxt = "¿Are you sure that you want to delete this register? " + selectedAttendanceRegister;
        boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
        if (!answer) return;
        attendanceRegisterDAO.deleteAttendanceRegister(selectedAttendanceRegister);
        this.loadTable();
    }

    public void cancelAdd() {
        showAddNewButtons(false);
    }


    public void showIncidences() {
    }
}
