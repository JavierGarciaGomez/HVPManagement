package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.WorkingDayTypeDAO;
import com.JGG.HVPManagement.entity.WorkingDayType;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageWorkingDayTypeController implements MyInitializable {
    public TableView<WorkingDayType> tblTable;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAbbr;
    public TableColumn colIfHours;
    public TableColumn colIfBranches;
    public TableColumn colDesc;
    public Button btnSave;
    public Button btnAddNew;
    public Button btnDelete;
    public Button btnSaveNew;
    public Button btnCancelAdd;
    public VBox panVboxLeft;
    public TextField txtName;
    public TextField txtAbbr;
    public CheckBox chkHasHours;
    public CheckBox chkHasBranches;
    public TextArea txtDescription;
    private Utilities utilities;
    private Model model;
    private WorkingDayTypeDAO workingDayTypeDAO;

    @Override
    public void initData() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        utilities = Utilities.getInstance();
        model = Model.getInstance();
        workingDayTypeDAO = WorkingDayTypeDAO.getInstance();

    }

    public void selectRegister(MouseEvent mouseEvent) {
    }

    public void save() {
        String name = txtName.getText();
        String abbr = txtAbbr.getText();
        String desc = txtDescription.getText();
        boolean hasHours = chkHasHours.isSelected();
        boolean hasBranches = chkHasBranches.isSelected();
        WorkingDayType workingDayType = new WorkingDayType();
        workingDayType.setName(name);
        workingDayType.setAbbr(abbr);
        workingDayType.setDescription(desc);
        workingDayType.setItNeedHours(hasHours);
        workingDayType.setItNeedBranches(hasBranches);

        workingDayTypeDAO.createWorkingDayType(workingDayType);

        //this.loadTable();

    }

    public void addNew(ActionEvent actionEvent) {
    }

    public void Delete(ActionEvent actionEvent) {
    }

    public void saveNew(ActionEvent actionEvent) {
    }

    public void cancelAdd(ActionEvent actionEvent) {
    }
}
