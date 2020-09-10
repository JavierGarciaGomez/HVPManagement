package com.JGG.HVPManagement.controller.schedule;

import com.JGG.HVPManagement.dao.AppointmentDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.model.Utilities;
import com.JGG.HVPManagement.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ResourceBundle;

public class ManageAppointmentController implements Initializable {
    public GridPane rootPane;
    public ComboBox<String> cboVet;
    public ComboBox<String> cboBranch;
    public Button btnDelete;
    public TextField txtPhone;
    public TextField txtTime;
    private CalendarController calendarController;
    public Button btnSave;
    public Button btnCancel;
    public TextField txtClient;
    public TextField txtPet;
    public TextField txtService;
    public DatePicker datePicker;
    public TextArea txtMotive;
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    // todo
    // These fields are for mouse dragging of window


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fill the comboboxex
        ObservableList<String> userNames = new UserDAO().getUsersNames();
        ObservableList<String> branchNames = FXCollections.observableArrayList(model.branchesNamesOld);
        this.cboVet.setItems(userNames);
        this.cboBranch.setItems(branchNames);

        utilities.addChangeListenerToTimeField(txtTime);

        if(model.appointmentToEdit!=null){
            cboVet.getSelectionModel().select(model.appointmentToEdit.getVeterinarian());
            txtPet.setText(model.appointmentToEdit.getPetName());
            txtClient.setText(model.appointmentToEdit.getClientName());
            cboBranch.getSelectionModel().select(model.appointmentToEdit.getBranch());
            txtService.setText(model.appointmentToEdit.getService());
            txtMotive.setText(model.appointmentToEdit.getMotive());
            datePicker.setValue(model.appointmentToEdit.getDate());
            txtTime.setText(String.valueOf(model.appointmentToEdit.getTime()));
        } else{
            datePicker.setValue(model.appointmentDateTime.toLocalDate());
            txtTime.setText(String.valueOf(model.appointmentDateTime.toLocalTime()));
        }
    }

    public void initData(CalendarController calendarController) {
        this.calendarController = calendarController;
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setOnHiding(event -> {
            System.out.println("Window closed");
            model.appointmentToEdit=null;
            model.appontimenTime =null;
            model.appontimenTime =null;
        });

    }

    public void save() {
        String veterinarian = cboVet.getSelectionModel().getSelectedItem();
        String petName = txtPet.getText();
        String clientName = txtClient.getText();
        String branch = cboBranch.getSelectionModel().getSelectedItem();
        String service = txtService.getText();
        String motive = txtMotive.getText();
        String phone = txtPhone.getText();
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.parse(txtTime.getText());

        String errorList = "The appointment couldn't be registered, because of the following errors :\n";
        boolean isValid = true;
        if (branch.equals("")) {
            errorList += "The branch mustn't be empty\n";
            isValid = false;
        }
        if (petName.equals("")) {
            errorList += "The pet name mustn't be empty\n";
            isValid = false;
        }
        if (service.equals("")) {
            errorList += "The service mustn't be empty\n";
            isValid = false;
        }
        if (date == null) {
            errorList += "The date mustn't be empty\n";
            isValid = false;
        }
        if (time == null) {
            errorList += "The time mustn't be empty\n";
            isValid = false;
        }
        boolean hourFound = false;
        for(LocalTime localTime:model.availableHours){
            if(time!=null && time.getHour()==localTime.getHour()){
                hourFound=true;
                break;
            }
        }
        if(!hourFound){
            errorList += "The time isn't between the opening hours\n";
            isValid = false;
        }


        if (isValid) {
            // TODO test 20200810... Before user.addUser();
            Appointment appointment = new Appointment(branch, veterinarian, clientName, phone, petName, service, motive, date, time);
            if(model.appointmentToEdit!=null){
                appointment.setId(model.appointmentToEdit.getId());
                new AppointmentDAO().createAppointment(appointment);
            } else{
                appointment.setId(0);
                new AppointmentDAO().createAppointment(appointment);
            }
            model.appointmentToEdit=null;
            calendarController.updateSchedule();
            exit();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(errorList);
            alert.showAndWait();
        }
    }

    public void delete() {
        if(model.appointmentToEdit==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setHeight(600);
            alert.setContentText("It can't be deleted because the save is not created");
            alert.showAndWait();
        } else{
            String confirmationTxt = "Are you sure that you want to delete this appointment?";
            boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
            if(!answer) return;
            new AppointmentDAO().deleteAppointment(model.appointmentToEdit);
            model.appointmentToEdit=null;
            calendarController.updateSchedule();
            exit();

        }
    }

    public void exit() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}

