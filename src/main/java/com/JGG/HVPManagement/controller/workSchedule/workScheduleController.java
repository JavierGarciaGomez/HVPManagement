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
    public GridPane gridPaneTheHarbor;
    public AnchorPane rootPane;
    public Label lblConnectionStatus;
    public DatePicker datePicker;
    public VBox paneGridPanesContainer;
    private GridPane gridPaneViewWorkers;
    private Model model;
    private Utilities utilities;
    private WorkScheduleDAO workScheduleDAO;
    private List<WorkSchedule> tempWorkSchedules;
    String errorList;
    String warningList;
    private boolean hasErrors = false;
    private boolean hasWarnings = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initInstances();
        initUnmutableVariables();
        initVariables();
        // Loading data from database
        initGrids();
        loadGrids();
    }

    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
    }

    private void initUnmutableVariables() {
        model.activeAndWorkersuserNamesAndNull = UserDAO.getInstance().getObservableListOfActiveAndWorkersUserNames();
        model.activeAndWorkersuserNamesAndNull.add(null);
    }

    // init variables and instances
    private void initVariables() {
        model.activeAndWorkerCollaboratos = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        refreshVariables();
    }

    private void refreshVariables() {
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.workSchedulesOfTheWeek = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
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
        addRowsToGrid(gridPaneTheHarbor, 4, true);
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
                TextField startingTime = new TextField();

                startingTime.setPrefWidth((50));
                addChangeListenerToTimeField(startingTime);
                Label label = new Label("-");
                // spinner hour
                TextField endingTime = new TextField();
                endingTime.setPrefWidth(50);

                if (setDefaultHour) {
                    startingTime.setText("09:00");
                    endingTime.setText("21:00");
                }

                addChangeListenerToTimeField(endingTime);
                tempHBox.getChildren().addAll(cboUsers, startingTime, label, endingTime);
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
            vBox.getChildren().add(new Label("PRUEBA"));
        }
    }


    // load the header
    private void loadGrids() {
        loadCalendarHeader(gridPaneHeader);
        loadCalendarDaysHeader(gridPaneHeader, 0);
        if (!model.workSchedulesOfTheWeek.isEmpty()) loadDatabase();
    }

    private void loadCalendarHeader(GridPane gridPane) {
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() + "/" + model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() + "/" + lastDate.getMonthValue();
        String fullString = "CALENDAR FROM " + strFirstDay + " TO " + strLastDay;
        Label label = new Label(fullString);
        gridPaneHeader.add(label, 0, 0, gridPane.getColumnCount(), 1);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }


    private void loadCalendarDaysHeader(GridPane gridPane, int startingColumn) {
        utilities.clearGridPaneChildren(gridPane, startingColumn, 1);

        LocalDate localDate;
        for (int i = 0; i < model.weekDaysNames.length; i++) {
            int col = i+startingColumn;
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
        utilities.clearGridPaneChildren(gridPaneTheHarbor, 0, 1);
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
            for (WorkSchedule workSchedule : model.workSchedulesOfTheWeek) {
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getWorkingDayType().equals("ORD")) {
                    if (workSchedule.getBranch().equals("Urban")) {
                        tempRowsNeededUrban += 1;
                    }
                    if (workSchedule.getBranch().equals("The Harbor")) {
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
        addRowsToGrid(gridPaneTheHarbor, rowsNeededTheHarbor, false);
        addRowsToGrid(gridPaneMontejo, rowsNeededMontejo, false
        );

        // Load the data
        for (WorkSchedule workSchedule : model.workSchedulesOfTheWeek) {
            if (workSchedule.getWorkingDayType().equals("ORD")) {
                GridPane gridPane = null;
                int col;
                // int row;
                switch (workSchedule.getBranch()) {
                    case "Urban":
                        gridPane = gridPaneUrban;
                        break;
                    case "The Harbor":
                        gridPane = gridPaneTheHarbor;
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
        // Initializes the arrayList, each time a new one in case of errors.
        tempWorkSchedules = new ArrayList<>();
        retrieveDataFromPane(gridPaneUrban);
        retrieveDataFromPane(gridPaneTheHarbor);
        retrieveDataFromPane(gridPaneMontejo);

        errorList = "\nIt couldn't be registered because of the next errors";
        warningList = "\nIf it has not errors, it can be registered, but we found the next WARNINGS: ";


        generateRestDaysAndValidateInternally();
        insertRestLabels();
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
                    for (Collaborator collaborator : model.activeAndWorkerCollaboratos) {
                        if (collaborator.getUser().getUserName().equals(choiceBox.getSelectionModel().getSelectedItem())) {
                            workSchedule.setCollaborator(collaborator);
                            break;
                        }
                    }
                    workSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    tempWorkSchedules.add(workSchedule);
                }
            }
        }
    }

    private void generateRestDaysAndValidateInternally() {
        // loop each day, for each collaborator, to check for errors
        for (Collaborator collaborator : model.activeAndWorkerCollaboratos) {
            double totalTimeWorkedPerCollaborator = 0;
            for (int i = 0; i < 7; i++) {
                LocalDate localDate = model.mondayOfTheWeek.plusDays(i);
                int registerPerCollaboratorPerDay = 0;
                for (WorkSchedule workSchedule : tempWorkSchedules) {
                    if ((workSchedule.getCollaborator().equals(collaborator)) && (workSchedule.getLocalDate().equals(localDate))) {
                        registerPerCollaboratorPerDay++;
                        totalTimeWorkedPerCollaborator += ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime()) / 60.0;
                    }
                }
                // Setting collaborator as restDay
                if (registerPerCollaboratorPerDay == 0) {
                    WorkSchedule restWorkSchedule = new WorkSchedule();
                    restWorkSchedule.setCollaborator(collaborator);
                    restWorkSchedule.setWorkingDayType("DES");
                    restWorkSchedule.setLocalDate(localDate);
                    restWorkSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                    tempWorkSchedules.add(restWorkSchedule);
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
        // loop to check for repetitions
        for (WorkSchedule workSchedule : model.workSchedulesOfTheWeek) {

        }
        // todo workScheduleDAO.createOrReplaceRegisters(tempWorkSchedules);

    }

    private void insertRestLabels() {
        VBox tempVBox;
        LocalDate localDate = model.mondayOfTheWeek;
        for (int i = 0; i < gridPaneRest.getColumnCount(); i++) {
            localDate = model.mondayOfTheWeek.plusDays(i);
            tempVBox = (VBox) utilities.getNodeFromGridPane(gridPaneRest, i, 1);
            tempVBox.getChildren().clear();
            for (WorkSchedule workSchedule : tempWorkSchedules) {
                if (workSchedule.getLocalDate().equals(localDate) && workSchedule.getWorkingDayType().equals("DES")) {
                    tempVBox.getChildren().add(new Label(workSchedule.getCollaborator().getUser().getUserName()));
                }
            }
        }
    }

    private void validateWithDataBase() {
        for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {

            for (WorkSchedule workSchedule : model.workSchedulesOfTheWeek) {
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
        loadGrids();
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
        // remove unused gridpanes
        paneGridPanesContainer.getChildren().clear();

        /*// add a new gridpane

            with rows = title, day with date, one for collaborator.
            columns = collaborator name, 7 for each day=8*/

        gridPaneViewWorkers = new GridPane();
        gridPaneViewWorkers.setMaxWidth(Double.MAX_VALUE);
        int totalColumns = model.weekDaysNames.length + 1;
        int totalRows = model.activeAndWorkerCollaboratos.size() + 2;

        for (int col = 0; col < totalColumns; col++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setFillWidth(true);
            columnConstraints.setHgrow(Priority.SOMETIMES);
            columnConstraints.setPrefWidth(100);
            gridPaneViewWorkers.getColumnConstraints().add(columnConstraints);
        }

        paneGridPanesContainer.getChildren().add(gridPaneViewWorkers);

        // Load top header
        // Load collaborators names
        // create vboxes for the: workingDayTupe, branch, startingTime, endingTime,

        Label testLabel;

        loadCalendarHeader(gridPaneViewWorkers);
        loadWorkersNames(gridPaneViewWorkers, 0, 1);
        loadCalendarDaysHeader(gridPaneViewWorkers, 1);

/*
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                testLabel = new Label("TEST");
                gridPaneViewWorkers.add(testLabel, j, i);
            }
        }
*/

    }

    private void loadWorkersNames(GridPane gridPaneViewWorkers, int i, int i1) {
/*        utilities.clearGridPaneChildren(gridPane, startingColumn, row);

        LocalDate localDate;
        for (int col = startingColumn; col < model.weekDaysNames.length; col++) {
            localDate = model.mondayOfTheWeek.plusDays(col);
            int dayNumber = localDate.getDayOfMonth();
            int monthNumber = localDate.getMonthValue();
            String labelString = model.weekDaysNames[col] + " " + dayNumber + " / " + monthNumber;
            Label dayLabel = new Label(labelString);
            gridPane.add(dayLabel, col, row);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
        }*/
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
