package com.JGG.HVPManagement.controller.schedule;

import com.JGG.HVPManagement.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class workScheduleController implements Initializable {
    public GridPane gridPaneHeader;
    public GridPane gridPaneUrban;
    public GridPane gridPaneMontejo;
    public GridPane gridPaneRest;
    public GridPane gridPaneTheHarbor;
    private Model model;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initVariables();
        initGrids();
        loadGrids();
    }

    private void initVariables() {
        model = Model.getInstance();
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.setLastDayOfMonth();
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
        for(int i=0; i<rowsToAdd; i++){
            for(int j=0; j<gridPaneBranch.getColumnCount();j++){
                tempHBox = new HBox();
                gridPaneBranch.add(tempHBox, j, rows+1+i);
                tempHBox.getChildren().add(new Label("Test Label"));
            }
        }

    }


    private void loadGrids() {
        loadCalendarHeader();
    }

    private void loadCalendarHeader() {
        String strFirstDay = model.mondayOfTheWeek.getDayOfMonth() +"/"+ model.mondayOfTheWeek.getMonthValue();
        LocalDate lastDate = model.mondayOfTheWeek.plusDays(6);
        String strLastDay = lastDate.getDayOfMonth() +"/"+ lastDate.getMonthValue();
        String fullString = "CALENDAR FROM "+strFirstDay+" TO "+strLastDay;

        Label label = new Label(fullString);
        gridPaneHeader.add(label,0,0);
        GridPane.setColumnSpan(label, 7);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }



    public void updateSchedule(ActionEvent actionEvent) {

    }

    public void addCollaboratorRow(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        GridPane gridPane = (GridPane) node.getParent();

        int rows = gridPane.getRowCount();
        HBox tempHBox;
        for(int i=0; i<gridPane.getColumnCount();i++){
            tempHBox = new HBox();
            gridPane.add(tempHBox, i, rows+1);
            tempHBox.getChildren().add(new Label("Test Label"));
        }


    }
}
