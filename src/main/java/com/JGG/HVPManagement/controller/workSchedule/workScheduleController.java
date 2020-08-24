package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private List<WorkSchedule> tempWorkSchedules;
    String errorList;
    String warningList;
    private boolean hasErrors = false;
    private boolean hasWarnings = false;

    private enum views {BRANCH_VIEW, COLLABORATOR_VIEW, GRAPHIC_VIEW}

    ;
    private views selectedView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedView = views.BRANCH_VIEW;
        System.out.println("STARTING init" + LocalTime.now());
        initInstances();
        initUnmutableVariables();
        loadComboBoxes();
        System.out.println("STARTING initvariables" + LocalTime.now());
        initVariables();
        // Loading data from database
        System.out.println("STARTING initgrids" + LocalTime.now());
        initGrids();
        System.out.println("STARTING loadgrids" + LocalTime.now());
        loadBranchView();
        System.out.println("FINISHED ALL" + LocalTime.now());
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
        System.out.println("STARTING Activeandworkercollaborators" + LocalTime.now());
        model.activeAndWorkerCollaborators = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        System.out.println("ENDED Activeandworkercollaborators" + LocalTime.now());
        refreshVariables();
    }

    private void refreshVariables() {
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        System.out.println("STARTING workschedules" + LocalTime.now());
        workSchedulesDB = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
        tempWorkSchedules = new ArrayList<>(workSchedulesDB);
        for(WorkSchedule tempWorkSchedule:tempWorkSchedules){
            tempWorkSchedule.setId(0);
        }
        System.out.println("ENDING workschedules" + LocalTime.now());
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


    private void initGrids() {
        addRowsToGrid(gridPaneUrban, 5, true);
        addRowsToGrid(gridPaneHarbor, 4, true);
        addRowsToGrid(gridPaneMontejo, 1, true);
        addVBoxesToRestPane();
    }


    // Adds a row to a calendar
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

                if (setDefaultHour) {
                    txtStartingTime.setText("09:00");
                    txtEndingTime.setText("21:00");
                }

                addChangeListenerToTimeField(txtEndingTime);
                tempHBox.getChildren().addAll(cboUsers, txtStartingTime, label, txtEndingTime);
            }
        }
    }

    // todo maybe it can be a utility method
    // for each textfield created add a changelistener to validate hour value
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

    private void addVBoxesToRestPane() {
        for (int col = 0; col < gridPaneRest.getColumnCount(); col++) {
            VBox vBox = new VBox();
            gridPaneRest.add(vBox, col, 1);
        }
    }


    // load the header
    private void loadBranchView() {
        cboViewSelector.getSelectionModel().select(views.BRANCH_VIEW);
        loadCalendarHeader(gridPaneHeader);
        loadCalendarDaysHeader(gridPaneHeader, 0);
        if (!tempWorkSchedules.isEmpty()) loadDatabase();
    }

    private void loadCalendarHeader(GridPane gridPane) {
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

    // Load the database, clearing the grids, calculating rows need it
    public void loadDatabase() {
        // todo call another method called clearAndAddGrids
        utilities.clearGridPaneChildren(gridPaneUrban, 0, 1);
        utilities.clearGridPaneChildren(gridPaneHarbor, 0, 1);
        utilities.clearGridPaneChildren(gridPaneMontejo, 0, 1);
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
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getWorkingDayType().equals("ORD")) {
                    if (workSchedule.getBranch().equals("Urban")) {
                        tempRowsNeededUrban += 1;
                    }
                    if (workSchedule.getBranch().equals("Harbor")) {
                        tempRowsNeededTheHarbor += 1;
                    }
                    if (workSchedule.getBranch().equals("Montejo")) {
                        tempRowsNeededMontejo += 1;
                    }
                }
            }
            rowsNeededUrban = Math.max(rowsNeededUrban, tempRowsNeededUrban);
            rowsNeededTheHarbor = Math.max(rowsNeededTheHarbor, tempRowsNeededTheHarbor);
            rowsNeededMontejo = Math.max(rowsNeededMontejo, tempRowsNeededMontejo);

        }
        System.out.println("U: " + rowsNeededUrban + "H:" + rowsNeededTheHarbor + "M:" + rowsNeededMontejo);
        addRowsToGrid(gridPaneUrban, rowsNeededUrban, false);
        addRowsToGrid(gridPaneHarbor, rowsNeededTheHarbor, false);
        addRowsToGrid(gridPaneMontejo, rowsNeededMontejo, false
        );

        // Load the data
        for (WorkSchedule workSchedule : workSchedulesDB) {
            if (workSchedule.getWorkingDayType().equals("ORD")) {
                GridPane gridPane = null;
                int col;
                // int row;
                switch (workSchedule.getBranch()) {
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
            } else if (workSchedule.getWorkingDayType().equals("DES")) {
                VBox tempVBox;
                int col = (int) ChronoUnit.DAYS.between(model.mondayOfTheWeek, workSchedule.getLocalDate());
                tempVBox = (VBox) utilities.getNodeFromGridPane(gridPaneRest, col, 1);
                tempVBox.getChildren().add(new Label(workSchedule.getCollaborator().getUser().getUserName()));
            }
        }
    }


    public void refreshAndValidateData() {
        if (selectedView == views.BRANCH_VIEW) {
            retrieveDataFromPane(gridPaneUrban);
            retrieveDataFromPane(gridPaneHarbor);
            retrieveDataFromPane(gridPaneMontejo);
        } else if (selectedView == views.COLLABORATOR_VIEW) {
            retrieveDataFromCollaboratorPane();
        }

        errorList = "\nIt couldn't be registered because of the next errors";
        warningList = "\nIf it has not errors, it can be registered, but we found the next WARNINGS: ";

        generateRestDaysAndValidateInternally();

        if (selectedView == views.BRANCH_VIEW) {
            insertRestLabels();
        }

        System.out.println("BEFORE VALIDATE");
        validateWithDataBase();
        System.out.println("AFTER VALIDATE");
        System.out.println(errorList);
        System.out.println(warningList);
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

    // It adds to the tempWorkSchedules arrayList each of the registered workSchedule

    private void retrieveDataFromPane(GridPane gridPane) {
        // Loop for each column (each day)
        for (int col = 0; col < gridPane.getColumnCount(); col++) {
            LocalDate date = model.mondayOfTheWeek.plusDays(col);
            // Loop for each row of each day
            for (int row = 1; row < gridPane.getRowCount(); row++) {
                HBox hBox = (HBox) utilities.getNodeFromGridPane(gridPane, col, row);
                ChoiceBox<String> choiceBox = (ChoiceBox<String>) hBox.getChildren().get(0);
                if (choiceBox.getSelectionModel().getSelectedItem() != null) {
                    // Data to be used in the WorkSchedule
                    WorkSchedule workSchedule = new WorkSchedule();

                    workSchedule.setLocalDate(date);
                    workSchedule.setBranch(((Label) gridPane.getChildren().get(0)).getText());
                    workSchedule.setStartingTime(LocalTime.parse((((TextField) hBox.getChildren().get(1)).getText())));
                    workSchedule.setEndingTime(LocalTime.parse((((TextField) hBox.getChildren().get(3)).getText())));
                    workSchedule.setWorkingDayType("ORD"); // Because if it works is an ordinary workingday
                    for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
                        if (collaborator.getUser().getUserName().equals(choiceBox.getSelectionModel().getSelectedItem())) {
                            workSchedule.setCollaborator(collaborator);
                            break;
                        }
                    }
                    workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    addOrUpdateTempWorkSchedules(workSchedule);
                }
            }
        }
    }

    private void addOrUpdateTempWorkSchedules(WorkSchedule tempWorkSchedule) {
        if(tempWorkSchedules.contains(tempWorkSchedule)){
            int index = tempWorkSchedules.indexOf(tempWorkSchedule);
            tempWorkSchedules.set(index, tempWorkSchedule);
        } else{
            tempWorkSchedules.add(tempWorkSchedule);
        }
    }

    private void addIfInexistentToTempWorkSchedules(WorkSchedule restWorkSchedule) {
        if(!tempWorkSchedules.contains(restWorkSchedule)){
            tempWorkSchedules.add(restWorkSchedule);
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
                    workSchedule.setWorkingDayType(cboWorkingDayType.getSelectionModel().getSelectedItem());
                } else {
                    workSchedule.setWorkingDayType("DES");
                }
                ChoiceBox<String> cboBranch = (ChoiceBox<String>) hBox.getChildren().get(1);
                workSchedule.setBranch(cboBranch.getSelectionModel().getSelectedItem());
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

                System.out.println(workSchedule);

                addOrUpdateTempWorkSchedules(workSchedule);
            }
        }
    }


    private void generateRestDaysAndValidateInternally() {

        System.out.println("BEGINS GENERATERESDAYS");
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            double totalTimeWorkedPerCollaborator = 0;
            // loop
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                int registerPerCollaboratorPerDay = 0;
                for (WorkSchedule workSchedule : tempWorkSchedules) {
                    System.out.println("REVIEWING  "+workSchedule.getLocalDate()+" "+workSchedule.getBranch()+
                            " "+workSchedule.getCollaborator().getUser().getUserName());

                    if ((workSchedule.getCollaborator().equals(collaborator)) && (workSchedule.getLocalDate().equals(localDate))) {
                        System.out.println("ENTERED THE IF");
                        registerPerCollaboratorPerDay++;
                        if (workSchedule.getStartingTime() != null && workSchedule.getEndingTime() != null) {
                            totalTimeWorkedPerCollaborator += ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime()) / 60.0;
                        }
                    }
                }
                // Setting collaborator as restDay
                if (registerPerCollaboratorPerDay == 0) {
                    WorkSchedule restWorkSchedule = new WorkSchedule();
                    restWorkSchedule.setCollaborator(collaborator);
                    restWorkSchedule.setWorkingDayType("DES");
                    restWorkSchedule.setBranch("None");
                    restWorkSchedule.setLocalDate(localDate);
                    restWorkSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    addIfInexistentToTempWorkSchedules(restWorkSchedule);
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
    }


    private void insertRestLabels() {
        VBox tempVBox;
        LocalDate localDate = model.mondayOfTheWeek;
        for (int i = 0; i < gridPaneRest.getColumnCount(); i++) {
            localDate = model.mondayOfTheWeek.plusDays(i);
            tempVBox = (VBox) utilities.getNodeFromGridPane(gridPaneRest, i, 1);
            System.out.println("RESTLABELS. Col"+i+". VBox"+tempVBox);
            tempVBox.getChildren().clear();
            for (WorkSchedule workSchedule : tempWorkSchedules) {
                System.out.println("PRINTING WORKSCHEDULE: "+workSchedule.getLocalDate()+" "+workSchedule.getBranch()+
                        " "+workSchedule.getCollaborator().getUser().getUserName());
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getBranch().equals("None")) {
                    tempVBox.getChildren().add(new Label(workSchedule.getCollaborator().getUser().getUserName()));
                }
            }
        }
    }

    private void validateWithDataBase() {
        for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
            for (WorkSchedule workSchedule : workSchedulesDB) {
                if ((workSchedule.getCollaborator().getId() == (tempWorkSchedule.getCollaborator().getId())) &&
                        (workSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))) {
                    // todo check because of getStartingTime returns null

                    if ((!workSchedule.getWorkingDayType().equals(tempWorkSchedule.getWorkingDayType())) ||
                            (!Objects.equals(workSchedule.getStartingTime(), tempWorkSchedule.getStartingTime())) ||
                            (!Objects.equals(workSchedule.getEndingTime(), tempWorkSchedule.getEndingTime())) ||
                            (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {
                        warningList += "\nThis date with this collaborator was already registered: " + workSchedule.getCollaborator().getUser().getUserName() + " " + workSchedule.getLocalDate() +
                                " with this data: " + "working day type: " + workSchedule.getWorkingDayType() + ", branch: " + workSchedule.getBranch() + ", starting time: " + workSchedule.getStartingTime() + "and ending time: " + workSchedule.getEndingTime() +
                                ". And it will be replaced with this data: " + "working day type: " + tempWorkSchedule.getWorkingDayType() + ", branch: " + tempWorkSchedule.getBranch() + ", starting time: " + tempWorkSchedule.getStartingTime() + "and ending time: " + tempWorkSchedule.getEndingTime();
                    }
                }
            }
        }

    }


    // BUTTONS ON ACTION

    public void updateSchedule() {
        Model.getInstance().selectedLocalDate = datePicker.getValue();
        refreshVariables();
        loadBranchView();
    }
    // It validates the inserted data and if there are no errors, insert it to the database


    public void saveIntoDB() {
        refreshAndValidateData();
        workScheduleDAO.createOrReplaceRegisters(tempWorkSchedules);
    }

    public void showCopyFromAnotherWeek() {
        utilities.loadWindow("view/workSchedule/copyWorkSchedule.fxml", new Stage(), "Copy from Another Week", StageStyle.DECORATED, false, true);
    }

    public void changeView() {
        selectedView = cboViewSelector.getValue();
        if (selectedView == views.BRANCH_VIEW) {
            initGrids();
            loadBranchView();
        }

        if (selectedView == views.COLLABORATOR_VIEW) {
            loadCollaboratorsView();
        }
    }

    public void loadCollaboratorsView() {

        // remove unused gridpanes
        paneGridPanesContainer.getChildren().clear();

        /*// add a new gridpane

            with rows = title, day with date, one for collaborator.
            columns = collaborator name, 7 for each day=8*/

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
        loadCollaboratorsNames(gridPaneViewCollaborators, 2);
        loadCalendarDaysHeader(gridPaneViewCollaborators, 1);
        loadInternalGrids(gridPaneViewCollaborators);

/*
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                testLabel = new Label("TEST");
                gridPaneViewWorkers.add(testLabel, j, i);
            }
        }
*/

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


}
