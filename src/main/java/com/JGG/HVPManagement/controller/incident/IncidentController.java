package com.JGG.HVPManagement.controller.incident;

import com.JGG.HVPManagement.dao.IncidentDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.Incident;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class IncidentController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public Label lblUser;
    public ComboBox<Branch> cboBranch;
    public GridPane rootPane;
    public ComboBox<Incident.incidentTypes> cboIncidentType;
    public DatePicker dtpDate;
    public TextArea txtDescription;
    private final Utilities utilities = Utilities.getInstance();
    private final Model model = Model.getInstance();
    private final IncidentDAO incidentDAO = IncidentDAO.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        utilities.loadBranches();
        loadComboBoxes();
        loadData();
    }

    private void loadComboBoxes() {
        cboIncidentType.getItems().addAll(Incident.incidentTypes.values());
        cboBranch.getItems().addAll(model.branches);
    }

    private void loadData() {
        cboIncidentType.getSelectionModel().select(model.incidentType);
        lblUser.setText(model.loggedUser.getUserName());
        dtpDate.setValue(LocalDate.now());
    }


    public void register() {
        Incident.incidentTypes incidentType = cboIncidentType.getSelectionModel().getSelectedItem();
        Collaborator collaborator = model.loggedUser.getCollaborator();
        Branch branch = null;
        if (cboBranch.getSelectionModel().getSelectedItem() != null) {
            branch = cboBranch.getSelectionModel().getSelectedItem();
        }
        LocalDate dateOfOccurrence = dtpDate.getValue();
        String desc = txtDescription.getText();
        LocalDateTime registerDate = LocalDateTime.now();

        boolean isValid = true;
        String errorList = "It couldn't be registered because of the following errors:\n";

        if (dateOfOccurrence == null || desc.equals("") || incidentType==null) {
            isValid = false;
            errorList += "The incident type, the date and the description must be registered";
        }

        if (isValid) {
            Incident incident = new Incident();
            incident.setIncidentType(incidentType);
            incident.setCollaborator(collaborator);
            incident.setBranch(branch);
            incident.setDateOfOccurrence(dateOfOccurrence);
            incident.setDescription(desc);
            incident.setRegisterDate(registerDate);
            incident.setSolved(false);
            incidentDAO.createIncident(incident);
            new Utilities().showAlert(Alert.AlertType.INFORMATION, "Success", "It was saved successfully");
            Stage thisStage = (Stage) rootPane.getScene().getWindow();
            thisStage.hide();
        } else {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", errorList);
        }
    }

    public void cancel() {
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        thisStage.close();
    }
}
