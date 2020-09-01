package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.OpeningHoursDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.OpeningHours;
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
    public TableColumn<OpeningHours, Branch> colBranch;
    public TableColumn<OpeningHours, LocalDate> colDate;
    public TableColumn<OpeningHours, LocalTime> colOpening;
    public TableColumn<OpeningHours, LocalTime> colClosing;
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
    private Utilities utilities;

    // todo create an abstract class or an interface for the similar classes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        utilities = Utilities.getInstance();
        openingHoursDAO = OpeningHoursDAO.getInstance();
        defaultSelectionModel = tblTable.getSelectionModel();
        setCellValueFactories();
        selectedOpeningHours = null;
        loadTable();

        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
        this.cboBranch.setItems(FXCollections.observableArrayList(Model.getInstance().branches));

        this.panVboxLeft.getChildren().remove(btnCancelAdd);
        utilities.addChangeListenerToTimeField(txtOpening);
        utilities.addChangeListenerToTimeField(txtClosing);

    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.colOpening.setCellValueFactory(new PropertyValueFactory<>("openingHour"));
        this.colClosing.setCellValueFactory(new PropertyValueFactory<>("closingHour"));
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadTable() {
        List<OpeningHours> openingHoursList = openingHoursDAO.getOpeningHoursList();
        ObservableList<OpeningHours> openingHoursObservableList = FXCollections.observableList(openingHoursList);
        this.tblTable.setItems(openingHoursObservableList);
    }

    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        OpeningHours openingHours = tblTable.getSelectionModel().getSelectedItem();
        cboBranch.getSelectionModel().select(openingHours.getBranch());
        dtpStartDate.setValue(openingHours.getStartDate());
        txtOpening.setText(openingHours.getOpeningHour().toString());
        txtClosing.setText(openingHours.getClosingHour().toString());
        txtDescription.setText(openingHours.getDescription());

        selectedOpeningHours = openingHours;
    }

    public void save() {
        OpeningHours openingHours = new OpeningHours();
        if (selectedOpeningHours != null) {
            openingHours = selectedOpeningHours;
        }

        openingHours.setBranch(cboBranch.getSelectionModel().getSelectedItem());
        openingHours.setStartDate(dtpStartDate.getValue());
        if (txtOpening.getText().equals("")) {
            openingHours.setOpeningHour(null);
        } else {
            openingHours.setOpeningHour(LocalTime.parse(txtOpening.getText()));
        }
        if (txtClosing.getText().equals("")) {
            openingHours.setClosingHour(null);
        } else {
            openingHours.setClosingHour(LocalTime.parse(txtClosing.getText()));
        }

        openingHours.setDescription(txtDescription.getText());

        openingHoursDAO.createOpeningHours(openingHours);
        this.loadTable();
        showAddNewButtons(false);
    }

    public void addNew() {
        selectedOpeningHours = null;
        cboBranch.getSelectionModel().clearSelection();
        dtpStartDate.setValue(null);
        txtOpening.setText("");
        txtClosing.setText("");
        txtDescription.setText("");

        showAddNewButtons(true);
    }

    private void showAddNewButtons(boolean show) {
        try{
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
        } catch (IllegalArgumentException ignore){
        }
    }

    public void delete() {
        String confirmationTxt = "¿Are you sure that you want to delete this register? " + selectedOpeningHours;
        boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
        if (!answer) return;
        openingHoursDAO.deleteOpeningHours(selectedOpeningHours);
        this.loadTable();
    }

    public void cancelAdd() {
        showAddNewButtons(false);
    }
}
