package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        startRunnablesWithNoUrgentData();
        // Loading data from database
        loadWorkers();
        initGrids();
        loadGrids();
    }

    private void initUnmutableVariables() {
        model.activeAndWorkersuserNamesAndNull = UserDAO.getInstance().getActiveAndWorkersUserNames();
        model.activeAndWorkersuserNamesAndNull.add(null);
    }

    private void initInstances() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
    }

    // init variables and instances
    private void initVariables() {
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.setLastDayOfMonth();
    }

    private void startRunnablesWithNoUrgentData() {
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
    }


    private void loadWorkers() {
        Runnable runnable = () -> {
            model.activeAndWorkerCollaboratos = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        };
        new Thread(runnable).start();
    }


    private void initGrids() {
        addRowsToGrid(gridPaneUrban, 5, true);
        addRowsToGrid(gridPaneTheHarbor, 4, true);
        addRowsToGrid(gridPaneMontejo, 1, true);
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

                if(setDefaultHour){
                    startingTime.setText("09:00");
                    endingTime.setText("21:00");
                }


                addChangeListenerToTimeField(endingTime);
                tempHBox.getChildren().addAll(cboUsers, startingTime, label, endingTime);
            }
        }
    }

    // for each textfield created add a changelistener to validate hour value
    private void addChangeListenerToTimeField(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if(textField.getText().equals("")){
                    return;
                }
                try {
                    LocalTime.parse(textField.getText());
                } catch (DateTimeParseException e) {
                    utilities.showAlert(Alert.AlertType.ERROR, "Time format error", "The hour format is incorrect, it has to be like 10:00");
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
    }

    // load the header
    private void loadGrids() {
        loadCalendarHeader();
        // loadDatabase();
    }

    private void loadCalendarHeader() {
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() + "/" + model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() + "/" + lastDate.getMonthValue();
        String fullString = "CALENDAR FROM " + strFirstDay + " TO " + strLastDay;

        Label label = (Label) utilities.getNodeFromGridPane(gridPaneHeader, 0, 0);
        label.setText(fullString);
    }

    // Load the database, clearing the grids, calculating rows need it
    public void loadDatabase() {
        model.workSchedulesOfTheWeek = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));

        // todo call another method called clearAndAddGrids
        clearGridPaneAndLeaveHeaders(gridPaneUrban);
        clearGridPaneAndLeaveHeaders(gridPaneTheHarbor);
        clearGridPaneAndLeaveHeaders(gridPaneMontejo);

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
            rowsNeededUrban = rowsNeededUrban > tempRowsNeededUrban ? rowsNeededUrban : tempRowsNeededUrban;
            rowsNeededTheHarbor = rowsNeededTheHarbor > tempRowsNeededTheHarbor ? rowsNeededTheHarbor : tempRowsNeededTheHarbor;
            rowsNeededMontejo = rowsNeededMontejo > tempRowsNeededMontejo ? rowsNeededMontejo : tempRowsNeededMontejo;

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
                if (workSchedule.getBranch().equals("Urban")) {
                    gridPane = gridPaneUrban;
                } else if (workSchedule.getBranch().equals("The Harbor")) {
                    gridPane = gridPaneTheHarbor;
                } else if (workSchedule.getBranch().equals("Montejo")) {
                    gridPane = gridPaneMontejo;
                }
                col = (int) ChronoUnit.DAYS.between(model.mondayOfTheWeek, workSchedule.getLocalDate());

                for (int row = 1; row < gridPane.getRowCount(); row++) {
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

    }


    private void clearGridPaneAndLeaveHeaders(GridPane gridPane) {
        for (int col = 0; col < gridPane.getColumnCount(); col++) {
            for (int row = 1; row < gridPane.getRowCount(); row++) {
                Node node = utilities.getNodeFromGridPane(gridPane, col, row);
                gridPane.getChildren().remove(node);
            }
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

    public void retrieveDataFrom() {

    }

    // It validates the inserted data and if there are no errors, insert it to the database
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

    public void saveIntoDB() {
        refreshAndValidateData();
        workScheduleDAO.createOrReplaceRegisters(tempWorkSchedules);
    }

    public void updateSchedule() {
        Model.getInstance().selectedLocalDate = datePicker.getValue();
        initVariables();
        loadGrids();

    }
}
