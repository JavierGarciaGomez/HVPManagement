package com.JGG.HVPManagement.controller.incident;

import com.JGG.HVPManagement.dao.IncidentDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.Incident;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ManageIncidentsController implements Initializable {
    public TableView<Incident> tblTable;
    public TableColumn<Incident, Integer> colId;
    public TableColumn<Incident, Incident.incidentTypes> colType;
    public TableColumn<Incident, Collaborator> colCollaborator;
    public TableColumn<Incident, Branch> colBranch;
    public TableColumn<Incident, String> colDate;
    public TableColumn<Incident, String> colDesc;
    public TableColumn <Incident, Boolean> colSolved;
    public TableColumn<Incident, String> colSolvedDate;
    public DatePicker dtpEnd;
    public DatePicker dtpStart;
    public ComboBox<Collaborator> cboCollaboratorFilter;
    public BorderPane rootPane;
    public Label lblDesc;
    private final Model model=Model.getInstance();
    private final Utilities utilities=Utilities.getInstance();
    public CheckBox chkSolved;
    public HBox bottomPane;
    private Collaborator selectedCollaborator;
    private LocalDate startDate;
    private LocalDate endDate;
    private final IncidentDAO incidentDAO = IncidentDAO.getInstance();
    private final Runnables runnables = Runnables.getInstance();
    private Incident selectedIncident;
    private boolean showSolved;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVariables();
        setDatePickers();
        loadComboBoxes();
        setCellValueFactories();
        loadTable();
        setRoleView();

    }

    private void initVariables() {
        utilities.loadActiveCollaborators();
        selectedCollaborator = null;
        startDate = utilities.getFirstDayOfTheFortNight(LocalDate.now());
        endDate = utilities.getLastDayOfTheFortNight(LocalDate.now());
        showSolved=false;
    }

    private void setDatePickers() {
        dtpStart.setValue(startDate);
        dtpEnd.setValue(endDate);
    }

    private void loadComboBoxes() {
        cboCollaboratorFilter.getItems().addAll(model.activeCollaborators);
    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colType.setCellValueFactory(new PropertyValueFactory<>("incidentType"));
        this.colCollaborator.setCellValueFactory(new PropertyValueFactory<>("collaborator"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colSolved.setCellValueFactory(new PropertyValueFactory<>("solved"));
        this.colSolvedDate.setCellValueFactory(new PropertyValueFactory<>("dateSolvedAsString"));
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
    }

    private void loadTable() {
        List<Incident> incidents;
        if(!showSolved){
            if (selectedCollaborator == null) {
                incidents = incidentDAO.getNotSolvedIncidentsByDate(startDate, endDate);
            } else {
                incidents = incidentDAO.getNotSolvedIncidentsByCollaboratorAndDate(selectedCollaborator, startDate, endDate);
            }
        } else {
            if (selectedCollaborator == null) {
                incidents = incidentDAO.getIncidentsByDate(startDate, endDate);
            } else {
                incidents = incidentDAO.getIncidentsByCollaboratorAndDate(selectedCollaborator, startDate, endDate);
            }
        }
        incidents.sort(Comparator.comparing(Incident::getDateOfOccurrence));
        ObservableList<Incident> incidentObservableList = FXCollections.observableList(incidents);
        this.tblTable.setItems(incidentObservableList);
    }

    private void setRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            bottomPane.setVisible(false);
            cboCollaboratorFilter.getSelectionModel().select(model.loggedUser.getCollaborator());
            selectedCollaborator=model.loggedUser.getCollaborator();
            cboCollaboratorFilter.setDisable(true);
            refreshView();
        }
    }


    /*
     * FILTER METHODS
     * */
    public void setShowSolved() {
        showSolved=chkSolved.isSelected();
        refreshView();
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

    public void refreshView() {
        if (cboCollaboratorFilter.getSelectionModel().getSelectedItem() == null) {
            selectedCollaborator = null;
        } else {
            selectedCollaborator = cboCollaboratorFilter.getSelectionModel().getSelectedItem();
        }
        startDate = dtpStart.getValue();
        endDate = dtpEnd.getValue();
        loadTable();
        this.tblTable.refresh();
    }

    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            selectedIncident=null;
            lblDesc.setText("");
            return;
        }
        selectedIncident = tblTable.getSelectionModel().getSelectedItem();
        lblDesc.setText(selectedIncident.getDescription());
    }

    public void editAttendance() {
        if(selectedIncident==null){
            utilities.showAlert(Alert.AlertType.ERROR, "Error", "You need to select an incident");
        } else{
            model.selectedLocalDate=selectedIncident.getDateOfOccurrence();
            model.selectedCollaborator=selectedIncident.getCollaborator();
            model.selectedBranch=selectedIncident.getBranch();
            utilities.loadModalWindow("view/attendanceControl/ChangeRegisters.fxml", "Review Registers", true, false);
        }
        // todo
    }

    public void editWorkSchedule() {
        if(selectedIncident==null){
            utilities.showAlert(Alert.AlertType.ERROR, "Error", "You need to select an incident");
        } else{
            model.selectedLocalDate=selectedIncident.getDateOfOccurrence();
            model.selectedView= Model.views.COLLABORATOR_VIEW;
            utilities.loadModalWindowWithInitData("view/workSchedule/WorkSchedule.fxml", "Work Schedule",
                    true, true);
        }
    }

    public void markAsSolved() {
        if(selectedIncident!=null){
            incidentDAO.solveIncident(selectedIncident);
        }
        refreshView();

    }

    public void createNewIncident() {
        utilities.loadModalWindow("view/incident/Incident.fxml", "Create a new incident", false, true);
        refreshView();
    }
}
