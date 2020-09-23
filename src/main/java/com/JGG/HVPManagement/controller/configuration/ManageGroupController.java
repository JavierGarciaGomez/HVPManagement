package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.AccountGroupDAO;
import com.JGG.HVPManagement.dao.AttendanceRegisterDAO;
import com.JGG.HVPManagement.entity.GroupAccount;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageGroupController implements Initializable {
    public TableView<GroupAccount> tblTable;
    public TableColumn<GroupAccount, Integer> colId;
    public TableColumn<GroupAccount, String> colName;
    public TableColumn<GroupAccount, String> colDesc;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public BorderPane rootPane;
    public Label lblRegistersMissing;
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    private final AccountGroupDAO accountGroupDAO = AccountGroupDAO.getInstance();
    public TextField txtId;
    public TextField txtName;
    public TextField txtDesc;
    private TableView.TableViewSelectionModel<GroupAccount> defaultSelectionModel;
    private GroupAccount selectedGroupAccount;
    private final AttendanceRegisterDAO attendanceRegisterDAO = AttendanceRegisterDAO.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVariables();
        addFieldValidators();
        setCellValueFactories();
        load();
        this.panVboxLeft.getChildren().remove(btnCancelAdd);
        defaultSelectionModel = tblTable.getSelectionModel();
    }

    private void initVariables() {
        // Thread activeCollaboratorsThread = runnables.runActiveCollaborators();
    }

    private void addFieldValidators() {
        utilities.validateNumberAndLength(txtId, 1);
    }

    private void setCellValueFactories() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        tblTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectRegister();
            }
        });
    }

    private void load() {
        loadTable();
        this.tblTable.refresh();
    }

    private void loadTable() {
        utilities.loadAccountGroups();
        this.tblTable.setItems(FXCollections.observableList(model.accountGroups));
    }

    public void refreshView() {
        load();
    }


    @FXML
    private void selectRegister() {
        if (tblTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedGroupAccount = tblTable.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(selectedGroupAccount.getId()));
        txtName.setText(selectedGroupAccount.getName());
        txtDesc.setText(selectedGroupAccount.getDescription());
    }

    @FXML
    public void save() {
        Integer id = Integer.valueOf(txtId.getText());
        String name = txtName.getText();
        String desc = txtDesc.getText();
        GroupAccount groupAccount = new GroupAccount();
        if(selectedGroupAccount != null){
            groupAccount = selectedGroupAccount;
        }
        groupAccount.setId(id);
        groupAccount.setName(name);
        groupAccount.setDescription(desc);

        if(txtId.getText().equals("")||txtName.getText().equals("")){
            model.hasErrors=true;
            model.errorList = "The id and name can't be empty";
        }

        if (model.hasErrors) {
            new Utilities().showAlert(Alert.AlertType.ERROR, "Error", model.errorList);
        } else{
            accountGroupDAO.createOrUpdate(groupAccount);
            utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "The register was saved successfully");
            utilities.loadAccountGroups();
            refreshView();
            showAddNewButtons(false);
        }
    }

    @FXML
    private void addNew() {
        selectedGroupAccount =null;
        showAddNewButtons(true);
        txtId.setText("");
        txtName.setText("");
        txtDesc.setText("");
    }

    private void showAddNewButtons(boolean show) {
        try{
            if (show) {
                tblTable.setSelectionModel(null);
                this.tblTable.setSelectionModel(null);
                this.panVboxLeft.getChildren().remove(btnAddNew);
                this.panVboxLeft.getChildren().remove(btnDelete);
                this.panVboxLeft.getChildren().add(btnCancelAdd);
            } else {
                tblTable.setSelectionModel(defaultSelectionModel);
                this.tblTable.setSelectionModel(defaultSelectionModel);
                this.panVboxLeft.getChildren().add(btnAddNew);
                this.panVboxLeft.getChildren().add(btnDelete);
                this.panVboxLeft.getChildren().remove(btnCancelAdd);
            }
        } catch (IllegalArgumentException ignore){

        }
    }

    @FXML
    public void delete() {
        String confirmationTxt = "Are you sure that you want to delete this register? " + selectedGroupAccount;
        boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", confirmationTxt);
        if (!answer) return;
        accountGroupDAO.delete(selectedGroupAccount);
        model.attendanceRegisters = attendanceRegisterDAO.getAttendanceRegisters();
        refreshView();
    }

    public void cancelAdd() {
        showAddNewButtons(false);
    }

}
