package com.JGG.HVPManagement.controller.configuration;

import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {
    private final Utilities utilities=Utilities.getInstance();
    private final Runnables runnables=Runnables.getInstance();
    public void showBranchesManagement(ActionEvent actionEvent) {
        utilities.loadModalWindowWithInitData("view/configuration/ManageBranches.fxml", "Add a new branch",
                true, true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread branchesThread = runnables.runBranches();
        Thread workingDayTypesThread = runnables.runWorkingDayTypes();
        Thread openingHoursThread = runnables.runOpeningHours();
        Thread jobPositionsThread = runnables.runJobPositions();
        try {
            branchesThread.join();
            workingDayTypesThread.join();
            openingHoursThread.join();
            jobPositionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showWorkingDayTypes(ActionEvent actionEvent) {
        utilities.loadModalWindow("view/configuration/ManageWorkingDayType.fxml", "Manage Activity Work Types",
                true, true);
    }

    public void showOpeningHours(ActionEvent actionEvent) {
        utilities.loadModalWindow("view/configuration/ManageOpeningHours.fxml", "Manage Opening Hours",
                true, true);

    }

    public void showJobPositions(ActionEvent actionEvent) {
        utilities.loadModalWindow("view/configuration/ManageJobPositions.fxml", "Manage Job positions",
                true, true);
    }

    public void showAccounts(ActionEvent actionEvent) {
    }
}
