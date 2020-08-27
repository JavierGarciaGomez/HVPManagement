package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {
    private Utilities utilities;
    public void showBranchesManagement(ActionEvent actionEvent) {
        utilities.loadWindowWithInitData("view/configuration/ManageBranches.fxml", new Stage(), "Manage Branches",
                StageStyle.DECORATED, true, true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilities = Utilities.getInstance();
    }

    public void showActivityWorkTypes(ActionEvent actionEvent) {
        utilities.loadWindowWithInitData("view/configuration/ManageWorkingDayType.fxml", new Stage(), "Manage Activity Work Types",
                StageStyle.DECORATED, true, true);
    }
}
