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
    public ComboBox<Collaborator> cboCollaborator;
    public TextField txtHour;
    public DatePicker dtpEnd;
    public DatePicker dtpStart;
    public ComboBox<Collaborator> cboCollaboratorFilter;
    public BorderPane rootPane;
    private Model model;
    private Utilities utilities;
    private TableView.TableViewSelectionModel<AttendanceRegister> defaultSelectionModel;
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
        model.selectedCollaborator = null;
        if(model.selectedLocalDate==null){
            model.selectedLocalDate = LocalDate.now();
            startDate = utilities.getFirstDayOfTheFortNight(model.selectedLocalDate);
            endDate = utilities.getLastDayOfTheFortNight(model.selectedLocalDate);
        } else{
            startDate = model.selectedLocalDate;
            endDate = model.selectedLocalDate;
        }
    }

    private void setDatePickers() {
        dtpStart.setValue(startDate);
        dtpEnd.setValue(endDate);
    }

    private void loadComboBoxes() {
        cboCollaborator.getItems().addAll(model.activeAndWorkerCollaborators);
        cboCollaboratorFilter.getItems().addAll(model.activeAndWorkerCollaborators);
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

                    if (attendanceRegister.getStatus().equals("On time") || attendanceRegister.getStatus().equals("Exit on time")) {
                        setStyle("-fx-background-color: greenyellow");
                    }
                    if (attendanceRegister.getStatus().equals("Late") ||attendanceRegister.getStatus().equals("Exit before time")) {
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
        if (model.selectedCollaborator == null) {
            attendanceRegisters = utilities.getAttendanceRegistersByDates(startDate, endDate);
        } else {
            attendanceRegisters = utilities.getAttendanceRegistersByCollaboratorAndDates(model.selectedCollaborator, startDate, endDate);
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
            model.selectedCollaborator = null;
        } else {
            model.selectedCollaborator = cboCollaboratorFilter.getSelectionModel().getSelectedItem();
        }
        startDate = dtpStart.getValue();
        endDate = dtpEnd.getValue();
        loadTable();
        this.tblTable.refresh();
    }


    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedAttendanceRegister = tblTable.getSelectionModel().getSelectedItem();
        cboCollaborator.getSelectionModel().select(selectedAttendanceRegister.getCollaborator());
        cboAction.getSelectionModel().select(selectedAttendanceRegister.getAction());
        cboBranch.getSelectionModel().select(selectedAttendanceRegister.getBranch());
        dtpDatePicker.setValue(selectedAttendanceRegister.getLocalDateTime().toLocalDate());
        txtHour.setText(model.DTFHHMM.format(selectedAttendanceRegister.getLocalDateTime().toLocalTime()));
    }

    // make similar to register controller
    @FXML
    public void save() {
        Collaborator collaborator = cboCollaborator.getSelectionModel().getSelectedItem();
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        String action = cboAction.getSelectionModel().getSelectedItem();
        LocalDateTime localDateTime = LocalDateTime.of(dtpDatePicker.getValue(), LocalTime.parse(txtHour.getText()));
        LocalDate mxLocalDate = utilities.getMexicanDate(localDateTime);
        WorkSchedule workSchedule = utilities.getWorkScheduleWithHoursByCollaboratorAndDate(collaborator, mxLocalDate);
        String status = RegisterController.getStatus(action, workSchedule, localDateTime);
        Integer minutesDelay = RegisterController.getMinDelay(action, workSchedule, localDateTime);

        AttendanceRegister attendanceRegister = new AttendanceRegister();
        if (selectedAttendanceRegister != null) {
            attendanceRegister = selectedAttendanceRegister;
        }
        attendanceRegister.setCollaborator(collaborator);
        attendanceRegister.setAction(action);
        attendanceRegister.setBranch(branch);
        attendanceRegister.setLocalDateTime(localDateTime);
        attendanceRegister.setMinutesLate(minutesDelay);
        attendanceRegister.setStatus(status);

        RegisterController.setErrorsAndWarnigs(attendanceRegister, workSchedule, null);

        if (model.hasErrors) {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", model.errorList);
        } else{
            if(model.hasWarnings){
                boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", model.warningList);
                if(!answer){
                    return;
                }
            }
            attendanceRegisterDAO.createAttendanceRegister(attendanceRegister);
            utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "The attendance register was saved successfully");
            model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
            refreshView();
            showAddNewButtons(false);
        }




/*
        try {
            WorkSchedule workSchedule = utilities.getWorkScheduleWithHoursByCollaboratorAndDate(collaborator, localDateTime.toLocalDate());
            status = getStatus(action, workSchedule, localDateTime);
            minutesDelay = getMinDelay(action, workSchedule, localDateTime);

            if(workSchedule ==null){
                errorList+= "The collaborator hasn't a work schedule for that day";
                isValid=false;
            }

            boolean registerExists = utilities.checkIfAttendanceRegisterExists(collaborator, action, localDateTime.toLocalDate());
            if (registerExists) {
                errorList += "\nThere is already a register of " + action + " in the " + localDateTime.toLocalDate();
                isValid = false;
            }
        } catch (
                NullPointerException e) {
            isValid = false;
            errorList += "\nYou have to select a collaborator, a branch and an action; and set a valid hour";
        }*/
/*
        if (isValid) {
            AttendanceRegister attendanceRegister = new AttendanceRegister();
            if (selectedAttendanceRegister != null) {
                attendanceRegister = selectedAttendanceRegister;
            }
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
        }*/
    }



    @FXML
    private void addNew() {
        selectedAttendanceRegister=null;
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

                tblTable.setSelectionModel(null);
                this.tblTable.setSelectionModel(null);
                this.panVboxLeft.getChildren().remove(btnAddNew);
                this.panVboxLeft.getChildren().remove(btnDelete);
                this.panVboxLeft.getChildren().add(btnCancelAdd);
            } else {
                tblTable.setSelectionModel(defaultSelectionModel);
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
        String confirmationTxt = "Are you sure that you want to delete this register? " + selectedAttendanceRegister;
        boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
        if (!answer) return;
        attendanceRegisterDAO.deleteAttendanceRegister(selectedAttendanceRegister);
        model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
        refreshView();
    }

    public void cancelAdd() {
        showAddNewButtons(false);
    }


    public void showIncidents() {
    }
}
