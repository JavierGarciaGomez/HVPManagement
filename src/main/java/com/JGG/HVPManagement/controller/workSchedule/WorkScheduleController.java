package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import com.JGG.HVPManagement.model.*;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class WorkScheduleController implements MyInitializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneRest;
    public GridPane gridPaneHarbor;
    public AnchorPane rootPane;
    public DatePicker datePicker;
    public VBox paneGridPanesContainer;
    public ChoiceBox<Model.views> cboViewSelector;
    public VBox panVboxLeft;
    public Button btnSaveIntoDB;
    public Button btnCopy;
    public Button btnEmpty;
    private GridPane gridPaneViewCollaborators;
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    private final Runnables runnables = Runnables.getInstance();
    private final WorkScheduleDAO workScheduleDAO = WorkScheduleDAO.getInstance();
    private List<WorkSchedule> weekWorkSchedulesDB;
    private ArrayList<WorkScheduleError> errors;
    private boolean hasErrors = false;
    private boolean hasWarnings = false;
    private List<WorkSchedule> workschedulesToUpdate;
    private List<WorkSchedule> workschedulesToSave;
    private List<GridPane> branchesGridPanes;
    private boolean isFirstLoadFinished;
    //todo delete

    @Override
    public void initData() {
        Stage thisStage = (Stage) rootPane.getScene().getWindow();
        thisStage.setMaximized(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComboBoxes();
        initVariables();
        loadView();
        setRoleView();
    }

    private void setRoleView() {
        if (utilities.oneOfEquals(Model.role.USER, Model.role.GUEST_USER, model.roleView)) {
            btnSaveIntoDB.setVisible(false);
            btnCopy.setVisible(false);
            btnEmpty.setVisible(false);
        }
    }

    private void loadComboBoxes() {
        cboViewSelector.getItems().setAll(Model.views.values());
    }

    // init variables and instances
    private void initVariables() {
        if (model.selectedView == null) {
            model.selectedView = Model.views.BRANCH_VIEW;
        }
        branchesGridPanes = new ArrayList<>(Arrays.asList(gridPaneUrban, gridPaneHarbor, gridPaneMontejo));
        refreshVariables();
        cboViewSelector.getSelectionModel().select(model.selectedView);
        datePicker.setValue(model.selectedLocalDate);
        isFirstLoadFinished = true;
    }

    private void refreshVariables() {
        try {
            if (model.selectedLocalDate == null) {
                model.selectedLocalDate = LocalDate.now();
            }
            utilities.setMondayDate();
            Thread workScheduleThread = runnables.runWorkSchedulesBetweenDates(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
            Thread activeCollaboratorsThread = runnables.runActiveCollaborators();
            Thread branchesThread = runnables.runBranches();
            Thread openingHoursThread = runnables.runOpeningHours();
            Thread jobPositionsThread = runnables.runJobPositions();
            Thread workingDayTypesThread = runnables.runWorkingDayTypes();
            openingHoursThread.join();
            branchesThread.join();

            model.tempOpeningHoursDetailedList = utilities.getOpeningHoursDetailedListByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));

            workScheduleThread.join();
            weekWorkSchedulesDB = model.tempWorkSchedules;
            //weekWorkSchedulesDB = utilities.getWorkSchedulesBetweenDates(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
            model.tempWorkSchedules = new ArrayList<>();
            for (WorkSchedule workScheduleDB : weekWorkSchedulesDB) {
                WorkSchedule tempWorkSchedule = new WorkSchedule();
                tempWorkSchedule.setId(workScheduleDB.getId());
                tempWorkSchedule.setCollaborator(workScheduleDB.getCollaborator());
                tempWorkSchedule.setLocalDate(workScheduleDB.getLocalDate());
                tempWorkSchedule.setWorkingDayType(workScheduleDB.getWorkingDayType());
                tempWorkSchedule.setBranch(workScheduleDB.getBranch());
                tempWorkSchedule.setStartingLDT(workScheduleDB.getStartingLDT());
                tempWorkSchedule.setEndingLDT(workScheduleDB.getEndingLDT());
                tempWorkSchedule.setRegisteredBy(workScheduleDB.getRegisteredBy());
                model.tempWorkSchedules.add(tempWorkSchedule);
            }

            activeCollaboratorsThread.join();
            jobPositionsThread.join();
            workingDayTypesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadView() {
        if (model.selectedView == Model.views.BRANCH_VIEW) {
            loadBranchView();
            if (!model.tempWorkSchedules.isEmpty()) {
                loadInternalGridsBranchView();
                loadInternalDataBranchView();
            }
        } else if (model.selectedView == Model.views.COLLABORATOR_VIEW) {
            loadCollaboratorsView();
            if (!model.tempWorkSchedules.isEmpty()) {
                loadInternalDataCollaboratorView();
            }
        }
    }

    /*LOADERS OF BRANCH VIEW*/
    private void loadBranchView() {
        paneGridPanesContainer.getChildren().clear();
        paneGridPanesContainer.getChildren().add(gridPaneHeader);
        paneGridPanesContainer.getChildren().addAll(branchesGridPanes);
        paneGridPanesContainer.getChildren().add(gridPaneRest);
        for (GridPane branchGridPane : branchesGridPanes) {
            utilities.clearGridPaneChildren(branchGridPane, 0, 1);
        }
        utilities.clearGridPaneChildren(gridPaneRest, 0, 1);
        loadCalendarHeader(gridPaneHeader);
        loadCalendarDaysHeader(gridPaneHeader, 0);
        addRowsToGrid(gridPaneUrban, 5);
        addRowsToGrid(gridPaneHarbor, 4);
        addRowsToGrid(gridPaneMontejo, 1);
        addGridPanesToGridPaneRest();
    }

    public void loadCalendarHeader(GridPane gridPane) {
        utilities.clearGridPaneChildren(gridPane, 0, 0);
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() + "/" + model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() + "/" + lastDate.getMonthValue();
        String fullString = "CALENDAR FROM " + strFirstDay + " TO " + strLastDay;
        Label label = new Label(fullString);
        label.setId("title");
        gridPane.add(label, 0, 0, gridPane.getColumnCount(), 1);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }

    public void loadCalendarDaysHeader(GridPane gridPane, int startingColumn) {
        utilities.clearGridPaneChildren(gridPane, startingColumn, 1);

        LocalDate localDate;
        for (int i = 0; i < model.weekDaysNames.length; i++) {
            int col = i + startingColumn;
            localDate = model.mondayOfTheWeek.plusDays(i);
            int dayNumber = localDate.getDayOfMonth();
            int monthNumber = localDate.getMonthValue();
            String labelString = model.weekDaysNames[i] + " " + dayNumber + " / " + monthNumber;
            Label dayLabel = new Label(labelString);
            dayLabel.setId("subTitle");
            gridPane.add(dayLabel, col, 1);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
        }
    }

    private void addRowsToGrid(GridPane gridPaneBranch, int rowsToAdd) {
        int rows = gridPaneBranch.getRowCount();
        HBox tempHBox;
        for (int i = 0; i < rowsToAdd; i++) {
            for (int j = 0; j < gridPaneBranch.getColumnCount(); j++) {
                tempHBox = new HBox();
//                gridPaneBranch.getRowConstraints().add(new RowConstraints(10));
                gridPaneBranch.add(tempHBox, j, rows + i);
                // combobox for users
                ChoiceBox<Collaborator> cboCollaborators = new ChoiceBox<>();
                //cboUsers.setPrefWidth(60);
                //cboUsers.setPrefSize(60, 25);
                cboCollaborators.setMaxSize(60, 25);
                cboCollaborators.setMinSize(60, 25);

                cboCollaborators.setItems(FXCollections.observableList(model.activeAndWorkerCollaboratorsAndNull));

                TextField txtStartingTime = new TextField();
                txtStartingTime.setPrefWidth((50));
                txtStartingTime.setStyle("-fx-background-color: lightgrey");
                //txtStartingTime.setPrefHeight(20);
                utilities.addChangeListenerToTimeField(txtStartingTime);

                Label label = new Label("-");

                TextField txtEndingTime = new TextField();
                txtEndingTime.setPrefWidth(50);
                txtEndingTime.setStyle("-fx-background-color: lightgrey");
                //txtEndingTime.setPrefHeight(Double.MAX_VALUE);

                utilities.addChangeListenerToTimeField(txtEndingTime);
                cboCollaborators.setOnAction(event -> handleSelectUserBranchView(cboCollaborators));

                HBox finalTempHBox = tempHBox;
                //addChangeListenerToValidateBranchView(cboUsers, txtStartingTime, txtEndingTime);

                tempHBox.getChildren().addAll(cboCollaborators, txtStartingTime, label, txtEndingTime);
                txtStartingTime.textProperty().addListener((observable, oldValue, newValue) -> validateHBoxBranchView(finalTempHBox));
                txtEndingTime.textProperty().addListener((observable, oldValue, newValue) -> validateHBoxBranchView(finalTempHBox));

            }
        }
    }

    private void addGridPanesToGridPaneRest() {
        for (int col = 0; col < gridPaneRest.getColumnCount(); col++) {
            GridPane internalGridPane = new GridPane();
            gridPaneRest.add(internalGridPane, col, 1);
            for (int col2 = 0; col2 < 2; col2++) {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setHgrow(Priority.SOMETIMES);
                internalGridPane.getColumnConstraints().add(columnConstraints);
            }
            for (int row = 0; row < 8; row++) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setVgrow(Priority.SOMETIMES);
                internalGridPane.getRowConstraints().add(rowConstraints);
            }
        }
    }

    public void loadInternalGridsBranchView() {
        for (GridPane gridPane : branchesGridPanes) {
            utilities.clearGridPaneChildren(gridPane, 0, 1);
        }
        utilities.clearGridPanesNodesChildren(gridPaneRest, 0, 1);

        int rowsNeededUrban = 0;
        int rowsNeededTheHarbor = 0;
        int rowsNeededMontejo = 0;
        for (LocalDate localDate = model.mondayOfTheWeek; localDate.isBefore(model.mondayOfTheWeek.plusDays(7)); localDate = localDate.plusDays(1)) {
            int tempRowsNeededUrban = 0;
            int tempRowsNeededTheHarbor = 0;
            int tempRowsNeededMontejo = 0;
            for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                // todo change this methods
                if (workSchedule.getLocalDate().equals(localDate) && (workSchedule.getWorkingDayType().isItNeedBranches())) {
                    Branch branch = workSchedule.getBranch();
                    if (branch.equals(utilities.getBranchByName("Urban"))) {
                        tempRowsNeededUrban += 1;
                    }
                    if (branch.equals(utilities.getBranchByName("Harbor"))) {
                        tempRowsNeededTheHarbor += 1;
                    }
                    if (branch.equals(utilities.getBranchByName("Montejo"))) {
                        tempRowsNeededMontejo += 1;
                    }
                }
            }
            rowsNeededUrban = Math.max(rowsNeededUrban, tempRowsNeededUrban);
            rowsNeededTheHarbor = Math.max(rowsNeededTheHarbor, tempRowsNeededTheHarbor);
            rowsNeededMontejo = Math.max(rowsNeededMontejo, tempRowsNeededMontejo);

        }
        addRowsToGrid(gridPaneUrban, rowsNeededUrban);
        addRowsToGrid(gridPaneHarbor, rowsNeededTheHarbor);
        addRowsToGrid(gridPaneMontejo, rowsNeededMontejo);
    }

    public void loadInternalDataBranchView() {
        for (WorkSchedule workSchedule : model.tempWorkSchedules) {
            if (workSchedule.getWorkingDayType().isItNeedBranches()) {
                GridPane gridPane;
                int col;
                String branchName = workSchedule.getBranch().getName();
                gridPane = getGridPaneByBranchName(branchName, gridPaneUrban, gridPaneHarbor, gridPaneMontejo);
                col = (int) ChronoUnit.DAYS.between(model.mondayOfTheWeek, workSchedule.getLocalDate());

                for (int row = 1; row < Objects.requireNonNull(gridPane).getRowCount(); row++) {
                    Node node = utilities.getNodeFromGridPane(gridPane, col, row);
                    HBox hBox = (HBox) node;
                    @SuppressWarnings("unchecked")
                    ChoiceBox<Collaborator> cboCollaborators = (ChoiceBox<Collaborator>) hBox.getChildren().get(0);
                    if (cboCollaborators.getSelectionModel().getSelectedItem() == null) {
                        cboCollaborators.getSelectionModel().select(workSchedule.getCollaborator());
                        /*((TextField) hBox.getChildren().get(1)).setText(String.valueOf(workSchedule.getStartingTime()));
                        ((TextField) hBox.getChildren().get(3)).setText(String.valueOf(workSchedule.getEndingTime()));*/
                        ((TextField) hBox.getChildren().get(1)).setText(String.valueOf(workSchedule.getStartingLDT().toLocalTime()));
                        ((TextField) hBox.getChildren().get(3)).setText(String.valueOf(workSchedule.getEndingLDT().toLocalTime()));
                        break;
                    }
                }
            }
        }
        insertRestLabels();
    }

    private void insertRestLabels() {
        GridPane internalGridPane;
        LocalDate localDate;
        for (int i = 0; i < gridPaneRest.getColumnCount(); i++) {
            int col = 0;
            int row = 0;
            localDate = model.mondayOfTheWeek.plusDays(i);
            internalGridPane = (GridPane) utilities.getNodeFromGridPane(gridPaneRest, i, 1);
            internalGridPane.getChildren().clear();

            for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getBranch() == null) {
                    if (col == 2) {
                        col = 0;
                        row++;
                    }
                    Label label = new Label(workSchedule.getCollaborator().getUser().getUserName());
                    label.setMaxHeight(Double.MAX_VALUE);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setAlignment(Pos.CENTER);
                    if (i % 2 == 1) {
                        label.setStyle("-fx-background-color: rgba(169,169,169,0.27)");
                    }
                    internalGridPane.add(label, col, row);
                    col++;
                }
            }
        }
    }


    /*LOADERS OF COLLABORATOR VIEW*/

    public void loadCollaboratorsView() {
        // remove unused gridpanes
        paneGridPanesContainer.getChildren().clear();
        gridPaneViewCollaborators = new GridPane();
        gridPaneViewCollaborators.setHgap(5);
        int totalColumns = model.weekDaysNames.length + 1;
        int totalRows = model.activeAndWorkerCollaborators.size() + 2;

        for (int col = 0; col < totalColumns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            columnConstraints.setMinWidth(164);
            gridPaneViewCollaborators.getColumnConstraints().add(columnConstraints);
        }
        gridPaneViewCollaborators.getColumnConstraints().get(0).setMinWidth(35);

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
        loadCollaboratorsNames(gridPaneViewCollaborators);
        loadInternalGridsCollaboratorView();
    }

    private void loadCollaboratorsNames(GridPane gridPane) {
        utilities.clearGridPaneChildren(gridPane, 1, 2);

        for (int i = 0; i < model.activeAndWorkersUserNames.size(); i++) {
            int row = i + 2;
            Label dayLabel = new Label(model.activeAndWorkersUserNames.get(i));
            gridPane.add(dayLabel, 0, row);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setStyle("-fx-font-size: 11");
        }
    }

    private void loadInternalGridsCollaboratorView() {
        utilities.clearGridPaneChildren(gridPaneViewCollaborators, 1, 2);
        for (int colIndex = 1; colIndex < gridPaneViewCollaborators.getColumnCount(); colIndex++) {
            for (int rowIndex = 2; rowIndex < gridPaneViewCollaborators.getRowCount(); rowIndex++) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                gridPaneViewCollaborators.add(hBox, colIndex, rowIndex);
                ChoiceBox<WorkingDayType> cboWorkingDayType = new ChoiceBox<>();
                cboWorkingDayType.setPrefSize(40, 25);
                cboWorkingDayType.getItems().addAll(model.workingDayTypes);
                cboWorkingDayType.setId("choiceBoxFont10");


                ChoiceBox<Branch> cboBranch = new ChoiceBox<>();
                cboBranch.setPrefSize(50, 25);
                cboBranch.getItems().addAll(model.branchesAndNone);
                cboBranch.setId("choiceBoxFont10");

                TextField txtStartingTime = new TextField();
                txtStartingTime.setPrefSize(37, 25);
                txtStartingTime.setStyle("-fx-font-size: 9");
                txtStartingTime.setStyle("-fx-background-color: lightgrey");
                utilities.addChangeListenerToTimeField(txtStartingTime);

                TextField txtEndingTime = new TextField();
                txtEndingTime.setPrefSize(37, 25);
                txtEndingTime.setStyle("-fx-font-size: 9");
                txtEndingTime.setStyle("-fx-background-color: lightgrey");
                utilities.addChangeListenerToTimeField(txtEndingTime);


                //addChangeListenerToValidateCollaboratorView(cboWorkingDayType, cboBranch, txtStartingTime, txtEndingTime);
                hBox.getChildren().addAll(cboWorkingDayType, cboBranch, txtStartingTime, txtEndingTime);

                cboWorkingDayType.setOnAction(event -> handleSelectWorkingDayTypeCollaboratorView(hBox));
                cboBranch.setOnAction(event -> handleSelectBranchCollaboratorView(hBox));

                txtStartingTime.textProperty().addListener((observable, oldValue, newValue) -> validateHBoxCollaboratorView(hBox));
                txtEndingTime.textProperty().addListener((observable, oldValue, newValue) -> validateHBoxCollaboratorView(hBox));

            }
        }
    }

    private void loadInternalDataCollaboratorView() {
        for (WorkSchedule workSchedule : model.tempWorkSchedules) {
            int col = 0;
            int row = 0;
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                if (localDate.equals(workSchedule.getLocalDate())) {
                    col = i + 1;
                    break;
                }
            }
            for (int i = 0; i < model.activeAndWorkersUserNames.size(); i++) {
                if (model.activeAndWorkersUserNames.get(i).equals(workSchedule.getCollaborator().getUser().getUserName())) {
                    row = i + 2;
                    break;
                }
            }

            HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPaneViewCollaborators, col, row);
            @SuppressWarnings("unchecked")
            ChoiceBox<WorkingDayType> cboWorkingDayType = (ChoiceBox<WorkingDayType>) hBox.getChildren().get(0);
            @SuppressWarnings("unchecked")
            ChoiceBox<Branch> cboBranch = (ChoiceBox<Branch>) hBox.getChildren().get(1);
            TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
            TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

            cboWorkingDayType.getSelectionModel().select(workSchedule.getWorkingDayType());
            if (workSchedule.getBranch() != null) {
                cboBranch.getSelectionModel().select(workSchedule.getBranch());
            }
//            if (workSchedule.getStartingTime() != null) {
            if (workSchedule.getStartingLDT() != null) {
                //txtStartingTime.setText(String.valueOf(workSchedule.getStartingTime()));
                txtStartingTime.setText(String.valueOf(workSchedule.getStartingLDT().toLocalTime()));
            }
//            if (workSchedule.getEndingTime() != null) {
//                txtEndingTime.setText(String.valueOf(workSchedule.getEndingTime()));
//            }
            if (workSchedule.getEndingLDT() != null) {
                txtEndingTime.setText(String.valueOf(workSchedule.getEndingLDT().toLocalTime()));
            }
        }
    }


    /*
     * BUTTONS
     * */
    // Load the database, clearing the grids, calculating rows need it

    public void changeDateOrView() {
        LocalDate originalLocalDate = model.selectedLocalDate;
        int originalIndex = cboViewSelector.getSelectionModel().getSelectedIndex();
        if (isFirstLoadFinished) {
            if (utilities.oneOfEquals(Model.role.ADMIN, Model.role.MANAGER, model.roleView)) {
                retrieveData();
                if (setWorkSchedulesToSaveOrUpdate()) {
                    boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "The unsaved data may be lost \nDo you want to save first?");
                    if (answer) {
                        // todo change this method to boolean and if false return
                        if (!saveIntoDB()) {
                            if (datePicker.getValue() != originalLocalDate) {
                                datePicker.setValue(originalLocalDate);
                            }
                            if (cboViewSelector.getSelectionModel().getSelectedIndex() != originalIndex) {
                                cboViewSelector.getSelectionModel().select(originalIndex);
                            }
                            return;
                        }
                    }
                }
            }
            model.selectedLocalDate = datePicker.getValue();
            refreshVariables();
            model.selectedView = cboViewSelector.getValue();
            loadView();
        }
    }

    public void setLastWeek() {
        datePicker.setValue(model.selectedLocalDate.minusDays(7));
    }

    public void setNextWeek() {
        datePicker.setValue(model.selectedLocalDate.plusDays(7));
    }

    public void setToday() {
        datePicker.setValue(LocalDate.now());
    }

    public void loadDataBase() {
        refreshVariables();
        loadView();
    }

    public void refresh() {
        retrieveData();
        loadView();
    }

    private void retrieveData() {
        if (model.selectedView == Model.views.BRANCH_VIEW) {
            if (!model.tempWorkSchedules.isEmpty()) setRestToCollaboratorsWithoutRegister();
            for (GridPane branchGridPane : branchesGridPanes) {
                retrieveDataFromPaneBranchView(branchGridPane);
            }
        } else if (model.selectedView == Model.views.COLLABORATOR_VIEW) {
            retrieveDataFromCollaboratorPane();
        }
        generateRestDays();
    }

    /// Check if the collaborators in tempWorkSchedule with a branch still has it, if not, set the wdt to rest

    private void setRestToCollaboratorsWithoutRegister() {
        HBox tempHBox;
        ChoiceBox<Collaborator> cboCollaborators;
        boolean isRegistered;

        for (int col = 0; col < 7; col++) {
            //made a list with of workschedules that has a branch in that day
            List<WorkSchedule> tempWorkSchedulesWithBranch = new ArrayList<>();
            for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
                if (tempWorkSchedule.getWorkingDayType().isItNeedBranches() &&
                        model.mondayOfTheWeek.plusDays(col).equals(tempWorkSchedule.getLocalDate())) {
                    tempWorkSchedulesWithBranch.add(tempWorkSchedule);
                }
            }
            //check if it has a selection in a combobox
            for (WorkSchedule tempWorkScheduleWithBranch : tempWorkSchedulesWithBranch) {
                isRegistered = false;
                for (GridPane branchGridPane : branchesGridPanes) {
                    for (int row = 1; row < branchGridPane.getRowCount(); row++) {
                        tempHBox = (HBox) utilities.getNodeFromGridPane(branchGridPane, col, row);
                        cboCollaborators = (ChoiceBox<Collaborator>) tempHBox.getChildren().get(0);
                        Collaborator collaborator = cboCollaborators.getSelectionModel().getSelectedItem();
                        if (Objects.equals(collaborator, tempWorkScheduleWithBranch.getCollaborator())) {
                            isRegistered = true;
                            break;
                        }
                    }
                    if (isRegistered) break;
                }
                //if none, change it to rest
                if (!isRegistered) {
                    tempWorkScheduleWithBranch.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                    tempWorkScheduleWithBranch.setBranch(null);
                    tempWorkScheduleWithBranch.setRegisteredBy(model.loggedUser.getCollaborator());
                    /*tempWorkScheduleWithBranch.setStartingTime(null);
                    tempWorkScheduleWithBranch.setEndingTime(null);*/
                    tempWorkScheduleWithBranch.setStartingLDT(null);
                    tempWorkScheduleWithBranch.setEndingLDT(null);
                    addOrUpdateTempWorkSchedules(tempWorkScheduleWithBranch);
                }
            }
        }
    }
    // It adds to the tempWorkSchedules arrayList each of the registered workSchedule

    private void retrieveDataFromPaneBranchView(GridPane gridPane) {
        // Loop for each column (each day)
        for (int col = 0; col < gridPane.getColumnCount(); col++) {
            LocalDate localDate = model.mondayOfTheWeek.plusDays(col);
            LocalTime localTime;
            // Loop for each row of each day
            for (int row = 1; row < gridPane.getRowCount(); row++) {
                HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPane, col, row);
                @SuppressWarnings("unchecked")
                ChoiceBox<Collaborator> cboCollaborator = (ChoiceBox<Collaborator>) hBox.getChildren().get(0);
                TextField txtStartingTime = (TextField) hBox.getChildren().get(1);
                TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

                if (cboCollaborator.getSelectionModel().getSelectedItem() != null) {
                    WorkSchedule workSchedule = new WorkSchedule();
                    // common setters
                    workSchedule.setLocalDate(localDate);
                    for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
                        if (collaborator.equals(cboCollaborator.getSelectionModel().getSelectedItem())) {
                            workSchedule.setCollaborator(collaborator);
                            break;
                        }
                    }

                    workSchedule = getWorkScheduleFromWorkSchedules(workSchedule); // get the workschedule if already exists; if not keeps the same workschedule
                    workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());

                    // if any time is empty, then clears all the data and generates a day without hours
                    if (txtStartingTime.getText().equals("") || txtEndingTime.getText().equals("")) {
                        workSchedule.setBranch(null);
                        /*workSchedule.setStartingTime(null);
                        workSchedule.setEndingTime(null);*/
                        workSchedule.setStartingLDT(null);
                        workSchedule.setEndingLDT(null);

                        // if exists and is not a day without hours, it assigns a rest day
                        if (workSchedule.getWorkingDayType() == null || workSchedule.getWorkingDayType().isItNeedHours()) {
                            workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                        }

                        // if the time is not empty, then it retrieves the data
                    } else {
                        Branch branch = utilities.getBranchByName(((Label) gridPane.getChildren().get(0)).getText());
                        workSchedule.setBranch(branch);

                        //workSchedule.setStartingTime(LocalTime.parse((((TextField) hBox.getChildren().get(1)).getText())));
                        localTime = LocalTime.parse((((TextField) hBox.getChildren().get(1)).getText()));
                        LocalDateTime startingLDT = LocalDateTime.of(localDate, localTime);
                        workSchedule.setStartingLDT(startingLDT);

                        //workSchedule.setEndingTime(LocalTime.parse((((TextField) hBox.getChildren().get(3)).getText())));
                        localTime = LocalTime.parse((((TextField) hBox.getChildren().get(3)).getText()));
                        LocalDateTime endingLDT = utilities.getEndingDateTimeWithTimeAdjuster(startingLDT, localTime);
                        workSchedule.setEndingLDT(endingLDT);

                        // if the workingdaytype is null, and the previous register has a wdt with no hours, it assigns ORD
                        if (workSchedule.getWorkingDayType() == null || !workSchedule.getWorkingDayType().isItNeedHours()) {
                            workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("ORD"));
                        }
                    }
                    addOrUpdateTempWorkSchedules(workSchedule);
                }
            }
        }
    }

    private void retrieveDataFromCollaboratorPane() {
        // Loop for each column (each day)
        for (int col = 1; col < gridPaneViewCollaborators.getColumnCount(); col++) {
            LocalDate localDate = model.mondayOfTheWeek.plusDays(col - 1);
            LocalTime localTime;
            // Loop for each row of each day
            for (int row = 2; row < gridPaneViewCollaborators.getRowCount(); row++) {
                HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPaneViewCollaborators, col, row);
                WorkSchedule workSchedule = new WorkSchedule();
                workSchedule.setLocalDate(localDate);
                workSchedule.setCollaborator(model.activeAndWorkerCollaborators.get(row - 2));
                workSchedule = getWorkScheduleFromWorkSchedules(workSchedule); // if the workschedule exists, get it; if not keep the new workschedule

                @SuppressWarnings("unchecked")
                ChoiceBox<WorkingDayType> cboWorkingDayType = (ChoiceBox<WorkingDayType>) hBox.getChildren().get(0);
                if (cboWorkingDayType.getSelectionModel().getSelectedItem() != null) {
                    workSchedule.setWorkingDayType(cboWorkingDayType.getSelectionModel().getSelectedItem());
                } else {
                    workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                }
                @SuppressWarnings("unchecked")
                ChoiceBox<Branch> cboBranch = (ChoiceBox<Branch>) hBox.getChildren().get(1);
                workSchedule.setBranch(cboBranch.getSelectionModel().getSelectedItem());

                TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
                LocalDateTime startingLDT = LocalDateTime.MIN;
                LocalDateTime endingLDT;
                if (txtStartingTime.getText().equals("")) {
//                    workSchedule.setStartingTime(null);
                    workSchedule.setStartingLDT(null);
                } else {
                    localTime = LocalTime.parse(txtStartingTime.getText());
                    startingLDT = LocalDateTime.of(localDate, localTime);
                    workSchedule.setStartingLDT(startingLDT);
                    // workSchedule.setStartingTime(LocalTime.parse(txtStartingTime.getText()));
                }
                TextField txtEndingTime = (TextField) hBox.getChildren().get(3);
                if (txtEndingTime.getText().equals("")) {
//                    workSchedule.setEndingTime(null);
                    workSchedule.setEndingLDT(null);
                } else {
                    localTime = LocalTime.parse(txtEndingTime.getText());
                    endingLDT = utilities.getEndingDateTimeWithTimeAdjuster(startingLDT, localTime);
                    workSchedule.setEndingLDT(endingLDT);
                    //workSchedule.setEndingTime(LocalTime.parse(txtEndingTime.getText()));
                }
                workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                addOrUpdateTempWorkSchedules(workSchedule);
            }
        }
    }

    private void generateRestDays() {
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                int registerPerCollaboratorPerDay = 0;
                for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                    if ((workSchedule.getCollaborator().equals(collaborator)) && (workSchedule.getLocalDate().equals(localDate))) {
                        registerPerCollaboratorPerDay++;
                    }
                }
                if (registerPerCollaboratorPerDay == 0) {
                    WorkSchedule restWorkSchedule = new WorkSchedule();
                    restWorkSchedule.setCollaborator(collaborator);
                    restWorkSchedule.setLocalDate(localDate);
                    restWorkSchedule = getWorkScheduleFromWorkSchedules(restWorkSchedule);
                    if (restWorkSchedule.getWorkingDayType() == null || !restWorkSchedule.getWorkingDayType().isItNeedBranches()) {
                        restWorkSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                    }
                    restWorkSchedule.setBranch(null);
                    restWorkSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    addOrUpdateTempWorkSchedules(restWorkSchedule);
                }
            }
        }
    }

    public void validateData() {
        retrieveData();
        hasWarnings = false;
        hasErrors = false;
        errors = new ArrayList<>();

        if (model.selectedView == Model.views.BRANCH_VIEW) validateUniqueUsers();
        validateInternally();
        validateReceptionistAtClose();
        validateHourlyIfThereIsAVetOrAsistA();
        validateWithDataBase();

        if (!errors.isEmpty()) {
            hasWarnings = true;
            for (WorkScheduleError workScheduleError : errors) {
                if (workScheduleError.getErrorType() == WorkScheduleError.errorType.ERROR) {
                    hasErrors = true;
                    break;
                }
            }
        }
        if (hasWarnings) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/workSchedule/Errors.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
                ErrorsController controller = loader.getController();
                controller.initData(errors);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateInternally() {
        // Vallidate the hours worked in the week
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            double totalTimeWorkedPerCollaborator = 0;
            for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                if ((workSchedule.getCollaborator().equals(collaborator))) {
                    //if (workSchedule.getStartingTime() != null && workSchedule.getEndingTime() != null) {
                    if (workSchedule.getStartingLDT() != null && workSchedule.getEndingLDT() != null) {
                        //totalTimeWorkedPerCollaborator += ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime()) / 60.0;
                        totalTimeWorkedPerCollaborator += ChronoUnit.MINUTES.between(workSchedule.getStartingLDT(), workSchedule.getEndingLDT()) / 60.0;
                    }
                }
            }
            if (totalTimeWorkedPerCollaborator != collaborator.getWorkingConditions().getWeeklyWorkingHours()) {
                errors.add(new WorkScheduleError(WorkScheduleError.errorType.WARNING, null, collaborator.getUser().getUserName(),
                        "Has " + collaborator.getWorkingConditions().getWeeklyWorkingHours() + " weekly working hours." +
                                " But you are trying to register: " + totalTimeWorkedPerCollaborator));
            }
        }

        // Various validations
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getWorkingDayType().isItNeedBranches()) {
                if (tempWorkSchedule.getBranch() == null) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type must have a branch"));
                }
            } else {
                if (tempWorkSchedule.getBranch() != null) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type mustn't have a branch"));
                }
            }
            if (tempWorkSchedule.getWorkingDayType().isItNeedHours()) {
                // if (tempWorkSchedule.getStartingTime() == null || tempWorkSchedule.getEndingTime() == null) {
                if (tempWorkSchedule.getStartingLDT() == null || tempWorkSchedule.getEndingLDT() == null) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type must have registered hours"));
                } else {
                    //if (tempWorkSchedule.getStartingTime().isAfter(tempWorkSchedule.getEndingTime())) {
                    if (tempWorkSchedule.getStartingLDT().isAfter(tempWorkSchedule.getEndingLDT())) {
                        errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                                tempWorkSchedule.getCollaborator().getUser().getUserName(), "The starting hour must be after the ending hour"));
                    }
                }
            } else {
                //if (tempWorkSchedule.getStartingTime() != null || tempWorkSchedule.getEndingTime() != null) {
                if (tempWorkSchedule.getStartingLDT() != null || tempWorkSchedule.getEndingLDT() != null) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type mustn't have registered hours"));
                }
            }
            // Validate opening and closing times
            if (tempWorkSchedule.getWorkingDayType().isItNeedBranches() && tempWorkSchedule.getWorkingDayType().isItNeedHours()) {
                //OpeningHours openingHours = utilities.getOpeningHoursByBranchAndDate(tempWorkSchedule.getBranch(), tempWorkSchedule.getLocalDate());
                //if (tempWorkSchedule.getStartingTime().isBefore(openingHours.getOpeningHour())) {
                OpeningHoursDetailed openingHoursDetailed = utilities.getOpeningHoursDetailedByBranchAndDate(tempWorkSchedule.getBranch(), tempWorkSchedule.getLocalDate());
                if (tempWorkSchedule.getStartingLDT().isBefore(openingHoursDetailed.getOpeningHour())) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type mustn't start before the opening hour"));
                }
                //if (tempWorkSchedule.getEndingTime().isAfter(openingHours.getClosingHour())) {
                if (tempWorkSchedule.getEndingLDT().isAfter(openingHoursDetailed.getClosingHour())) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, tempWorkSchedule.getLocalDate(),
                            tempWorkSchedule.getCollaborator().getUser().getUserName(), "The activity type mustn't end after the closing hour"));
                }
            }
        }
    }

    private void validateReceptionistAtClose() {
        for (LocalDate localDate = model.mondayOfTheWeek; localDate.isBefore(model.mondayOfTheWeek.plusDays(7)); localDate = localDate.plusDays(1)) {
            for (Branch branch : model.branches) {
                if (branch.getName().equals("Montejo")) {
                    continue;
                }
                boolean hasReceptionist = false;
                //OpeningHours openingHours = utilities.getOpeningHoursByBranchAndDate(branch, localDate);
                OpeningHoursDetailed openingHoursDetailed = utilities.getOpeningHoursDetailedByBranchAndDate(branch, localDate);
                for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                    if (workSchedule.getWorkingDayType().isItNeedBranches()) {
                        /*if (workSchedule.getBranch().equals(branch) && workSchedule.getLocalDate().equals(localDate) &&
                                workSchedule.getCollaborator().getJobPosition().equals(utilities.getJobPositionByName("Recepcionista"))
                                && workSchedule.getEndingTime().equals(openingHours.getClosingHour())) {*/
                        if (workSchedule.getBranch().equals(branch) && workSchedule.getLocalDate().equals(localDate) &&
                                workSchedule.getCollaborator().getJobPosition().equals(utilities.getJobPositionByName("Recepcionista"))
                                && workSchedule.getEndingLDT().equals(openingHoursDetailed.getClosingHour())) {
                            hasReceptionist = true;
                            break;
                        }
                    }
                }
                if (!hasReceptionist) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.WARNING, localDate,
                            null, "There is no receptionist at the closing hour"));
                }
            }
        }
    }

    public void validateHourlyIfThereIsAVetOrAsistA() {
        for (Branch branch : model.branches) {
            if (branch.getName().equals("Montejo")) {
                continue;
            }
            for (LocalDate localDate = model.mondayOfTheWeek; localDate.isBefore(model.mondayOfTheWeek.plusDays(7)); localDate = localDate.plusDays(1)) {
                List<LocalDateTime> availableHours = utilities.getAvailableHoursByDateAndBranch(localDate, branch);
                for (LocalDateTime localDateTime : availableHours) {
                    boolean isCovered = false;
                    for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                        if (workSchedule.getWorkingDayType().isItNeedBranches()) {
                            if (workSchedule.getBranch().equals(branch) && workSchedule.getLocalDate().equals(localDate)) {
                                JobPosition jobPosition = workSchedule.getCollaborator().getJobPosition();
                                if (jobPosition.equals(utilities.getJobPositionByName("Veterinario A"))
                                        || jobPosition.equals(utilities.getJobPositionByName("Veterinario B"))
                                        || jobPosition.equals(utilities.getJobPositionByName("Asistente A"))) {
                                    /*if ((workSchedule.getStartingTime().equals(localTime) || workSchedule.getStartingTime().isBefore(localTime))
                                            && workSchedule.getEndingTime().equals(localTime) || workSchedule.getEndingTime().isAfter(localTime)) {*/
                                    if ((workSchedule.getStartingLDT().equals(localDateTime) || workSchedule.getStartingLDT().isBefore(localDateTime))
                                            && workSchedule.getEndingLDT().equals(localDateTime) || workSchedule.getEndingLDT().isAfter(localDateTime)) {
                                        isCovered = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!isCovered) {
                        errors.add(new WorkScheduleError(WorkScheduleError.errorType.WARNING, localDate, null, "There is no VETA/VETB/ASISA at " + localDateTime));
                    }
                }
            }
        }
    }

    private void validateUniqueUsers() {
        HBox tempHBox;
        ChoiceBox<Collaborator> cboCollaborators;
        int counter;
        for (int col = 0; col < 7; col++) {
            for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
                counter = 0;
                for (GridPane branchGridPane : branchesGridPanes) {
                    for (int row = 1; row < branchGridPane.getRowCount(); row++) {
                        tempHBox = (HBox) utilities.getNodeFromGridPane(branchGridPane, col, row);
                        cboCollaborators = (ChoiceBox<Collaborator>) tempHBox.getChildren().get(0);
                        if (Objects.equals(cboCollaborators.getSelectionModel().getSelectedItem(), collaborator)) {
                            counter++;
                        }
                    }
                }
                if (counter > 1) {
                    errors.add(new WorkScheduleError(WorkScheduleError.errorType.ERROR, model.mondayOfTheWeek.plusDays(col),
                            collaborator.getUser().getUserName(), "has more than one register"));
                }
            }
        }
    }

    private void validateWithDataBase() {
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            for (WorkSchedule workSchedule : weekWorkSchedulesDB) {
                if ((workSchedule.getCollaborator().getId().equals(tempWorkSchedule.getCollaborator().getId())) &&
                        (workSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))) {
                    /*if ((!Objects.equals(workSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())) ||
                            (!Objects.equals(workSchedule.getStartingTime(), tempWorkSchedule.getStartingTime())) ||
                            (!Objects.equals(workSchedule.getEndingTime(), tempWorkSchedule.getEndingTime())) ||
                            (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {*/
                    if ((!Objects.equals(workSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())) ||
                            (!Objects.equals(workSchedule.getStartingLDT(), tempWorkSchedule.getStartingLDT())) ||
                            (!Objects.equals(workSchedule.getEndingLDT(), tempWorkSchedule.getEndingLDT())) ||
                            (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {
                        errors.add(new WorkScheduleError(WorkScheduleError.errorType.WARNING, tempWorkSchedule.getLocalDate(),
                                tempWorkSchedule.getCollaborator().getUser().getUserName(), "This date with this collaborator was already registered, it will be replaced to" + tempWorkSchedule));
                    }
                }
            }
        }
    }

    public void showHoursByDateAndBranch() {
        utilities.loadWindow("view/workSchedule/HoursByDateAndBranch.fxml", new Stage(), "Hours by date and branch", StageStyle.DECORATED, true, true);
    }

    public void showGraphic() {
        refresh();
        utilities.loadWindow("view/workSchedule/GraphicWorkSchedule.fxml", new Stage(), "Graphic view", StageStyle.DECORATED, true, true);
    }


    public boolean saveIntoDB() {
        validateData();
        if (hasErrors) {
            utilities.showAlert(Alert.AlertType.ERROR, "ERROR", "The work schedule can't be saved because it has errors");
            return false;
        }
        if (hasWarnings) {
            boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "The work schedule has warnings do you still want to save?");
            if (!answer) {
                return false;
            }
        }

        setWorkSchedulesToSaveOrUpdate();

        workScheduleDAO.updateWorkSchedules(workschedulesToUpdate);
        workScheduleDAO.saveWorkSchedules(workschedulesToSave);
        if (workschedulesToUpdate.isEmpty() && workschedulesToSave.isEmpty()) {
            utilities.showAlert(Alert.AlertType.INFORMATION, "Information", "There was nothing to save or update");
        } else {
            refreshVariables();
            utilities.showAlert(Alert.AlertType.INFORMATION, "Information", "The data was saved or updated succesfully");
            loadView();
        }
        return true;
    }

    private boolean setWorkSchedulesToSaveOrUpdate() {
        workschedulesToSave = new ArrayList<>();
        workschedulesToUpdate = new ArrayList<>();
        boolean isAllEmpty = true;

        WorkingDayType restWorkingDayType = utilities.getWorkingDayTypeByAbbr("DES");

        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getWorkingDayType() != restWorkingDayType) {
                isAllEmpty = false;
            }

            if (tempWorkSchedule.getId() == null) {
                workschedulesToSave.add(tempWorkSchedule);
            } else {
                for (WorkSchedule workSchedule : weekWorkSchedulesDB) {
                    if ((workSchedule.getCollaborator().getId().equals(tempWorkSchedule.getCollaborator().getId())) &&
                            (workSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))) {
                        // check if there is something different, in that case addid to the list
                        /*if ((!Objects.equals(workSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())) ||
                                (!Objects.equals(workSchedule.getStartingTime(), tempWorkSchedule.getStartingTime())) ||
                                (!Objects.equals(workSchedule.getEndingTime(), tempWorkSchedule.getEndingTime())) ||
                                (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {*/
                        if ((!Objects.equals(workSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())) ||
                                (!Objects.equals(workSchedule.getStartingLDT(), tempWorkSchedule.getStartingLDT())) ||
                                (!Objects.equals(workSchedule.getEndingLDT(), tempWorkSchedule.getEndingLDT())) ||
                                (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {
                            workschedulesToUpdate.add(tempWorkSchedule);
                        }
                        // If not, then go to next tempWorkSchedule
                        else {
                            break;
                        }
                    }
                }
            }
        }
        if (isAllEmpty) return false;
        return !workschedulesToSave.isEmpty() || !workschedulesToUpdate.isEmpty();
    }

    public void generateSnapshot() {
        WritableImage image = paneGridPanesContainer.snapshot(new SnapshotParameters(), null);
        File file = new File("res\\saves\\" + model.mondayOfTheWeek.toString() + " Calendar.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showCopyFromAnotherWeek() {
        utilities.loadWindow("view/workSchedule/CopyWorkSchedule.fxml", new Stage(), "Copy from Another Week", StageStyle.DECORATED, false, true);
        refreshVariables();
        loadView();
    }

    // todo

    public void emptyWeek() {
        boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Are you sure you want to delete this week data?");
        if (!answer) {
            return;
        }
        workScheduleDAO.deleteRegistersByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
        refreshVariables();
        loadView();
        utilities.showAlert(Alert.AlertType.INFORMATION, "SUCCESS", "The data was deleted successfully");


    }

    public void showIncident() {
        model.incidentType = Incident.incidentTypes.WORK_SCHEDULE;
        utilities.loadWindow("view/incident/Incident.fxml", new Stage(), "Create a new incident", StageStyle.DECORATED, false, true);
    }


    /*ACCESORY METHODS*/
    public void addCollaboratorRow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        GridPane gridPane = (GridPane) node.getParent();
        addRowsToGrid(gridPane, 1);
    }

    public void removeCollaboratorRow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        GridPane gridPane = (GridPane) node.getParent();

        int lastRow = gridPane.getRowCount();
        for (int col = 0; col < gridPane.getColumnCount(); col++) {
            Node nodeToRemove = utilities.getNodeFromGridPane(gridPane, col, lastRow - 1);
            gridPane.getChildren().remove(nodeToRemove);
        }
    }

    private void handleSelectUserBranchView(ChoiceBox<Collaborator> cboCollaborators) {
        HBox parent = (HBox) cboCollaborators.getParent();
        TextField txtStarting = (TextField) parent.getChildren().get(1);
        TextField txtEnding = (TextField) parent.getChildren().get(3);
        Collaborator collaborator = cboCollaborators.getSelectionModel().getSelectedItem();
        if (collaborator == null) {
            txtStarting.setText("");
            txtEnding.setText("");
        } else {
            if (txtStarting.getText().equals("") || (!txtEnding.getText().equals(""))) {
                // Get the branch and local date to retrieve the workSchedule
                Branch branch = null;
                GridPane grandParent = (GridPane) parent.getParent();
                if (grandParent.equals(gridPaneUrban)) branch = utilities.getBranchByName("Urban");
                else if (grandParent.equals(gridPaneHarbor)) branch = utilities.getBranchByName("Harbor");
                else if (grandParent.equals(gridPaneMontejo)) branch = utilities.getBranchByName("Montejo");
                int columnIndex = GridPane.getColumnIndex(parent);
                LocalDate workScheduleDate = model.mondayOfTheWeek.plusDays(columnIndex);
                // Get the openingHours
                OpeningHoursDetailed openingHoursDetailed = utilities.getOpeningHoursDetailedByBranchAndDate(branch, workScheduleDate);
                // set the info
                txtStarting.setText(String.valueOf(openingHoursDetailed.getOpeningHour().toLocalTime()));
                txtEnding.setText(String.valueOf(openingHoursDetailed.getClosingHour().toLocalTime()));
            }
        }
        validateHBoxBranchView(parent);
    }

    private void validateHBoxBranchView(HBox hBox) {
        @SuppressWarnings("unchecked")
        ChoiceBox<Collaborator> cboCollaborators = (ChoiceBox<Collaborator>) hBox.getChildren().get(0);
        TextField txtStartingTime = (TextField) hBox.getChildren().get(1);
        TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

        boolean paintRed = false;
        boolean paintGray = false;

        Collaborator collaborator = cboCollaborators.getSelectionModel().getSelectedItem();
        String inputStarting = txtStartingTime.getText();
        String inputEnding = txtEndingTime.getText();

        if (collaborator == null) {
            if (!inputStarting.equals("") || !inputEnding.equals("")) {
                paintRed = true;
            } else {
                paintGray = true;
            }
        } else {
            paintRed = inputStarting.equals("") || inputEnding.equals("");
            if (!inputStarting.equals("") && !inputEnding.equals("")) {
                try {
                    LocalTime startingTime = LocalTime.parse(inputStarting);
                    LocalTime endingTime = LocalTime.parse(inputEnding);
                    paintRed = startingTime.isAfter(endingTime) && endingTime.isAfter(LocalTime.of(6, 0));
                } catch (DateTimeParseException ignore) {

                }
            }
        }

        if (paintRed) {
            txtStartingTime.setStyle("-fx-background-color: red");
            txtEndingTime.setStyle("-fx-background-color: red");
        } else {
            txtStartingTime.setStyle("");
            txtEndingTime.setStyle("");
        }
        if (paintGray) {
            txtStartingTime.setStyle("-fx-background-color: lightgrey");
            txtEndingTime.setStyle("-fx-background-color: lightgrey");
        }
    }


    private void handleSelectWorkingDayTypeCollaboratorView(HBox hBox) {
        @SuppressWarnings("unchecked")
        ChoiceBox<WorkingDayType> cboWorkingDayType = (ChoiceBox<WorkingDayType>) hBox.getChildren().get(0);
        @SuppressWarnings("unchecked")
        ChoiceBox<Branch> cboBranch = (ChoiceBox<Branch>) hBox.getChildren().get(1);
        TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
        TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

        WorkingDayType workingDayType = cboWorkingDayType.getSelectionModel().getSelectedItem();

        if (workingDayType != null) {
            if (!workingDayType.isItNeedBranches()) {
                cboBranch.getSelectionModel().select(null);
            }
            if (!workingDayType.isItNeedHours()) {
                txtStartingTime.setText("");
                txtEndingTime.setText("");
            }
        }
        validateHBoxCollaboratorView(hBox);
    }

    private void handleSelectBranchCollaboratorView(HBox hBox) {
        // Retrieve all the HBOX fields
        @SuppressWarnings("unchecked")
        ChoiceBox<WorkingDayType> cboWorkingDayType = (ChoiceBox<WorkingDayType>) hBox.getChildren().get(0);
        @SuppressWarnings("unchecked")
        ChoiceBox<Branch> cboBranch = (ChoiceBox<Branch>) hBox.getChildren().get(1);
        TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
        TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        WorkingDayType workingDayType = cboWorkingDayType.getSelectionModel().getSelectedItem();

        // If a branch is selected
        if (branch != null) {
            // If none wdt is selected, then select "ORD".
            if (workingDayType == null) {
                cboWorkingDayType.getSelectionModel().select(utilities.getWorkingDayTypeByAbbr("ORD"));
                workingDayType = utilities.getWorkingDayTypeByAbbr("ORD");
            }
            // If a branch is selected and the wdt doesn't need a branch, set wdt to DES
            if (!workingDayType.isItNeedBranches()) {
                cboWorkingDayType.getSelectionModel().select(utilities.getWorkingDayTypeByAbbr("ORD"));
            }
            if (txtStartingTime.getText().equals("") || txtEndingTime.getText().equals("")) {
                int columnIndex = GridPane.getColumnIndex(hBox);
                LocalDate workScheduleDate = model.mondayOfTheWeek.plusDays(columnIndex - 1);
                // Get the openingHours
                OpeningHoursDetailed openingHoursDetailed = utilities.getOpeningHoursDetailedByBranchAndDate(branch, workScheduleDate);
                // set the info
                txtStartingTime.setText(String.valueOf(openingHoursDetailed.getOpeningHour().toLocalTime()));
                txtEndingTime.setText(String.valueOf(openingHoursDetailed.getClosingHour().toLocalTime()));
            }
            // If none branch is selected
        } else {
            if (workingDayType != null) {
                if (workingDayType.isItNeedBranches()) {
                    cboWorkingDayType.getSelectionModel().select(utilities.getWorkingDayTypeByAbbr("DES"));
                }
            }
        }
        validateHBoxCollaboratorView(hBox);
    }

    private void validateHBoxCollaboratorView(HBox hBox) {
        @SuppressWarnings("unchecked")
        ChoiceBox<WorkingDayType> cboWorkingDayType = (ChoiceBox<WorkingDayType>) hBox.getChildren().get(0);
        @SuppressWarnings("unchecked")
        ChoiceBox<Branch> cboBranch = (ChoiceBox<Branch>) hBox.getChildren().get(1);
        TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
        TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

        boolean paintRed = false;
        boolean paintGrey = false;
        WorkingDayType workingDayType = cboWorkingDayType.getSelectionModel().getSelectedItem();
        Branch branch = cboBranch.getSelectionModel().getSelectedItem();
        String inputStarting = txtStartingTime.getText();
        String inputEnding = txtEndingTime.getText();

        if (txtStartingTime.getText().equals("") || txtEndingTime.getText().equals("")) {
            if (workingDayType == null) {
                workingDayType = utilities.getWorkingDayTypeByAbbr("ORD");
                cboWorkingDayType.getSelectionModel().select(utilities.getWorkingDayTypeByAbbr("ORD"));
            }
        }

        if (workingDayType.isItNeedBranches()) {
            if (branch == null) {
                paintRed = true;
            }
        }
        if (workingDayType.isItNeedHours()) {
            if (inputStarting.equals("") || (inputEnding.equals(""))) {
                paintRed = true;
            } else {
                try {
                    LocalTime startingTime = LocalTime.parse(inputStarting);
                    LocalTime endingTime = LocalTime.parse(inputEnding);
                    if (startingTime.isAfter(endingTime) && endingTime.isAfter(LocalTime.of(6, 0))) {
                        paintRed = true;
                    }
                } catch (DateTimeParseException ignore) {
                }
            }
        } else {
            if (inputStarting.equals("") || (inputEnding.equals(""))) {
                paintGrey = true;
            }
        }

        if (paintRed) {
            txtStartingTime.setStyle("-fx-background-color: red; -fx-font-size: 9");
            txtEndingTime.setStyle("-fx-background-color: red; -fx-font-size: 9");
        } else {
            txtStartingTime.setStyle("");
            txtEndingTime.setStyle("");
            txtStartingTime.setStyle("-fx-font-size: 9");
            txtEndingTime.setStyle("-fx-font-size: 9");
        }

        if (paintGrey) {
            txtStartingTime.setStyle("-fx-background-color: lightgrey; -fx-font-size: 9");
            txtEndingTime.setStyle("-fx-background-color: lightgrey; -fx-font-size: 9");
        }
    }


    private WorkSchedule getWorkScheduleFromWorkSchedules(WorkSchedule workSchedule) {
        for (WorkSchedule tempworkSchedule : model.tempWorkSchedules) {
            if (tempworkSchedule.getCollaborator().getId().equals(workSchedule.getCollaborator().getId()) &&
                    tempworkSchedule.getLocalDate().equals(workSchedule.getLocalDate())) {
                workSchedule = tempworkSchedule;
            }
        }
        return workSchedule;
    }

    private void addOrUpdateTempWorkSchedules(WorkSchedule tempWorkSchedule) {
        if (model.tempWorkSchedules.contains(tempWorkSchedule)) {
            int index = model.tempWorkSchedules.indexOf(tempWorkSchedule);
            model.tempWorkSchedules.set(index, tempWorkSchedule);
        } else {
            model.tempWorkSchedules.add(tempWorkSchedule);
        }
    }

    public GridPane getGridPaneByBranchName(String branchName, GridPane gridPaneUrban, GridPane
            gridPaneHarbor, GridPane gridPaneMontejo) {
        GridPane gridPane = null;
        switch (branchName) {
            case "Urban":
                gridPane = gridPaneUrban;
                break;
            case "Harbor":
                gridPane = gridPaneHarbor;
                break;
            case "Montejo":
                gridPane = gridPaneMontejo;
                break;
        }
        return gridPane;
    }
}
