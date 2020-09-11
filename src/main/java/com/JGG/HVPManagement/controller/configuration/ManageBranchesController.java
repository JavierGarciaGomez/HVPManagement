package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.BranchDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class ManageBranchesController implements MyInitializable {
    public GridPane rootPane;
    public TextField txtName;
    public TextField txtAbbr;
    public TextArea txtAddress;
    public TextField txtPhone;
    public TextField txtWhatsappNumber;
    public DatePicker dtpOpeningDate;
    private Model model;
    private Stage thisStage;
    private BranchDAO branchDAO;
    private Utilities utilities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        branchDAO = BranchDAO.getInstance();
        utilities=Utilities.getInstance();

    }

    @Override
    public void initData() {
        this.thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.setOnHiding(event -> model.selectedBranch=null);
    }

    public void edit() {
        utilities.loadModalWindow("view/configuration/SelectBranch.fxml", "Select the branch", false, true);
        if (model.selectedBranch != null) setBranchData();
    }

    public void save() {

        String name = txtName.getText();
        String abbr = txtAbbr.getText();
        LocalDate openingDate = dtpOpeningDate.getValue();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();
        String whatsappNumber = txtWhatsappNumber.getText();

        Branch branch;
        branch = Objects.requireNonNullElseGet(model.selectedBranch, Branch::new);
        branch.setName(name);
        branch.setAbbr(abbr);
        branch.setOpeningDay(openingDate);
        branch.setAddress(address);
        branch.setPhoneNumber(phone);
        branch.setWhatsappNumber(whatsappNumber);

        branchDAO.createBranch(branch);
        model.selectedBranch = null;
        utilities.showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "The branch was created or updated successfully");
        utilities.loadModalWindow("view/main/Main.fxml", "Main Window", false, false);
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.hide();
    }

    private void setBranchData() {
        System.out.println("PRINTING " + model.selectedBranch);
        txtName.setText(model.selectedBranch.getName());
        txtAbbr.setText(model.selectedBranch.getAbbr());
        dtpOpeningDate.setValue(model.selectedBranch.getOpeningDay());
        txtAddress.setText(model.selectedBranch.getAddress());
        txtPhone.setText(model.selectedBranch.getPhoneNumber());
        txtWhatsappNumber.setText(model.selectedBranch.getWhatsappNumber());
    }

    public void cancel() {
        thisStage.hide();
    }
}
