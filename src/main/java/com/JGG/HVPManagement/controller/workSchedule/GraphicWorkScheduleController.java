package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class GraphicWorkScheduleController implements Initializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneHarbor;
    public AnchorPane rootPane;
    public VBox paneGridPanesContainer;
    private Model model;
    private Utilities utilities;
    private WorkScheduleController workScheduleController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        loadCollaboratorsView();
        workScheduleController = new WorkScheduleController();
    }

    public void loadCollaboratorsView() {
        workScheduleController.loadCalendarHeader(gridPaneHeader);
        workScheduleController.loadCalendarDaysHeader(gridPaneHeader, 1);
        createHoursGridPane(gridPaneUrban);
        createHoursGridPane(gridPaneHarbor);
        createHoursGridPane(gridPaneMontejo);
        loadHoursGridPane(gridPaneUrban);
        loadHoursGridPane(gridPaneHarbor);
        loadHoursGridPane(gridPaneMontejo);
        createInternalGrids();
        loadData();
    }

    private void createHoursGridPane(GridPane gridPane) {
        GridPane hoursGridPane = new GridPane();
        int numColumns = 1;
        int numRows = model.availableHoursOld.length;
        if (gridPane.equals(gridPaneMontejo)) numRows = 2;
        gridPane.add(hoursGridPane, 0, 1);
        for (int col = 0; col < numColumns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            hoursGridPane.getColumnConstraints().add(columnConstraints);
        }
        for (int row = 0; row < numRows; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            hoursGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void loadHoursGridPane(GridPane gridPane) {
        GridPane internalGridPane = (GridPane) utilities.getNodeFromGridPane(gridPane, 0, 1);
        if (!gridPane.equals(gridPaneMontejo)) {
            for (int i = 0; i < model.availableHoursOld.length; i++) {
                Label lblHour = new Label(model.availableHoursOld[i]);
                internalGridPane.add(lblHour, 0, i);
            }
        } else {
            internalGridPane.add(new Label("09:00"), 0, 0);
            internalGridPane.add(new Label("15:00"), 0, 1);
        }
    }

    private void createInternalGrids() {
        LocalDate localDate;
        GridPane parentGridPane;
        int colIndex;
        int rowIndex = 1;
        int numColumns;
        int numRows;

        for (int i = 0; i < 7; i++) {
            localDate = model.mondayOfTheWeek.plusDays(i);
            for (String branch : model.branchesNames) {
                int maxPerBranch = 0;
                for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
                    if (tempWorkSchedule.getBranch() != null) {
                        String tempBranchName = tempWorkSchedule.getBranch().getName();
                        if (tempWorkSchedule.getLocalDate().equals(localDate) && tempBranchName.equals(branch)) {
                            maxPerBranch++;
                        }
                    }
                }
                colIndex = i + 1;
                numColumns = maxPerBranch;
                numRows = model.availableHoursOld.length;

                parentGridPane = workScheduleController.getGridPaneByBranchName(branch, gridPaneUrban, gridPaneHarbor, gridPaneMontejo);

                if (parentGridPane.equals(gridPaneMontejo)) {
                    numRows = 2;
                }
                createInternalGrid(parentGridPane, colIndex, rowIndex, numColumns, numRows);
            }
        }
    }

    // todo check if I can create a method for creating grids
    private void createInternalGrid(GridPane parentGridPane, int colIndex, int rowIndex, int numColumns, int numRows) {
        GridPane internalGridPane = new GridPane();
        parentGridPane.add(internalGridPane, colIndex, rowIndex);
        internalGridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        for (int col = 0; col < numColumns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            internalGridPane.getColumnConstraints().add(columnConstraints);
        }
        for (int row = 0; row < numRows; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            rowConstraints.setMinHeight(20);
            internalGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    private void loadData() {
        GridPane grandParentGridPane;
        int colGrandParentIndex;
        int rowGrandParentIndex = 1;
        GridPane parentGridPane;
        int colParentIndex = 0;
        int rowParentIndexStart = 0;
        int rowParentIndexEnd = 0;
        Label label;

        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getWorkingDayType().getItNeedBranches()) {
                String branchName = tempWorkSchedule.getBranch().getName();
                grandParentGridPane = workScheduleController.getGridPaneByBranchName(branchName, gridPaneUrban, gridPaneHarbor, gridPaneMontejo);
                long daysBetween = ChronoUnit.DAYS.between(model.mondayOfTheWeek, tempWorkSchedule.getLocalDate());
                colGrandParentIndex = (int) (daysBetween) + 1;
                parentGridPane = (GridPane) utilities.getNodeFromGridPane(grandParentGridPane, colGrandParentIndex, rowGrandParentIndex);


                for (int col = 0; col < parentGridPane.getColumnCount(); col++) {
                    boolean isColumnEmpty = true;
                    for (int row = 0; row < parentGridPane.getRowCount(); row++) {
                        if (utilities.getNodeFromGridPane(parentGridPane, col, row) != null) {
                            isColumnEmpty = false;
                            break;
                        }
                    }
                    if (isColumnEmpty) {
                        colParentIndex = col;
                        break;
                    }
                }


                if (!tempWorkSchedule.getBranch().getName().equals("Montejo")) {
                    for (int i = 0; i < model.availableHoursOld.length; i++) {
                        LocalTime parsedLocalTime = LocalTime.parse(model.availableHoursOld[i]);
                        if (tempWorkSchedule.getStartingTime().getHour() == parsedLocalTime.getHour()) {
                            rowParentIndexStart = i;
                            rowParentIndexEnd = i + tempWorkSchedule.getEndingTime().getHour() - tempWorkSchedule.getStartingTime().getHour() - 1;
                        }
                    }
                    for (int i = rowParentIndexStart; i <= rowParentIndexEnd; i++) {
                        label = new Label(tempWorkSchedule.getCollaborator().getUser().getUserName());
                        setLabelStyle(label, tempWorkSchedule);
                        parentGridPane.add(label, colParentIndex, i);
                    }
                } else {
                    if (tempWorkSchedule.getStartingTime().isBefore(LocalTime.of(15, 0))) {
                        label = new Label(tempWorkSchedule.getCollaborator().getUser().getUserName());
                        setLabelStyle(label, tempWorkSchedule);
                        parentGridPane.add(label, colParentIndex, 0);
                    }
                    if (tempWorkSchedule.getEndingTime().isAfter(LocalTime.of(15, 0))) {
                        label = new Label(tempWorkSchedule.getCollaborator().getUser().getUserName());
                        setLabelStyle(label, tempWorkSchedule);
                        parentGridPane.add(label, colParentIndex, 1);
                    }
                }


            }
        }
    }

    private void setLabelStyle(Label label, WorkSchedule tempWorkSchedule) {
        String jobPositionName = tempWorkSchedule.getCollaborator().getJobPosition().getName();
        switch (jobPositionName) {
            case "Directora administrativa":
            case "Gerente":
                label.setStyle("-fx-background-color: chocolate; -fx-font-size: 11");
                break;

            case "Recepcionista":

                label.setStyle("-fx-background-color: aqua; -fx-font-size: 11");
                break;

            case "Director médico":
            case "Veterinario A":
            case "Veterinario B":
                label.setStyle("-fx-background-color: greenyellow; -fx-font-size: 11");
                break;

            case "Asistente A":
                label.setStyle("-fx-background-color: goldenrod; -fx-font-size: 11");
                break;

            case "Asistente B":
                label.setStyle("-fx-background-color: darksalmon; -fx-font-size: 11");
                break;

            case "Pasante":
                label.setStyle("-fx-background-color: antiquewhite; -fx-font-size: 11");
                break;
        }
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

    }
}
