package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.entity.WorkingDayType;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class workScheduleController implements Initializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneRest;
    public GridPane gridPaneHarbor;
    public AnchorPane rootPane;
    public Label lblConnectionStatus;
    public DatePicker datePicker;
    public VBox paneGridPanesContainer;
    public ChoiceBox<views> cboViewSelector;
    private GridPane gridPaneViewCollaborators;
    private Model model;
    private Utilities utilities;
    private WorkScheduleDAO workScheduleDAO;
    private List<WorkSchedule> workSchedulesDB;
    String errorList;
    String warningList;
    private boolean hasErrors = false;
    private boolean hasWarnings = false;
    private LocalTime startingToRun; // todo delete is just to watch the time to run
    private LocalTime endingRun; // todo delete is just to watch the time to run

    private enum views {BRANCH_VIEW, COLLABORATOR_VIEW, GRAPHIC_VIEW}

    private List<GridPane> branchesGridPanes;
    private views selectedView;
    private boolean isFirstLoadFinished;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initInstances();
        initUnmutableVariables();
        loadComboBoxes();
        initVariables();
        // Loading data from database
        loadView();
    }


    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
    }

    private void initUnmutableVariables() {
        model.activeAndWorkersuserNamesAndNull = UserDAO.getInstance().getObservableListOfActiveAndWorkersUserNames();
        model.activeAndWorkersuserNamesAndNull.add(null);
        model.activeAndWorkersUserNames = UserDAO.getInstance().getActiveAndWorkersUserNames();
    }

    private void loadComboBoxes() {
        cboViewSelector.getItems().setAll(views.values());
    }

    // init variables and instances
    private void initVariables() {
        selectedView = views.BRANCH_VIEW;
        branchesGridPanes = new ArrayList<>(Arrays.asList(gridPaneUrban, gridPaneHarbor, gridPaneMontejo));
        model.activeAndWorkerCollaborators = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        refreshVariables();
        cboViewSelector.getSelectionModel().select(views.BRANCH_VIEW);
        isFirstLoadFinished = true;
    }

    private void refreshVariables() {
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        workSchedulesDB = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
        model.tempWorkSchedules = new ArrayList<>(workSchedulesDB);
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            tempWorkSchedule.setId(0);
        }
    }

    //
/*    private void startRunnablesWithNoUrgentData() {
        Runnable runnable = () -> {
            model.workSchedulesOfTheWeek = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lblConnectionStatus.setText("You can register new data");
                    lblConnectionStatus.setStyle("-fx-background-color: lawngreen");
                }
            });
        };
        new Thread(runnable).start();

        loadWorkers();
    }*/


    private void loadView() {
        if (selectedView == views.BRANCH_VIEW) {
            loadBranchView();
            if (!model.tempWorkSchedules.isEmpty()) loadDatabaseViewBranch();
        } else if (selectedView == views.COLLABORATOR_VIEW) {
            loadCollaboratorsView();
        }
    }

    // Adds a row to a calendar
    // todo maybe it can be a utility method
    // for each textfield created add a changelistener to validate hour value
    // load the header
    private void loadBranchView() {
        paneGridPanesContainer.getChildren().clear();
        paneGridPanesContainer.getChildren().add(gridPaneHeader);
        paneGridPanesContainer.getChildren().addAll(branchesGridPanes);
        paneGridPanesContainer.getChildren().add(gridPaneRest);
        for (GridPane branchGridPane : branchesGridPanes) {
            utilities.clearGridPaneChildren(branchGridPane, 0, 1);
        }

        // todo gridpanerest
        utilities.clearGridPaneChildren(gridPaneRest, 0, 1);
        addRowsToGrid(gridPaneUrban, 5, true);
        addRowsToGrid(gridPaneHarbor, 4, true);
        addRowsToGrid(gridPaneMontejo, 1, true);
        addGridPanesToRestPane();
        loadCalendarHeader(gridPaneHeader);
        loadCalendarDaysHeader(gridPaneHeader, 0);
    }

    private void addRowsToGrid(GridPane gridPaneBranch, int rowsToAdd, boolean setDefaultHour) {
        int rows = gridPaneBranch.getRowCount();
        HBox tempHBox;
        for (int i = 0; i < rowsToAdd; i++) {
            for (int j = 0; j < gridPaneBranch.getColumnCount(); j++) {
                tempHBox = new HBox();
                gridPaneBranch.add(tempHBox, j, rows + i);
                // combobox for users
                ChoiceBox<String> cboUsers = new ChoiceBox<>();
                cboUsers.setStyle("-fx-font-size:11");
                cboUsers.setMaxHeight(Double.MAX_VALUE);

                ObservableList<String> cboOptions = model.activeAndWorkersuserNamesAndNull;
                cboUsers.setItems(cboOptions);
                TextField txtStartingTime = new TextField();

                txtStartingTime.setPrefWidth((50));
                addChangeListenerToTimeField(txtStartingTime);


                Label label = new Label("-");
                // spinner hour
                TextField txtEndingTime = new TextField();
                txtEndingTime.setPrefWidth(50);
                addChangeListenerToTimeField(txtEndingTime);

                addChangeListenerToValidateBranchView(cboUsers, txtStartingTime, txtEndingTime);
                if (setDefaultHour) {
                    txtStartingTime.setText("09:00");
                    txtEndingTime.setText("21:00");
                }
                tempHBox.getChildren().addAll(cboUsers, txtStartingTime, label, txtEndingTime);
            }
        }
    }


    // todo gridpanerest
    private void addGridPanesToRestPane() {
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
    public void loadCollaboratorsView() {
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
        for (WorkSchedule workSchedule : model.tempWorkSchedules) {
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
            cboWorkingDayType.getItems().addAll(model.workingDayTypesAbbr);
            cboWorkingDayType.setId("choiceBoxFont10");

            ChoiceBox<String> cboBranch = new ChoiceBox<>();
            cboBranch.setPrefWidth(60);
            cboBranch.getItems().addAll(model.branchesNamesAndNone);
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

            cboWorkingDayType.getSelectionModel().select(workSchedule.getWorkingDayType().getAbbr());
            // todo branchChanges DONE
            if (workSchedule.getBranch() != null) {
                cboBranch.getSelectionModel().select(workSchedule.getBranch().getName());
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

    // Load the database, clearing the grids, calculating rows need it
    public void loadDataBase() {
        refreshVariables();
        loadView();
    }


    public void loadDatabaseViewBranch() {
        // todo call another method called clearAndAddGrids
        for (GridPane gridPane : branchesGridPanes) {
            utilities.clearGridPaneChildren(gridPane, 0, 1);
        }
        utilities.clearGridPanesNodesChildren(gridPaneRest, 0, 1);

        // todo maybe create a method instead of repeating
        int rowsNeededUrban = 0;
        int rowsNeededTheHarbor = 0;
        int rowsNeededMontejo = 0;
        for (LocalDate localDate = model.mondayOfTheWeek; localDate.isBefore(model.mondayOfTheWeek.plusDays(7)); localDate = localDate.plusDays(1)) {
            int tempRowsNeededUrban = 0;
            int tempRowsNeededTheHarbor = 0;
            int tempRowsNeededMontejo = 0;
            for (WorkSchedule workSchedule : workSchedulesDB) {
                // todo wdt change DONE
                if (workSchedule.getLocalDate().equals(localDate) && (workSchedule.getWorkingDayType().getItNeedBranches())) {
                    // todo branchChanges DONE
                    String workScheduleBranch = workSchedule.getBranch().getName();
                    if (workScheduleBranch.equals("Urban")) {
                        tempRowsNeededUrban += 1;
                    }
                    if (workScheduleBranch.equals("Harbor")) {
                        tempRowsNeededTheHarbor += 1;
                    }
                    if (workScheduleBranch.equals("Montejo")) {
                        tempRowsNeededMontejo += 1;
                    }
                }
            }
            rowsNeededUrban = Math.max(rowsNeededUrban, tempRowsNeededUrban);
            rowsNeededTheHarbor = Math.max(rowsNeededTheHarbor, tempRowsNeededTheHarbor);
            rowsNeededMontejo = Math.max(rowsNeededMontejo, tempRowsNeededMontejo);

        }
        addRowsToGrid(gridPaneUrban, rowsNeededUrban, false);
        addRowsToGrid(gridPaneHarbor, rowsNeededTheHarbor, false);
        addRowsToGrid(gridPaneMontejo, rowsNeededMontejo, false
        );

        for (WorkSchedule workSchedule : workSchedulesDB) {
            // todo wdt change DONE

            if (workSchedule.getWorkingDayType().getItNeedBranches()) {
                GridPane gridPane = null;
                int col;
                // int row;
                // todo branchChanges DONE
                switch (workSchedule.getBranch().getName()) {
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
                col = (int) ChronoUnit.DAYS.between(model.mondayOfTheWeek, workSchedule.getLocalDate());

                for (int row = 1; row < Objects.requireNonNull(gridPane).getRowCount(); row++) {
                    Node node = utilities.getNodeFromGridPane(gridPane, col, row);
                    HBox hBox = (HBox) node;
                    ChoiceBox<String> choiceBox = (ChoiceBox<String>) hBox.getChildren().get(0);
                    if (choiceBox.getSelectionModel().getSelectedItem() == null) {
                        choiceBox.getSelectionModel().select(workSchedule.getCollaborator().getUser().getUserName());
                        ((TextField) hBox.getChildren().get(1)).setText(String.valueOf(workSchedule.getStartingTime()));
                        ((TextField) hBox.getChildren().get(3)).setText(String.valueOf(workSchedule.getEndingTime()));
                        break;
                    }
                }
            }
        }
        insertRestLabels();
    }

    public void refreshAndValidateData() {
        if (selectedView == views.BRANCH_VIEW) {
            if (!model.tempWorkSchedules.isEmpty()) changeCollaboratorsWithoutRegister();
            for (GridPane branchGridPane : branchesGridPanes) {
                retrieveDataFromPane(branchGridPane);
            }
        } else if (selectedView == views.COLLABORATOR_VIEW) {
            retrieveDataFromCollaboratorPane();
        }


        hasWarnings = false;
        hasErrors = false;
        errorList = "\nERRORS (It prevents from saving)";
        warningList = "\nWARNINGS (It doesn't prevent from saving)";


        if (selectedView == views.BRANCH_VIEW) {
            generateRestDays();
            insertRestLabels();
        }
        if (selectedView == views.BRANCH_VIEW) validateUniqueUsers();
        validateInternally();
        validateWithDataBase();

        if (hasErrors) {
            if (hasWarnings) {
                utilities.showAlert(Alert.AlertType.ERROR, "Error", errorList + warningList);
            } else {
                utilities.showAlert(Alert.AlertType.ERROR, "Error", errorList);
            }
        } else {
            if (hasWarnings) {
                utilities.showAlert(Alert.AlertType.WARNING, "Error", warningList);
            }
        }
    }

    private void changeCollaboratorsWithoutRegister() {
        HBox tempHBox;
        ChoiceBox<String> cboUsers;
        int counter;
        boolean isRegistered = false;

        // Loop for each day
        for (int col = 0; col < 7; col++) {
            //made a list with of workschedules that has a branch in that day
            List<WorkSchedule> tempWorkSchedulesWithBranch = new ArrayList<>();
            for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
                // todo wdt change DONE
                if (tempWorkSchedule.getWorkingDayType().getItNeedBranches() &&
                        model.mondayOfTheWeek.plusDays(col).equals(tempWorkSchedule.getLocalDate())) {
                    tempWorkSchedulesWithBranch.add(tempWorkSchedule);
                }
            }
            //check if it has a selection in a combobox
            for (WorkSchedule tempWorkScheduleWithBranch : tempWorkSchedulesWithBranch) {
                for (GridPane branchGridPane : branchesGridPanes) {
                    for (int row = 1; row < branchGridPane.getRowCount(); row++) {
                        tempHBox = (HBox) utilities.getNodeFromGridPane(branchGridPane, col, row);
                        cboUsers = (ChoiceBox<String>) tempHBox.getChildren().get(0);
                        String selectedUserName = cboUsers.getSelectionModel().getSelectedItem();
                        String tempWorkScheduleUserName = tempWorkScheduleWithBranch.getCollaborator().getUser().getUserName();
                        if (Objects.equals(selectedUserName, tempWorkScheduleUserName)) {
                            isRegistered = true;
                            break;
                        }
                    }
                    if (isRegistered) break;
                }
                //if none, change it to rest
                if (!isRegistered) {
                    // todo wdt change DONE
                    tempWorkScheduleWithBranch.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                    // todo branchChanges DONE
                    tempWorkScheduleWithBranch.setBranch(null);
                    tempWorkScheduleWithBranch.setRegisteredBy(model.loggedUser.getCollaborator());
                    tempWorkScheduleWithBranch.setStartingTime(null);
                    tempWorkScheduleWithBranch.setEndingTime(null);
                    addOrUpdateTempWorkSchedules(tempWorkScheduleWithBranch);
                }
            }
        }
    }


    // It adds to the tempWorkSchedules arrayList each of the registered workSchedule
    private void retrieveDataFromPane(GridPane gridPane) {
        // Loop for each column (each day)
        for (int col = 0; col < gridPane.getColumnCount(); col++) {
            LocalDate date = model.mondayOfTheWeek.plusDays(col);
            // Loop for each row of each day
            for (int row = 1; row < gridPane.getRowCount(); row++) {
                HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPane, col, row);
                ChoiceBox<String> choiceBox = (ChoiceBox<String>) hBox.getChildren().get(0);
                TextField txtStartingTime = (TextField) hBox.getChildren().get(1);
                TextField txtEndingTime = (TextField) hBox.getChildren().get(3);

                if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                    WorkSchedule workSchedule = new WorkSchedule();
                    // common setters
                    workSchedule.setLocalDate(date);
                    for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
                        if (collaborator.getUser().getUserName().equals(choiceBox.getSelectionModel().getSelectedItem())) {
                            workSchedule.setCollaborator(collaborator);
                            break;
                        }
                    }
                    workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());

                    if (txtStartingTime.getText().equals("") || txtEndingTime.getText().equals("")) {
                        // todo branchChanges DONE
                        workSchedule.setBranch(null);
                        workSchedule.setStartingTime(null);
                        workSchedule.setEndingTime(null);
                        // todo wdt change
                        workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                    } else {
                        // todo branchChanges DONE
                        Branch branch = utilities.getBranchByName(((Label) gridPane.getChildren().get(0)).getText());
                        workSchedule.setBranch(branch);

                        workSchedule.setStartingTime(LocalTime.parse((((TextField) hBox.getChildren().get(1)).getText())));
                        workSchedule.setEndingTime(LocalTime.parse((((TextField) hBox.getChildren().get(3)).getText())));
                        // todo wdt change DONE
                        workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("ORD"));
                    }
                    addOrUpdateTempWorkSchedules(workSchedule);
                }
            }
        }
    }

    private void retrieveDataFromCollaboratorPane() {
        // Loop for each column (each day)
        for (int col = 1; col < gridPaneViewCollaborators.getColumnCount(); col++) {
            LocalDate date = model.mondayOfTheWeek.plusDays(col - 1);
            // Loop for each row of each day
            for (int row = 2; row < gridPaneViewCollaborators.getRowCount(); row++) {
                HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPaneViewCollaborators, col, row);
                WorkSchedule workSchedule = new WorkSchedule();
                workSchedule.setLocalDate(date);
                workSchedule.setCollaborator(model.activeAndWorkerCollaborators.get(row - 2));

                ChoiceBox<String> cboWorkingDayType = (ChoiceBox<String>) hBox.getChildren().get(0);
                if (cboWorkingDayType.getSelectionModel().getSelectedItem() != null) {
                    // todo wdt change
                    workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr(cboWorkingDayType.getSelectionModel().getSelectedItem()));
                } else {
                    // todo wdt change
                    workSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                }
                ChoiceBox<String> cboBranch = (ChoiceBox<String>) hBox.getChildren().get(1);
                // todo branchChanges DONE
                Branch branch = utilities.getBranchByName(cboBranch.getSelectionModel().getSelectedItem());
                workSchedule.setBranch(branch);


                TextField txtStartingTime = (TextField) hBox.getChildren().get(2);
                if (txtStartingTime.getText().equals("")) {
                    workSchedule.setStartingTime(null);
                } else {
                    workSchedule.setStartingTime(LocalTime.parse(txtStartingTime.getText()));
                }
                TextField txtEndingTime = (TextField) hBox.getChildren().get(3);
                if (txtEndingTime.getText().equals("")) {
                    workSchedule.setEndingTime(null);
                } else {
                    workSchedule.setEndingTime(LocalTime.parse(txtEndingTime.getText()));
                }
                workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                addOrUpdateTempWorkSchedules(workSchedule);
            }
        }
    }

    private void generateRestDays() {
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            for (int i = 0; i < 7; i++) {
                boolean registerRest = false;
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
                    // todo wdt change
                    restWorkSchedule.setWorkingDayType(utilities.getWorkingDayTypeByAbbr("DES"));
                    // todo branchChanges DONE
                    restWorkSchedule.setBranch(null);

                    restWorkSchedule.setLocalDate(localDate);
                    restWorkSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    addOrUpdateTempWorkSchedules(restWorkSchedule);
                }
            }
        }
    }

    private void insertRestLabels() {
        GridPane internalGridPane;
        LocalDate localDate = model.mondayOfTheWeek;

        // todo gridpanerest
        for (int i = 0; i < gridPaneRest.getColumnCount(); i++) {
            int col = 0;
            int row = 0;
            localDate = model.mondayOfTheWeek.plusDays(i);
            // todo gridpanerest
            internalGridPane = (GridPane) utilities.getNodeFromGridPane(gridPaneRest, i, 1);
            internalGridPane.getChildren().clear();


            for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                // todo branchChanges NOT DONE
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getBranch() == null) {
                    if (col == 2) {
                        col = 0;
                        row++;
                    }
                    Label label = new Label(workSchedule.getCollaborator().getUser().getUserName());
                    internalGridPane.add(label, col, row);
                    col++;
                }
            }
        }
    }

    private void validateInternally() {
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            double totalTimeWorkedPerCollaborator = 0;
            // loop
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                int registerPerCollaboratorPerDay = 0;
                for (WorkSchedule workSchedule : model.tempWorkSchedules) {
                    if ((workSchedule.getCollaborator().equals(collaborator)) && (workSchedule.getLocalDate().equals(localDate))) {
                        registerPerCollaboratorPerDay++;
                        if (workSchedule.getStartingTime() != null && workSchedule.getEndingTime() != null) {
                            totalTimeWorkedPerCollaborator += ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime()) / 60.0;
                        }
                    }
                }
                if (registerPerCollaboratorPerDay > 1) {
                    errorList += "\nThe collaborator: " + collaborator.getUser().getUserName() + " has " +
                            registerPerCollaboratorPerDay + " registers in: " + localDate;
                    hasErrors = true;
                }
            }

            if (totalTimeWorkedPerCollaborator != collaborator.getWorkingConditions().getWeeklyWorkingHours()) {
                warningList += "\nThe collaborator: " + collaborator.getUser().getUserName() + " has " +
                        collaborator.getWorkingConditions().getWeeklyWorkingHours() + " weekly working hours. And you are trying to register " + totalTimeWorkedPerCollaborator;
                hasWarnings = true;
            }
        }

        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            // todo wdt change DONE
            if (tempWorkSchedule.getWorkingDayType().getItNeedBranches()) {
                // todo branchChanges DONE
                if (tempWorkSchedule.getBranch() == null) {
                    errorList += "\n The activity type can't have none branch";
                    hasErrors = true;
                }
            } else {
                // todo branchChanges DONE
                if (tempWorkSchedule.getBranch() != null) {
                    errorList += "\n The activity type mustn't have a branch";
                    hasErrors = true;
                }
            }
            // todo wdt change DONE
            if (tempWorkSchedule.getWorkingDayType().getItNeedHours()) {
                if (tempWorkSchedule.getStartingTime() == null || tempWorkSchedule.getEndingTime() == null) {
                    errorList += "\n The activity type must have registered hours";
                    hasErrors = true;
                } else {
                    if (tempWorkSchedule.getStartingTime().isAfter(tempWorkSchedule.getEndingTime())) {
                        errorList += "\n The starting hour must be after the ending hour";
                        hasErrors = true;
                    }
                }
            } else {
                if (tempWorkSchedule.getStartingTime() != null || tempWorkSchedule.getEndingTime() != null) {
                    errorList += "\n The activity type mustn't have registered hours";
                    hasErrors = true;
                }
            }
        }
    }

    private void validateUniqueUsers() {
        HBox tempHBox;
        ChoiceBox<String> cboUsers;
        int counter;
        for (int col = 0; col < 7; col++) {
            for (String userName : model.activeAndWorkersUserNames) {
                counter = 0;
                for (GridPane branchGridPane : branchesGridPanes) {
                    for (int row = 1; row < branchGridPane.getRowCount(); row++) {
                        tempHBox = (HBox) utilities.getNodeFromGridPane(branchGridPane, col, row);
                        cboUsers = (ChoiceBox<String>) tempHBox.getChildren().get(0);
                        if (Objects.equals(cboUsers.getSelectionModel().getSelectedItem(), userName)) {
                            counter++;
                        }
                    }
                }
                if (counter > 1) {
                    errorList += userName + "\n has more than one register, in " + model.mondayOfTheWeek.plusDays(col);
                    hasErrors = true;
                }
            }
        }
    }

    private void validateWithDataBase() {
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            for (WorkSchedule workSchedule : workSchedulesDB) {
                if ((workSchedule.getCollaborator().getId() == (tempWorkSchedule.getCollaborator().getId())) &&
                        (workSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))) {
                    // todo check because of getStartingTime returns null

                    // todo wdt change DONE
                    if ((!Objects.equals(workSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())) ||
                            (!Objects.equals(workSchedule.getStartingTime(), tempWorkSchedule.getStartingTime())) ||
                            (!Objects.equals(workSchedule.getEndingTime(), tempWorkSchedule.getEndingTime())) ||
                            // todo branchChanges
                            (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {
                        warningList += "\nThis date with this collaborator was already registered, it will be replaced: " + workSchedule.getCollaborator().getUser().getUserName() + " " + workSchedule.getLocalDate();
                    }
                }
            }
        }

    }


    /*
     BUTTONS ON ACTION
     */

    public void changeDateView() {
        Model.getInstance().selectedLocalDate = datePicker.getValue();
        refreshVariables();
        loadView();
    }
    // It validates the inserted data and if there are no errors, insert it to the database


    public void saveIntoDB() {
        refreshAndValidateData();
        if (hasWarnings) {
            boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "The work schedule has warnings do you still want to save?");
            if (!answer) {
                return;
            }
        }
        workScheduleDAO.createOrReplaceRegisters(model.tempWorkSchedules);
        refreshVariables();
        loadView();
    }

    public void showCopyFromAnotherWeek() {
        utilities.loadWindow("view/workSchedule/copyWorkSchedule.fxml", new Stage(), "Copy from Another Week", StageStyle.DECORATED, false, true);
    }

    public void changeView() {
        if (isFirstLoadFinished) {
            boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "You are going to change view, the unsaved data may be lost \n¿Do you want to save first?");
            if (answer) {
                saveIntoDB();
            }
            selectedView = cboViewSelector.getValue();
            loadView();
        }
    }

    public void addCollaboratorRow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        GridPane gridPane = (GridPane) node.getParent();
        addRowsToGrid(gridPane, 1, true);
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

    private void addChangeListenerToTimeField(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String inputText = textField.getText();
                try {
                    if (inputText.equals("")) {
                        return;
                    }
                    if (inputText.length() <= 2) {
                        Integer.parseInt(inputText);
                        if (inputText.length() == 1) inputText = "0" + inputText;
                        textField.setText(inputText + ":00");
                    } else {
                        LocalTime.parse(inputText);
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    utilities.showAlert(Alert.AlertType.ERROR, "Time format error", "The hour format is incorrect, it has to be like 10:00 or just the hour: 12");
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
    }

    private void addChangeListenerToValidateBranchView(ChoiceBox<String> cboUsers, TextField txtStartingTime, TextField txtEndingTime) {
        ChangeListener changeListener = (observable, oldValue, newValue) -> {
            boolean paintRed;
            String selectedUser = cboUsers.getSelectionModel().getSelectedItem();
            String inputStarting = txtStartingTime.getText();
            String inputEnding = txtEndingTime.getText();

            if (selectedUser == null) {
                paintRed = !inputStarting.equals("") || !inputEnding.equals("");
            } else {
                paintRed = inputStarting.equals("") || inputEnding.equals("");
                if (!inputStarting.equals("") && !inputEnding.equals("")) {
                    try {
                        LocalTime startingTime = LocalTime.parse(inputStarting);
                        LocalTime endingTime = LocalTime.parse(inputEnding);
                        paintRed = startingTime.isAfter(endingTime);
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

            if (observable.equals(cboUsers.valueProperty())) {
                if (newValue == null) {
                    txtStartingTime.setText("");
                    txtEndingTime.setText("");
                }
            }
        };

        cboUsers.valueProperty().addListener(changeListener);
        txtStartingTime.textProperty().addListener(changeListener);
        txtEndingTime.textProperty().addListener(changeListener);
    }

    // todo
    private void addChangeListenerToValidateCollaboratorView(ChoiceBox<String> cboWorkingDayType, ChoiceBox<String> cboBranchs, TextField txtStartingTime, TextField txtEndingTime) {
        ChangeListener changeListener = (observable, oldValue, newValue) -> {
            boolean paintRed = false;
            WorkingDayType workingDayType = utilities.getWorkingDayTypeByAbbr(cboWorkingDayType.getSelectionModel().getSelectedItem());

            // String activityWorkingType = cboWorkingDayType.getSelectionModel().getSelectedItem();
            // todo branchChanges
            String branch = cboBranchs.getSelectionModel().getSelectedItem();
            String inputStarting = txtStartingTime.getText();
            String inputEnding = txtEndingTime.getText();

            if (branch != null) {
                // todo wdt change DONE
                if (workingDayType.getItNeedBranches()) {
                    // todo branchChanges
                    if (branch.equals("None")) {
                        paintRed = true;
                    }
                } else {
                    // todo branchChanges
                    if (!branch.equals("None")) {
                        paintRed = true;
                    }
                }
                // todo wdt change DONE
                if (workingDayType.getItNeedHours()) {
                    if (inputStarting.equals("") || (inputEnding.equals(""))) {
                        paintRed = true;
                    } else {
                        try {
                            LocalTime startingTime = LocalTime.parse(inputStarting);
                            LocalTime endingTime = LocalTime.parse(inputEnding);
                            if (startingTime.isAfter(endingTime)) {
                                paintRed = true;
                            }
                        } catch (DateTimeParseException ignore) {
                        }
                    }
                } else {
                    if (!inputStarting.equals("") || (!inputEnding.equals(""))) {
                        paintRed = true;
                    }
                }
            }


            if (paintRed) {
                txtStartingTime.setStyle("-fx-background-color: red");
                txtEndingTime.setStyle("-fx-background-color: red");
            } else {
                txtStartingTime.setStyle("");
                txtStartingTime.setStyle("-fx-font-size: 10");
                txtEndingTime.setStyle("");
                txtEndingTime.setStyle("-fx-font-size: 10");
            }
            // todo wdt change DONE
            if (observable.equals(cboWorkingDayType.valueProperty())) {
                // todo wdt change
                if (!workingDayType.getItNeedHours()) {
                    txtStartingTime.setText("");
                    txtEndingTime.setText("");
                }
                // todo wdt change
                if (!workingDayType.getItNeedBranches()) {
                    cboBranchs.getSelectionModel().select("None");
                }
            }
        };
        cboBranchs.valueProperty().addListener(changeListener);
        cboWorkingDayType.valueProperty().addListener(changeListener);
        txtStartingTime.textProperty().addListener(changeListener);
        txtEndingTime.textProperty().addListener(changeListener);
    }


    private void addOrUpdateTempWorkSchedules(WorkSchedule tempWorkSchedule) {
        if (model.tempWorkSchedules.contains(tempWorkSchedule)) {
            int index = model.tempWorkSchedules.indexOf(tempWorkSchedule);
            model.tempWorkSchedules.set(index, tempWorkSchedule);
        } else {
            model.tempWorkSchedules.add(tempWorkSchedule);
        }
    }

    public void testMyThings() {
        System.out.println("TESTTT" + gridPaneRest.getHeight());
    }

    public void showGraphic() {
        utilities.loadWindow("view/workSchedule/graphicWorkSchedule.fxml", new Stage(), "Graphic view", StageStyle.DECORATED, true, true);
    }

}
