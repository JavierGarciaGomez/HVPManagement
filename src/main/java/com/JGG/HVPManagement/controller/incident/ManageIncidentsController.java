package com.JGG.HVPManagement.controller.incident;

import com.JGG.HVPManagement.dao.IncidentDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.Incident;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

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
    public TableColumn<Incident, Collaborator> colSolvedBy;
    public TableColumn<Incident, String> colSolvedDate;
    public DatePicker dtpEnd;
    public DatePicker dtpStart;
    public ComboBox<Collaborator> cboCollaboratorFilter;
    public BorderPane rootPane;
    public Label lblDesc;
    private final Model model=Model.getInstance();
    private final Utilities utilities=Utilities.getInstance();
    private TableView.TableViewSelectionModel<Incident> defaultSelectionModel;
    private Collaborator selectedCollaborator;
    private LocalDate startDate;
    private LocalDate endDate;
    private final IncidentDAO incidentDAO = IncidentDAO.getInstance();
    private Incident selectedIncident;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVariables();
        setDatePickers();
        loadComboBoxes();
        setCellValueFactories();
        loadTable();
        defaultSelectionModel = tblTable.getSelectionModel();
    }

    private void initVariables() {
        selectedCollaborator = null;
        startDate = utilities.getFirstDayOfTheFortNight(LocalDate.now());
        endDate = utilities.getLastDayOfTheFortNight(LocalDate.now());
        tblTable.setSelectionModel(defaultSelectionModel);
    }

    private void setDatePickers() {
        dtpStart.setValue(startDate);
        dtpEnd.setValue(endDate);
    }

    private void loadComboBoxes() {
        cboCollaboratorFilter.getItems().addAll(model.activeAndWorkerCollaborators);
    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colType.setCellValueFactory(new PropertyValueFactory<>("incidentType"));
        this.colCollaborator.setCellValueFactory(new PropertyValueFactory<>("collaborator"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colSolvedBy.setCellValueFactory(new PropertyValueFactory<>("solvedBy"));
        this.colSolvedDate.setCellValueFactory(new PropertyValueFactory<>("dateSolvedAsString"));
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
    }

    private void loadTable() {
        List<Incident> incidents;
        if (selectedCollaborator == null) {
            incidents = incidentDAO.getIncidentsByDate(startDate, endDate);
        } else {
            incidents = incidentDAO.getIncidentsByCollaboratorAndDate(selectedCollaborator, startDate, endDate);
        }
        incidents.sort(Comparator.comparing(Incident::getDateOfOccurrence));
        ObservableList<Incident> incidentObservableList = FXCollections.observableList(incidents);
        this.tblTable.setItems(incidentObservableList);
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
            lblDesc.setText(null);
            return;
        }
        selectedIncident = tblTable.getSelectionModel().getSelectedItem();
        lblDesc.setText(selectedIncident.getDescription());
    }

    public void editAttendance() {
        // todo
    }

    public void editWorkSchedule() {
        // todo
    }

    public void markAsSolved() {
        incidentDAO.solveIncident(selectedIncident);
    }
}
