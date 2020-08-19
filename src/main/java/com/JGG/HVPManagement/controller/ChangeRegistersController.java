package com.JGG.HVPManagement.controller;

import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.TimeRegister;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChangeRegistersController implements Initializable {
    public TableView<TimeRegister> tblTable;
    public TableColumn<TimeRegister, Integer> colId;
    public TableColumn<TimeRegister, String> colUserName;
    public TableColumn<TimeRegister, String> colBranch;
    public TableColumn<TimeRegister, String> colAction;
    public TableColumn<TimeRegister, String> colTime;
    public TextField txtId;
    public ComboBox<String> cboUser;
    public ComboBox<String> cboBranch;
    public ComboBox<String> cboAction;
    public Spinner<Integer> spinHour;
    public Spinner<Integer> spinMin;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public DatePicker dtpDatePicker;
    public Button btnSaveNew;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public Label tue11;
    private User user;
    private TableView.TableViewSelectionModel<TimeRegister> defaultSelectionModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
        this.panVboxLeft.getChildren().remove(btnSaveNew);
        this.panVboxLeft.getChildren().remove(btnCancelAdd);

        defaultSelectionModel = tblTable.getSelectionModel();

    }

    public void initData(User user) {
        this.user = user;
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        this.colTime.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        loadTable();
        loadCboUsers();

    }

    private void loadTable() {
        try {
            TimeRegister timeRegister = new TimeRegister(user.getUserName(), "", "");
            ObservableList<TimeRegister> timeRegisters = timeRegister.getTimeRegistersObservableList();
            this.tblTable.setItems(timeRegisters);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadCboUsers() {
        ObservableList<String> userNames = UserDAO.getInstance().getUsersNames();
        System.out.println(userNames);
        this.cboUser.setItems(userNames);
    }

    @FXML
    private void selectRegister() {
        try {
            int index = tblTable.getSelectionModel().getSelectedIndex();

            if (index <= -1) {
                return;
            }
            txtId.setText(colId.getCellData(index).toString());
            cboUser.getSelectionModel().select(colUserName.getCellData(index));
            cboBranch.getSelectionModel().select(colBranch.getCellData(index));
            cboAction.getSelectionModel().select(colAction.getCellData(index));

            // Getting the date and time
            String mystring = colTime.getCellData(index);
            String[] arr = mystring.split(" ", 2);
            String date = arr[0];   //the
            String time = arr[1];     //quick brown fox

            // Date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);

            String[] hourMinutes = time.split(":");
            int hour = Integer.parseInt(hourMinutes[0].trim());
            int min = Integer.parseInt(hourMinutes[1].trim());
            //
            dtpDatePicker.setValue(localDate);
            spinHour.getValueFactory().setValue(hour);
            spinMin.getValueFactory().setValue(min);

        } catch (NullPointerException ignore) {

        }
    }

    @FXML
    private void save() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();

            LocalDateTime localDateTime = localDate.atTime(hour, min);
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            timeRegister.updateTimeRegister();
            this.loadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNew() {
        try {
            setAddNewPane(true);
            this.txtId.setText(String.valueOf(new TimeRegister().getMaxID() + 1));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        try {
            int id = Integer.parseInt(txtId.getText());
            String userName = cboUser.getSelectionModel().getSelectedItem();
            String branch = cboBranch.getSelectionModel().getSelectedItem();
            String action = cboAction.getSelectionModel().getSelectedItem();
            LocalDate localDate = dtpDatePicker.getValue();
            int hour = spinHour.getValue();
            int min = spinMin.getValue();
            LocalDateTime localDateTime = localDate.atTime(hour, min);
            TimeRegister timeRegister = new TimeRegister(id);

            String confirmationTxt = "¿Estás seguro de querer eliminar el registro siguiente? " +
                    "\nid: "+id+
                    "\nuser: "+userName+
                    "\nbranch: "+branch+
                    "\naction: "+action+
                    "\nfecha y hora: "+localDateTime;

            boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", confirmationTxt);
            if(!answer) return;

            timeRegister.deleteTimeRegister();
            this.loadTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNew(ActionEvent event) {
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
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            if (timeRegister.isDateAndActionRegistered()) {
                errorList += "Ya se cuenta con un registro de " + action + " de " + userName + " con la fecha requerida";
                isValid = false;
            }
            if (isValid) {
                timeRegister.createTimeRegister();
                new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "Información guardada con éxito");
                setAddNewPane(false);
            } else {
                new Utilities().showAlert(Alert.AlertType.ERROR, "Error de registro", errorList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.loadTable();
    }

    public void cancelAdd(ActionEvent event) {
        setAddNewPane(false);
    }
}
