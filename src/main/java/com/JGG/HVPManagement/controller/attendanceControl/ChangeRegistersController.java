package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
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
    public Button btnSaveNew;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public ComboBox<String> cboCollaborator;
    public TextField txtHour;
    public DatePicker dtpEnd;
    public DatePicker dtpStart;
    public ComboBox<String> cboCollaboratorFilter;
    public BorderPane rootPane;
    private Collaborator collaborator;
    private Model model;
    private Utilities utilities;
    private TableView.TableViewSelectionModel<AttendanceRegister> defaultSelectionModel;
    private Collaborator selectedCollaborator;
    private List<AttendanceRegister> attendanceRegisters;
    private LocalDate startDate;
    private LocalDate endDate;
    private AttendanceRegister selectedAttendanceRegister;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInstances();
        initVariables();
        setDatePickers();
        loadComboBoxes();
        setCellValueFactories();
        loadTable();
        this.panVboxLeft.getChildren().remove(btnSaveNew);
        this.panVboxLeft.getChildren().remove(btnCancelAdd);
        defaultSelectionModel = tblTable.getSelectionModel();

    }

    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        collaborator = model.loggedUser.getCollaborator();
    }

    private void initVariables() {
        selectedCollaborator = null;
        startDate= utilities.getFirstDayOfTheFortNight(LocalDate.now());
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
        colStatus.setCellFactory(column -> new TableCell<AttendanceRegister, String>() {
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
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
    }

    private void loadTable() {
        if(selectedCollaborator ==null){
            attendanceRegisters = utilities.getAttendanceRegistersByDates(startDate, endDate);
        } else{
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

    public void filter() {
        if(cboCollaboratorFilter.getSelectionModel().getSelectedItem()==null){
            selectedCollaborator =null;
        } else{
            String userName=cboCollaboratorFilter.getSelectionModel().getSelectedItem();
            selectedCollaborator = utilities.getCollaboratorFromUserName(userName);
        }
        startDate=dtpStart.getValue();
        endDate=dtpEnd.getValue();
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
        dtpDatePicker.setValue(selectedAttendanceRegister.getLocalDateTime().toLocalDate());
        txtHour.setText(String.valueOf(selectedAttendanceRegister.getLocalDateTime().toLocalTime()));


    }

    @FXML
    private void save() {
/*        try {
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();

            LocalDateTime localDateTime = localDate.atTime(hour, min);
            AttendanceRegister attendanceRegister = new AttendanceRegister(id, userName, branch, action, localDateTime);
            attendanceRegister.updateTimeRegister();
            this.loadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    private void addNew() {

        setAddNewPane(true);
        //this.txtId.setText(String.valueOf(new AttendanceRegister().getMaxID() + 1));

    }

    private void setAddNewPane(boolean isInsertPane) {
        if (isInsertPane) {
            this.tblTable.setSelectionModel(null);
            this.panVboxLeft.getChildren().remove(btnSave);
            this.panVboxLeft.getChildren().remove(btnAddNew);
            this.panVboxLeft.getChildren().remove(btnDelete);
            this.panVboxLeft.getChildren().add(btnSaveNew);
            this.panVboxLeft.getChildren().add(btnCancelAdd);
        } else {
            this.tblTable.setSelectionModel(defaultSelectionModel);
            this.panVboxLeft.getChildren().add(btnSave);
            this.panVboxLeft.getChildren().add(btnAddNew);
            this.panVboxLeft.getChildren().add(btnDelete);
            this.panVboxLeft.getChildren().remove(btnSaveNew);
            this.panVboxLeft.getChildren().remove(btnCancelAdd);
        }
    }

    @FXML
    private void Delete() {
      /*  try {
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();
            LocalDateTime localDateTime = localDate.atTime(hour, min);
            AttendanceRegister attendanceRegister = new AttendanceRegister(id);

            String confirmationTxt = "¿Estás seguro de querer eliminar el registro siguiente? " +
                    "\nid: "+id+
                    "\nuser: "+userName+
                    "\nbranch: "+branch+
                    "\naction: "+action+
                    "\nfecha y hora: "+localDateTime;

            boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", confirmationTxt);
            if(!answer) return;

            attendanceRegister.deleteTimeRegister();
            this.loadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public void saveNew(ActionEvent event) {
        /*
        try {
            // Fields
            boolean isValid = true;
            String errorList = "No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";
            // Getting the data
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();

            LocalDateTime localDateTime = localDate.atTime(hour, min);
            AttendanceRegister attendanceRegister = new AttendanceRegister(id, userName, branch, action, localDateTime);
            if (attendanceRegister.isDateAndActionRegistered()) {
                errorList += "Ya se cuenta con un registro de " + action + " de " + userName + " con la fecha requerida";
                isValid = false;
            }
            if (isValid) {
                attendanceRegister.createTimeRegister();
                new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Información guardada con éxito");
                setAddNewPane(false);
            } else {
                new Utilities().showAlert(Alert.AlertType.ERROR, "Error de registro", errorList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.loadTable();
        */

    }

    public void cancelAdd(ActionEvent event) {
        setAddNewPane(false);
    }




    public void showIncidences(ActionEvent actionEvent) {
    }
}
