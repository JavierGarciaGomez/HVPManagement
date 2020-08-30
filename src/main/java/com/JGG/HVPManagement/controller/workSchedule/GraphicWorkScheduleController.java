package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.OpeningHours;
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
import java.util.ArrayList;
import java.util.List;
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
    private List<LocalTime> availableHoursUrban;
    private List<LocalTime> availableHoursHarbor;
    private List<LocalTime> availableHoursMontejo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleController = new WorkScheduleController();

        availableHoursUrban = setAvailableHours(utilities.getBranchByName("Urban"));
        availableHoursHarbor = setAvailableHours(utilities.getBranchByName("Harbor"));
        availableHoursMontejo = setAvailableHours(utilities.getBranchByName("Montejo"));

        loadCollaboratorsView();
    }

    private List<LocalTime> setAvailableHours(Branch branch) {
        LocalTime minOpeningTime = LocalTime.MAX;
        LocalTime maxClosingTime = LocalTime.MIN;
        List<LocalTime> availableHours = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            OpeningHours tempOpeningHours = utilities.getOpeningHoursByBranchAndDate(branch, model.mondayOfTheWeek.plusDays(i));
            minOpeningTime = minOpeningTime.isBefore(tempOpeningHours.getOpeningHour()) ? minOpeningTime : tempOpeningHours.getOpeningHour();
            maxClosingTime = maxClosingTime.isAfter(tempOpeningHours.getClosingHour()) ? maxClosingTime : tempOpeningHours.getClosingHour();
        }
        LocalTime localTime = minOpeningTime;

        if (!branch.equals(utilities.getBranchByName("Montejo"))) {
            while (localTime.isBefore(maxClosingTime)) {
                availableHours.add(localTime);
                localTime = localTime.plusHours(1);
                if (localTime.getMinute() != 0) {
                    localTime = LocalTime.of(localTime.getHour(), 0);
                }
            }

        } else {
            availableHours.add(minOpeningTime);
            long difHours = ChronoUnit.HOURS.between(minOpeningTime, maxClosingTime);
            LocalTime midDay = localTime.plusHours(difHours / 2);
            availableHours.add(midDay);
        }
        return availableHours;
    }


    public void loadCollaboratorsView() {
        workScheduleController.loadCalendarHeader(gridPaneHeader);
        workScheduleController.loadCalendarDaysHeader(gridPaneHeader, 1);
        for (Branch branch : model.branches) {
            createHoursGridPane(branch);
        }
        for (Branch branch : model.branches) {
            loadHoursGridPane(branch);
        }
        createInternalGrids();
        loadData();
    }

    private void createHoursGridPane(Branch branch) {
        List<LocalTime> availableHours = getAvailableHoursByBranch(branch);
        GridPane gridPane = workScheduleController.getGridPaneByBranchName(branch.getName(), gridPaneUrban, gridPaneHarbor, gridPaneMontejo);
        GridPane hoursGridPane = new GridPane();
        int numColumns = 1;
        int numRows = availableHours.size();

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

    private void loadHoursGridPane(Branch branch) {
        List<LocalTime> availableHours = getAvailableHoursByBranch(branch);
        GridPane gridPane = workScheduleController.getGridPaneByBranchName(branch.getName(), gridPaneUrban, gridPaneHarbor, gridPaneMontejo);
        GridPane internalGridPane = (GridPane) utilities.getNodeFromGridPane(gridPane, 0, 1);
        for (int i = 0; i < availableHours.size(); i++) {
            Label lblHour = new Label(String.valueOf(availableHours.get(i)));
            internalGridPane.add(lblHour, 0, i);
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
            for (Branch branch : model.branches) {
                int maxPerBranch = 0;
                for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
                    if (tempWorkSchedule.getBranch() != null) {
                        if (tempWorkSchedule.getLocalDate().equals(localDate) && tempWorkSchedule.getBranch().equals(branch)) {
                            maxPerBranch++;
                        }
                    }
                }
                colIndex = i + 1;
                numColumns = maxPerBranch;
                List<LocalTime> availableHours = getAvailableHoursByBranch(branch);
                numRows = availableHours.size();

                parentGridPane = workScheduleController.getGridPaneByBranchName(branch.getName(), gridPaneUrban, gridPaneHarbor, gridPaneMontejo);

                if (parentGridPane.equals(gridPaneMontejo)) {
                    numRows = 2;
                }
                createInternalGrid(parentGridPane, colIndex, rowIndex, numColumns, numRows);
            }
        }
    }


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
                List<LocalTime> availableHours = getAvailableHoursByBranch(tempWorkSchedule.getBranch());

                for (int i = 0; i < availableHours.size(); i++) {
                    if (tempWorkSchedule.getStartingTime().getHour() == availableHours.get(i).getHour()) {
                        rowParentIndexStart = i;
                    }
                    if (tempWorkSchedule.getEndingTime().getHour() - 1 >= availableHours.get(i).getHour()) {
                        rowParentIndexEnd = i;
                    }
                }
                for (int i = rowParentIndexStart; i <= rowParentIndexEnd; i++) {
                    label = new Label(tempWorkSchedule.getCollaborator().getUser().getUserName());
                    setLabelStyle(label, tempWorkSchedule);
                    parentGridPane.add(label, colParentIndex, i);
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

    public List<LocalTime> getAvailableHoursByBranch(Branch branch) {

        if (branch.equals(utilities.getBranchByName("Urban"))) {
            return availableHoursUrban;
        } else if (branch.equals(utilities.getBranchByName("Harbor"))) {
            return availableHoursHarbor;
        } else if (branch.equals(utilities.getBranchByName("Montejo"))) {
            return availableHoursMontejo;
        } else return null;
    }
}
