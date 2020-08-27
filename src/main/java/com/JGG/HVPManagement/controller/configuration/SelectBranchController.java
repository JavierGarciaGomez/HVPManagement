package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.dao.BranchDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectBranchController implements Initializable {
    public VBox rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Branch> branches = BranchDAO.getInstance().getBranches();
        for(Branch branch: branches){
            RadioButton radioButton = new RadioButton(branch.getName());
            radioButton.setOnAction(event -> selectBranch(branch));
            rootPane.getChildren().add(radioButton);
        }
    }

    private void selectBranch(Branch branch) {
        Model.getInstance().selectedBranch =branch;
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.hide();
    }
}
