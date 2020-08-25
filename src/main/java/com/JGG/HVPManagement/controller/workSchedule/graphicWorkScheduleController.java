package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class graphicWorkScheduleController implements Initializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneHarbor;
    public AnchorPane rootPane;
    public VBox paneGridPanesContainer;
    private Model model;
    private Utilities utilities;
    private WorkScheduleDAO workScheduleDAO;

    private List<WorkSchedule> workSchedulesDB;
    private List<WorkSchedule> tempWorkSchedules;
    List<String> workingDayTypesWithHour = new ArrayList<>(Arrays.asList("ORD", "PER", "ASE", "INC", "IMS", "JUE", "INJ", "PED"));
    List<String> workingDayTypesWithBranch = new ArrayList<>(Arrays.asList("ORD", "PER"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
        loadCollaboratorsView();
    }

    public void loadCollaboratorsView() {
        loadCalendarDaysHeader();
        // remove unused gridpanes
        paneGridPanesContainer.getChildren().clear();
        gridPaneViewCollaborators = new GridPane();
        int totalColumns = model.weekDaysNames.length + 1;
        int totalRows = model.activeAndWorkerCollaborators.size() + 2;

        for (int col = 0; col < totalColumns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            columnConstraints.setMinWidth(200);
            gridPaneViewCollaborators.getColumnConstraints().add(columnConstraints);
        }
        gridPaneViewCollaborators.getColumnConstraints().get(0).setMinWidth(40);

        for (int row = 0; row < totalRows; row++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setFillHeight(true);
            rowConstraints.setVgrow(Priority.SOMETIMES);
            rowConstraints.setPrefHeight(30);
            gridPaneViewCollaborators.getRowConstraints().add(rowConstraints);
        }

        paneGridPanesContainer.getChildren().add(gridPaneViewCollaborators);

        loadCalendarHeader(gridPaneViewCollaborators);
        loadCalendarDaysHeader(gridPaneViewCollaborators, 1);
        loadCollaboratorsNames(gridPaneViewCollaborators, 2);
        loadInternalGrids(gridPaneViewCollaborators);

        endingRun = LocalTime.now();
    }


    private void loadCalendarHeader(GridPane gridPane) {
        utilities.clearGridPaneChildren(gridPane, 0, 0);
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() + "/" + model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() + "/" + lastDate.getMonthValue();
        String fullString = "CALENDAR FROM " + strFirstDay + " TO " + strLastDay;
        Label label = new Label(fullString);
        gridPane.add(label, 0, 0, gridPane.getColumnCount(), 1);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }

    private void loadCalendarDaysHeader(GridPane gridPane, int startingColumn) {
        utilities.clearGridPaneChildren(gridPane, startingColumn, 1);

        LocalDate localDate;
        for (int i = 0; i < model.weekDaysNames.length; i++) {
            int col = i + startingColumn;
            localDate = model.mondayOfTheWeek.plusDays(i);
            int dayNumber = localDate.getDayOfMonth();
            int monthNumber = localDate.getMonthValue();
            String labelString = model.weekDaysNames[i] + " " + dayNumber + " / " + monthNumber;
            Label dayLabel = new Label(labelString);
            gridPane.add(dayLabel, col, 1);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
        }
    }

    // todo need to create an empty collaborators view and another to load the database

    private void loadCollaboratorsNames(GridPane gridPane, int startingRow) {
        utilities.clearGridPaneChildren(gridPane, 1, startingRow);

        for (int i = 0; i < model.activeAndWorkersUserNames.size(); i++) {
            int row = i + startingRow;
            Label dayLabel = new Label(model.activeAndWorkersUserNames.get(i));
            gridPane.add(dayLabel, 0, row);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setStyle("-fx-font-size: 11");
        }
    }

    private void loadInternalGrids(GridPane gridPane) {
        utilities.clearGridPaneChildren(gridPane, 1, 2);
        for (WorkSchedule workSchedule : tempWorkSchedules) {
            int row = 0;
            int col = 0;

            for (int i = 0; i < model.activeAndWorkersUserNames.size(); i++) {
                if (model.activeAndWorkersUserNames.get(i).equals(workSchedule.getCollaborator().getUser().getUserName())) {
                    row = i + 2;
                    break;
                }
            }
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                if (localDate.equals(workSchedule.getLocalDate())) {
                    col = i + 1;
                }
            }
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);

            // set the id here as readabletext
            gridPane.add(hBox, col, row);
            // workingDayType (ComboBox), Branch(ComboBox), startingTime(Field), endingTime(Field,
            ChoiceBox<String> cboWorkingDayType = new ChoiceBox<>();
            cboWorkingDayType.setPrefWidth(50);
            cboWorkingDayType.getItems().addAll(model.workingDayTypes);
            cboWorkingDayType.setId("choiceBoxFont10");

            ChoiceBox<String> cboBranch = new ChoiceBox<>();
            cboBranch.setPrefWidth(60);
            cboBranch.getItems().addAll(model.branchesAndNone);
            cboBranch.setId("choiceBoxFont10");

            TextField txtStartingTime = new TextField();
            txtStartingTime.setPrefWidth(40);
            txtStartingTime.setStyle("-fx-font-size: 10");
            addChangeListenerToTimeField(txtStartingTime);

            TextField txtEndingTime = new TextField();
            txtEndingTime.setPrefWidth(40);
            txtEndingTime.setStyle("-fx-font-size: 10");
            addChangeListenerToTimeField(txtEndingTime);

            hBox.getChildren().addAll(cboWorkingDayType, cboBranch, txtStartingTime, txtEndingTime);

            addChangeListenerToValidateCollaboratorView(cboWorkingDayType, cboBranch, txtStartingTime, txtEndingTime);

            // Retrieving the data
            cboWorkingDayType.getSelectionModel().select(workSchedule.getWorkingDayType());
            if (workSchedule.getBranch() != null) {
                cboBranch.getSelectionModel().select(workSchedule.getBranch());
            }
            if (workSchedule.getStartingTime() != null) {
                txtStartingTime.setText(String.valueOf(workSchedule.getStartingTime()));
            }
            if (workSchedule.getEndingTime() != null) {
                txtEndingTime.setText(String.valueOf(workSchedule.getEndingTime()));
            }
        }
    }

    /*
     * COMMON VIEWS FUNCTIONS
     * */


}
