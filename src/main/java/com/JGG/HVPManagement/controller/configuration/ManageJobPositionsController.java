package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.JobPositionDAO;
import com.JGG.HVPManagement.dao.WorkingDayTypeDAO;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.WorkingDayType;
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
import java.util.List;
import java.util.ResourceBundle;

public class ManageJobPositionsController implements Initializable {
    public TableView<JobPosition> tblTable;
    public TableColumn<JobPosition, Integer> colId;
    public TableColumn<JobPosition, String> colName;
    public TableColumn<JobPosition, Double> colWage;
    public TableColumn<JobPosition, Double> colYearlyWageBonus;
    public TableColumn<JobPosition, Double> colMinimumIncome;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public TextField txtName;
    public TextField txtWage;
    public TextField txtYearlyWageBonus;
    public TextField txtMinimumPositionIncome;
    public BorderPane rootPane;
    private JobPositionDAO jobPositionDAO;
    private TableView.TableViewSelectionModel<JobPosition> defaultSelectionModel;
    private JobPosition jobPosition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jobPositionDAO = JobPositionDAO.getInstance();
        defaultSelectionModel = tblTable.getSelectionModel();
        setCellValueFactories();
        jobPosition = null;
        loadTable();

        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });

        this.panVboxLeft.getChildren().remove(btnCancelAdd);
    }


    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colWage.setCellValueFactory(new PropertyValueFactory<>("positionWage"));
        this.colYearlyWageBonus.setCellValueFactory(new PropertyValueFactory<>("yearlyPercentageWageBonus"));
        this.colMinimumIncome.setCellValueFactory(new PropertyValueFactory<>("minimumPositionIncome"));
    }

    private void loadTable() {
        List<JobPosition> jobPositions = jobPositionDAO.getJobPositions();
        Model.getInstance().jobPositions = jobPositionDAO.getJobPositions();
        ObservableList<JobPosition> jobPositionObservableList = FXCollections.observableList(jobPositions);
        this.tblTable.setItems(jobPositionObservableList);
    }

    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        JobPosition jobPosition = tblTable.getSelectionModel().getSelectedItem();
        txtName.setText(jobPosition.getName());
        txtWage.setText(String.valueOf(jobPosition.getPositionWage()));
        txtYearlyWageBonus.setText(String.valueOf(jobPosition.getYearlyPercentageWageBonus()));
        txtMinimumPositionIncome.setText(String.valueOf(jobPosition.getMinimumPositionIncome()));
        this.jobPosition = jobPosition;
    }

    public void save() {
        JobPosition jobPosition = new JobPosition();
        if (this.jobPosition != null) {
            jobPosition = this.jobPosition;
        }
        String name = txtName.getText();
        double wage = Double.parseDouble(txtWage.getText());
        double yearlyWageBonus = Double.parseDouble(txtYearlyWageBonus.getText());
        double minimumPositionIncome = Double.parseDouble(txtMinimumPositionIncome.getText());

        jobPosition.setName(name);
        jobPosition.setPositionWage(wage);
        jobPosition.setYearlyPercentageWageBonus(yearlyWageBonus);
        jobPosition.setMinimumPositionIncome(minimumPositionIncome);

        jobPositionDAO.createJobPosition(jobPosition);
        this.loadTable();
        this.tblTable.refresh();
        showAddNewButtons(false);
    }

    public void addNew() {
        jobPosition = null;
        txtName.setText("");
        txtWage.setText("");
        txtYearlyWageBonus.setText("");
        txtMinimumPositionIncome.setText("");
        showAddNewButtons(true);
    }

    private void showAddNewButtons(boolean show) {
        try {
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
        } catch (IllegalArgumentException ignore) {
        }
    }

    public void Delete() {
        String confirmationTxt = "Are you sure that you want to delete the next register? " + jobPosition;
        boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
        if (!answer) return;
        jobPositionDAO.deleteJobPosition(jobPosition);
        this.loadTable();
        this.tblTable.refresh();
    }

    public void cancelAdd() {
        showAddNewButtons(false);
    }
}
