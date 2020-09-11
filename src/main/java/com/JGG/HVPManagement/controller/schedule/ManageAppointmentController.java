package com.JGG.HVPManagement.controller.schedule;

import com.JGG.HVPManagement.dao.AppointmentDAO;
import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.Utilities;
import com.JGG.HVPManagement.model.Model;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ResourceBundle;

public class ManageAppointmentController implements MyInitializable {
    public GridPane rootPane;
    public ComboBox<Collaborator> cboVet;
    public ComboBox<Branch> cboBranch;
    public Button btnDelete;
    public TextField txtPhone;
    public TextField txtTime;
    public Button btnSave;
    public Button btnCancel;
    public TextField txtClient;
    public TextField txtPet;
    public TextField txtService;
    public DatePicker datePicker;
    public TextArea txtMotive;
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    private final AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();
    // todo
    // These fields are for mouse dragging of window


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fill the comboboxex
        this.cboVet.setItems(FXCollections.observableList(model.activeAndWorkerCollaborators));
        this.cboBranch.setItems(FXCollections.observableList(model.branches));

        utilities.addChangeListenerToTimeField(txtTime);

        if(model.appointmentToEdit!=null){
            cboVet.getSelectionModel().select(model.appointmentToEdit.getCollaborator());
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

    @Override
    public void initData() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setOnHiding(event -> {
            System.out.println("Window closed");
            model.appointmentToEdit=null;
        });
    }

    public void save() {
        Collaborator collaborator = cboVet.getSelectionModel().getSelectedItem();
        String petName = txtPet.getText();
        String clientName = txtClient.getText();
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        String service = txtService.getText();
        String motive = txtMotive.getText();
        String phone = txtPhone.getText();
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.parse(txtTime.getText());

        String errorList = "The appointment couldn't be registered, because of the following errors :\n";
        boolean isValid = true;
        boolean hasWarnings = false;

        if (cboBranch.getSelectionModel().getSelectedItem()==null) {
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
        if(date!=null && date.isBefore(LocalDate.now())){
            boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "CONFIRMATION", "You are trying to register an appointment in a date before today, are you sure?");
            if(!answer){
                return;
            }
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
            Appointment appointment = new Appointment();
            appointment.setBranch(branch);
            appointment.setCollaborator(collaborator);
            appointment.setClientName(clientName);
            appointment.setDate(date);
            appointment.setTime(time);
            appointment.setMotive(motive);
            appointment.setPetName(petName);
            appointment.setPhone(phone);
            appointment.setService(service);
            appointment.setRegisteredBy(model.loggedUser.getCollaborator());

            if(model.appointmentToEdit!=null){
                appointment.setId(model.appointmentToEdit.getId());
            } else{
                appointment.setId(null);
            }
            appointmentDAO.createAppointment(appointment);
            model.appointmentToEdit=null;
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
            appointmentDAO.deleteAppointment(model.appointmentToEdit);
            model.appointmentToEdit=null;
            exit();

        }
    }

    public void exit() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}

