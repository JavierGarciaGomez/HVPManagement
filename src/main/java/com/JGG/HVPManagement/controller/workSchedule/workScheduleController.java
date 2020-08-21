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
import javafx.geometry.Pos;
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
    public Button saveIntoDB;
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
        initVariables();
        initGrids();
        loadGrids();
    }

    private void initVariables() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.setLastDayOfMonth();
        model.activeAndWorkersuserNamesAndNull = UserDAO.getInstance().getActiveAndWorkersUserNames();
        model.activeAndWorkersuserNamesAndNull.add(null);

        Runnable runnable = () -> {
            model.workSchedulesOfTheWeek = workScheduleDAO.getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
            model.activeAndWorkerCollaboratos = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lblConnectionStatus.setText("You can register new data");
                    lblConnectionStatus.setStyle("-fx-background-color: lawngreen");
                }
            });
        };
        new Thread(runnable).start();
    }

    private void initGrids() {
        addRowsToGrid(gridPaneUrban, 5);
        addRowsToGrid(gridPaneTheHarbor, 4);
        addRowsToGrid(gridPaneMontejo, 1);
    }

    private void addRowsToGrid(GridPane gridPaneBranch, int rowsToAdd) {
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
                startingTime.setText("09:00");

                startingTime.setPrefWidth((50));
                addChangeListenerToTimeField(startingTime);
                Label label = new Label("-");
                // spinner hour
                TextField endingTime = new TextField();
                endingTime.setPrefWidth(50);
                endingTime.setText("21:00");

                addChangeListenerToTimeField(endingTime);
                tempHBox.getChildren().addAll(cboUsers, startingTime, label, endingTime);
            }
        }
    }

    private void addChangeListenerToTimeField(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    LocalTime.parse(textField.getText());
                } catch (DateTimeParseException e) {
                    utilities.showAlert(Alert.AlertType.ERROR, "Time format error", "The hour format is incorrect, it has to be like 10:00");
                    textField.requestFocus();
                }
            }
        });
    }

    private void loadGrids() {
        loadCalendarHeader();
    }

    private void loadCalendarHeader() {
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() + "/" + model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() + "/" + lastDate.getMonthValue();
        String fullString = "CALENDAR FROM " + strFirstDay + " TO " + strLastDay;

        Label label = new Label(fullString);
        gridPaneHeader.add(label, 0, 0);
        GridPane.setColumnSpan(label, 7);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }


    public void updateSchedule(ActionEvent actionEvent) {

    }

    public void addCollaboratorRow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        GridPane gridPane = (GridPane) node.getParent();
        addRowsToGrid(gridPane, 1);

    }

    public void delCollaboratorRow(ActionEvent actionEvent) {
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
        System.out.println("PRINT WORKSCHEDULES");
        for (WorkSchedule workSchedule : tempWorkSchedules) {
            System.out.println(workSchedule.getId() + " " + workSchedule.getWorkingDayType());
        }
        System.out.println("Finished");
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
        System.out.println("VALIDATE DATABASE PRINT WORKSCHEDULES");
        for (WorkSchedule workSchedule : tempWorkSchedules) {
            System.out.println(workSchedule.getId() + " " + workSchedule.getWorkingDayType());
        }
        System.out.println("Finished");

        for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
            System.out.println("Entering the first for " + tempWorkSchedule.getId() + " " + tempWorkSchedule.getWorkingDayType() + " " + tempWorkSchedule.getCollaborator().getUser().getUserName());
            for (WorkSchedule workSchedule : model.workSchedulesOfTheWeek) {
                System.out.println("Temp. CollaboratorId" + tempWorkSchedule.getCollaborator().getId() + ". LocalDate: " + tempWorkSchedule.getLocalDate());
                System.out.println("Database. CollaboratorId" + workSchedule.getCollaborator().getId() + ". LocalDate: " + workSchedule.getLocalDate());
                if ((workSchedule.getCollaborator().getId() == (tempWorkSchedule.getCollaborator().getId())) &&
                        (workSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))) {
                    // todo check because of getStartingTime returns null
                    System.out.println("Entering the first for " + tempWorkSchedule.getId() + " " + tempWorkSchedule.getWorkingDayType() + " " + tempWorkSchedule.getCollaborator().getUser().getUserName());
                    System.out.println("Entering the first for " + tempWorkSchedule.getId() + " " + tempWorkSchedule.getWorkingDayType() + " " + tempWorkSchedule.getCollaborator().getUser().getUserName());

                    if ((!workSchedule.getWorkingDayType().equals(tempWorkSchedule.getWorkingDayType())) ||
                            (!Objects.equals(workSchedule.getStartingTime(), tempWorkSchedule.getStartingTime())) ||
                            (!Objects.equals(workSchedule.getEndingTime(), tempWorkSchedule.getEndingTime())) ||
                            (!Objects.equals(workSchedule.getBranch(), tempWorkSchedule.getBranch()))) {
                        System.out.println("I found a warning");
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
}
