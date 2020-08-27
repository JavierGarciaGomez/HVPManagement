package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.OpeningHoursDAO;
import com.JGG.HVPManagement.dao.WorkingDayTypeDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.OpeningHours;
import com.JGG.HVPManagement.entity.WorkingDayType;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ManageOpeningHoursController implements Initializable {
    public TableView<OpeningHours> tblTable;
    public TableColumn<OpeningHours, Integer> colId;
    public TableColumn<OpeningHours, String> colDesc;
    public TableColumn <OpeningHours, Branch> colBranch;
    public TableColumn <OpeningHours, LocalDate> colDate;
    public TableColumn <OpeningHours, LocalTime> colOpening;
    public TableColumn <OpeningHours, LocalTime> colClosing;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public TextArea txtDescription;
    public TextField txtClosing;
    public ChoiceBox<Branch> cboBranch;
    public DatePicker dtpStartDate;
    public TextField txtOpening;
    private OpeningHoursDAO openingHoursDAO;
    private TableView.TableViewSelectionModel<OpeningHours> defaultSelectionModel;
    private OpeningHours selectedOpeningHours;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openingHoursDAO = OpeningHoursDAO.getInstance();
        defaultSelectionModel = tblTable.getSelectionModel();
        setCellValueFactories();
        selectedOpeningHours=null;
        loadTable();

        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
        this.cboBranch.setItems(FXCollections.observableArrayList(Model.getInstance().branches));

        this.panVboxLeft.getChildren().remove(btnCancelAdd);
    }

    private void setCellValueFactories() {
/*        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colAbbr.setCellValueFactory(new PropertyValueFactory<>("abbr"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colIfBranches.setCellValueFactory(new PropertyValueFactory<>("itNeedBranches"));
        this.colIfHours.setCellValueFactory(new PropertyValueFactory<>("itNeedHours"));*/
    }

    private void loadTable() {
/*        List<WorkingDayType> workingDayTypesList = workingDayTypeDAO.getWorkingDayTypes();
        ObservableList<WorkingDayType> workingDayTypesObservableList = FXCollections.observableList(workingDayTypesList);
        this.tblTable.setItems(workingDayTypesObservableList);*/
    }

    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
/*        WorkingDayType workingDayType = tblTable.getSelectionModel().getSelectedItem();
        txtName.setText(workingDayType.getName());
        txtAbbr.setText(workingDayType.getAbbr());
        txtDescription.setText(workingDayType.getDescription());
        chkHasHours.setSelected(workingDayType.getItNeedHours());
        chkHasBranches.setSelected(workingDayType.getItNeedBranches());
        selectedWorkingDayType = workingDayType;*/
    }

    public void save() {
/*        WorkingDayType workingDayType = new WorkingDayType();
        if (selectedWorkingDayType != null) {
            workingDayType = selectedWorkingDayType;
        }
        String name = txtName.getText();
        String abbr = txtAbbr.getText();
        String desc = txtDescription.getText();
        boolean hasHours = chkHasHours.isSelected();
        boolean hasBranches = chkHasBranches.isSelected();

        workingDayType.setName(name);
        workingDayType.setAbbr(abbr);
        workingDayType.setDescription(desc);
        workingDayType.setItNeedHours(hasHours);
        workingDayType.setItNeedBranches(hasBranches);

        workingDayTypeDAO.createWorkingDayType(workingDayType);
        this.loadTable();*/
    }

    public void addNew() {
/*        selectedWorkingDayType = null;

        txtName.setText("");
        txtAbbr.setText("");
        txtDescription.setText("");
        chkHasHours.setSelected(false);
        chkHasBranches.setSelected(false);

        showAddNewButtons(true);*/
    }

    private void showAddNewButtons(boolean show) {
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
    }

    public void Delete() {
/*        String confirmationTxt = "¿Estás seguro de querer eliminar el registro siguiente? " + selectedWorkingDayType;
        boolean answer = new Utilities().showAlert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de querer continuar?", confirmationTxt);
        if (!answer) return;
        workingDayTypeDAO.deleteWorkingDayType(selectedWorkingDayType);
        this.loadTable();   */ }

    public void cancelAdd() {
        showAddNewButtons(false);
    }
}
