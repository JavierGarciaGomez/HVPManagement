package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class workScheduleController implements Initializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneRest;
    public GridPane gridPaneTheHarbor;
    private Model model;
    private Utilities utilities;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initVariables();
        initGrids();
        loadGrids();
    }

    private void initVariables() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();

        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.setLastDayOfMonth();
        model.userNames = UserDAO.getInstance().getActiveUserNames();
        System.out.println("Monday date and lastday of month setted: " + Model.getInstance().mondayOfTheWeek + " " + Model.getInstance().lastDayOfMonth);
    }

    private void initGrids() {
        initRowsForEachBranch();
    }

    private void initRowsForEachBranch() {
        addRowsToGrid(gridPaneUrban, 5);
        addRowsToGrid(gridPaneTheHarbor, 4);
        addRowsToGrid(gridPaneMontejo, 1);
    }

    private void addRowsToGrid(GridPane gridPaneBranch, int rowsToAdd) {
//        Node node = (Node) actionEvent.getSource();
//        GridPane gridPane = (GridPane) node.getParent();

        int rows = gridPaneBranch.getRowCount();
        HBox tempHBox;
        for (int i = 0; i < rowsToAdd; i++) {
            for (int j = 0; j < gridPaneBranch.getColumnCount(); j++) {
                tempHBox = new HBox();
                gridPaneBranch.add(tempHBox, j, rows + 1 + i);
                // combobox for users
                ComboBox<String> cboUsers = new ComboBox<>();
                cboUsers.setStyle("-fx-font-size:11");
                cboUsers.setMaxHeight(Double.MAX_VALUE);
                cboUsers.setItems(model.userNames);

                // spinner hour
                TextField startingTime = new TextField();
                startingTime.setText("09:00");

                startingTime.setPrefWidth((50));

                // todo try to generate a method passing the TextField

                startingTime.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue) {
                    }
                    if (!newValue) {
                        try {
                            LocalTime.parse(startingTime.getText());
                        } catch (DateTimeParseException e) {
                            utilities.showAlert(Alert.AlertType.ERROR, "Time format error", "The hour format is incorrect, it has to be like 10:00");
                            startingTime.requestFocus();
                        }
                    }
                });

/*
                startingTime.focusedProperty().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        LocalTime originalLocalTime = LocalTime.parse(startingTime.getText());
                        LocalTime newLocalTime =originalLocalTime;
                        boolean isValid = true;
                        int hour = 0;

                        int minute = 0;
                        try {
                            String inputString = startingTime.getText();
                            if (inputString.length() <= 2) hour = Integer.parseInt(inputString);
                            else if (inputString.length() <= 5 && inputString.contains(":")) {
                                String[] strTime = inputString.split(":");
                                hour = Integer.parseInt(strTime[0]);
                                minute = Integer.parseInt(strTime[1]);
                            } else isValid = false;
                            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) isValid = false;

                        } catch (DateTimeParseException | NumberFormatException e) {
                            isValid = false;
                        }
                        if (isValid) {
                            newLocalTime = LocalTime.of(hour, minute);
                            startingTime.setText(String.valueOf(newLocalTime));
                        } else {
                            System.out.println("On lost focus IS NOT VALID, starting: " +
                                    originalLocalTime + " " + newLocalTime);

                            System.out.println("Wrong format hour");
                            newLocalTime = originalLocalTime;
                        }


                        startingTime.setText(String.valueOf(newLocalTime));
                        System.out.println("At the end. starting: " +
                                originalLocalTime + " " + newLocalTime);

                    }
                });
*/

                // label -
                Label label = new Label("-");
                // spinner hour
                TextField endingTime = new TextField();
                endingTime.setPrefWidth(50);
                tempHBox.getChildren().addAll(cboUsers, startingTime, label, endingTime);
            }
        }

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
}
