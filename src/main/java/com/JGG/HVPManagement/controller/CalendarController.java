package com.JGG.HVPManagement.controller;

import com.JGG.HVPManagement.dao.AppointmentDAO;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    public BorderPane borderPane;

    public GridPane gridPane;
    public DatePicker datePicker;
    public VBox branchCheckBoxes;
    public VBox vetCheckBoxes;

    public CalendarController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGrid();
        loadGrid();


        //todo
        // Initialize the grid

        //loadAppointmentsGrid();

        //todo change method name

        // todo Appointment appointment = new Appointment("ANV", "pet", "client", "Montejo", "Consulta", "Garrapatas", "2020-12-08", "08:00");
        // todo tue11.setText(appointment.getVeterinarian()+"\n "+appointment.getPetName()+"\n "+appointment.getClientName()+"\n "+appointment.getService());
    }

    private void initVariables() {
        if (Model.getInstance().selectedLocalDate == null) {
            Model.getInstance().selectedLocalDate = LocalDate.now();
        }
        Model.getInstance().setMondayDate();
        Model.getInstance().setLastDayOfMonth();
        System.out.println("Monday date and lastday of month setted: " + Model.getInstance().mondayOfTheWeek + " " + Model.getInstance().lastDayOfMonth);
    }

    // This methods set the constraints and insert a Pane for each cell
    private void initGrid() {
        initGridAndSetConstraints();
        addHeadersDaysPanes();
        addHeaderHoursPanesAndLabels();
        addAppointmentsGridPanes();
        addCheckBoxes();
    }


    // Set the grid of 8*12 and set the constraints
    private void initGridAndSetConstraints() {
        for (int i = 0; i < 13; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < 8; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(columnConstraints);
        }
    }

    private void addHeadersDaysPanes() {
        //todo change comments and variablenames
        // loop for each day
        for (int i = 1; i < gridPane.getColumnCount(); i++) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("grid-header");

            // Make panes take up equal space
            HBox.setHgrow(stackPane, Priority.ALWAYS);

            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            stackPane.setMinWidth(gridPane.getPrefWidth() / 8);
            // Add it to the header
            gridPane.add(stackPane, i, 0);
            // Add weekday name
        }
        // todo delete
        gridPane.setGridLinesVisible(true);
    }


    private void addHeaderHoursPanesAndLabels() {
        //todo change comments and variablenames
        // 7 days in a week
        int hours = 12;
        // Weekday names
        String[] availableHours = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        // loop for each hour
        for (int i = 1; i < gridPane.getRowCount(); i++) {
            // Make new pane
            StackPane stackPane = new StackPane();
            // Make panes take up equal space
            // todo review styles
            stackPane.getStyleClass().add("grid-header");

            HBox.setHgrow(stackPane, Priority.ALWAYS);
            stackPane.setMaxWidth(Double.MAX_VALUE);
            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            stackPane.setMinWidth(gridPane.getPrefWidth() / gridPane.getRowCount());
            // Create label and add it
            Label lbl = new Label(availableHours[i - 1]);
            lbl.setPadding(new Insets(2));

            stackPane.getChildren().add(lbl);
            // add the pane to the grid
            gridPane.add(stackPane, 0, i);
            // Add weekday name
        }
    }


    private void addAppointmentsGridPanes() {
        int rows = 13;
        int cols = 8;
        // set a VBox for each grid
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                //Add a VBox for each cell
                VBox vBox = new VBox();
                //todo change the name and add styles
                vBox.getStyleClass().add("calendar_pane");
                // todo check if neccesary
                vBox.setMinWidth(gridPane.getPrefWidth() / 8);

                vBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent -> {
                    addAppointment(vBox);
                }));
                // set vGrow to always expand or shrink
                GridPane.setVgrow(vBox, Priority.ALWAYS);
                // Add the Vbox to the grid
                gridPane.add(vBox, j, i);
            }
        }

    }

    private void addCheckBoxes() {
        CheckBox checkBox = null;
        for (String string : Model.getInstance().branches) {
            checkBox = new CheckBox(string);
            branchCheckBoxes.getChildren().add(checkBox);
            checkBox.setAccessibleText(string);
            checkBox.addEventHandler(ActionEvent.ACTION, (actionEvent -> {
                handleFilters();
            }));
            checkBox.setSelected(true);
        }

        for (User user : new UserDAO().getUsers()) {
            checkBox = new CheckBox(user.getUser());
            vetCheckBoxes.getChildren().add(checkBox);
            checkBox.setAccessibleText(user.getUser());
            checkBox.addEventHandler(ActionEvent.ACTION, (actionEvent -> {
                handleFilters();
            }));
            checkBox.setSelected(true);
        }

    }


    private void loadGrid() {
        initVariables();
        loadWeekDaysHeaderLabels();
        loadAppointmentsGrid();
    }


    private void loadWeekDaysHeaderLabels() {
        int dayDateLabel = Model.getInstance().mondayOfTheWeek.getDayOfMonth();
        int lastDayOfMonth = Model.getInstance().lastDayOfMonth.getDayOfMonth();
        int monthDateLabel = Model.getInstance().mondayOfTheWeek.getMonthValue();

        for (int i = 1; i < gridPane.getColumnCount(); i++) {
            // retrieve pane for each grid
            StackPane dayHeader = (StackPane) getNodeFromGridPane(gridPane, i, 0);
            dayHeader.getChildren().clear();
            


            //change day if gets to the lasy day of the month
            if (dayDateLabel > lastDayOfMonth) dayDateLabel = 1;
            Label lbl = new Label(Model.getInstance().weekDaysNames[i - 1] + "\n" + Integer.toString(dayDateLabel) +
                    "/" + Integer.toString(monthDateLabel));





            dayHeader.getChildren().add(lbl);
            dayDateLabel++;
        }

        //lblCount++;
    }


    private void loadAppointmentsGrid() {
        clearAppointmentsGrid();
        int rows = 12;
        int cols = 7;

        LocalDate monday = Model.getInstance().mondayOfTheWeek;
        LocalDate sunday = monday.plusDays(6);
        List<Appointment> appointmentsInTheWeek = new AppointmentDAO().getAppointmentsBetweenDates(monday, sunday);
        String[] availableHours = {"9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        // method to put in a label;
        for (Appointment a : appointmentsInTheWeek) {
            int dayIndex = a.getDate().getDayOfWeek().getValue();
            int hourIndex = a.getTime().getHour();
            String hourIndexString = (hourIndex + ":00");
            for (int i = 0; i < availableHours.length; i++) {
                if (availableHours[i].equals(hourIndexString)) {
                    System.out.println(a.getPetName()+hourIndexString+" found");
                    hourIndex = i + 1;
                    System.out.println(a.getPetName()+hourIndex);
                    //todo delete
                }
            }
            if(hourIndex>12){
                System.out.println(a.getPetName()+hourIndexString+"changed hour index, from "+hourIndex);
                hourIndex=12;
            }


            Label label = new Label(a.getService() + "-" + a.getPetName());
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.getStyleClass().add("appointment-label");

            if (a.getBranch() != null) {
                switch (a.getBranch()) {
                    case "Montejo":
                        label.setStyle("-fx-background-color: cyan");
                        break;

                    case "Urban":
                        label.setStyle("-fx-background-color: goldenrod");
                        break;

                    case "Harbor":
                        label.setStyle("-fx-background-color: greenyellow");
                        break;
                }
            }


            label.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {

                //editAppointment((VBox) label.getParent(), label.getText(), label.getAccessibleText());
                editAppointment((VBox) label.getParent(), a.getId());
            });

            // Mouse effects
            label.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                label.getScene().setCursor(Cursor.HAND);
            });

            label.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                label.getScene().setCursor(Cursor.DEFAULT);
            });

            System.out.println("grid pane rows: "+gridPane.getRowCount());
            System.out.println("Searching "+a+"day index"+dayIndex+"row index"+hourIndex);


            VBox vBox = (VBox) getNodeFromGridPane(gridPane, dayIndex, hourIndex);


            vBox.getChildren().add(label);
        }
    }


    private void clearAppointmentsGrid() {
        int rows = 13;
        int cols = 8;
        for (int j = 1; j < cols; j++) {
            for (int i = 1; i < rows; i++) {
                VBox vBox = (VBox) getNodeFromGridPane(gridPane, j, i);
                vBox.getChildren().clear();
            }
        }
    }


    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {

            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }

        }
        return null;
    }

    @FXML
    public void updateSchedule() {
        Model.getInstance().selectedLocalDate = datePicker.getValue();
        loadGrid();
    }

    private void addAppointment(VBox day) {

        LocalDate appointmentDate = getAppointmentDate(day);
        LocalTime appointmentTime = getAppointmentTime(day);
        System.out.println(appointmentDate + " " + appointmentTime);

        Model.getInstance().appointmentDate = appointmentDate;
        Model.getInstance().appontimenTime = appointmentTime;

        // Open the view
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ManageAppointment.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Manage users");


            ManageAppointmentController controller = fxmlLoader.getController();
            controller.initData(this);

            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private LocalDate getAppointmentDate(VBox day) {
        LocalDate localDate = Model.getInstance().mondayOfTheWeek;

        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (node.equals(day)) {
                    int daysToAdd = GridPane.getColumnIndex(node);
                    return localDate.plusDays(daysToAdd - 1);
                }
            }
        }
        return null;

    }

    private LocalTime getAppointmentTime(VBox day) {
        LocalTime localTime = LocalTime.of(9, 0);

        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (node.equals(day)) {
                    int hoursToAdd = GridPane.getRowIndex(node);
                    return localTime.plusHours(hoursToAdd - 1);
                }
            }
        }
        return null;
    }


    private void editAppointment(VBox parent, int appointmentId) {
        Model.getInstance().appointmentToEdit = new AppointmentDAO().getAppointmentbyId(appointmentId);

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/ManageAppointment.fxml"));
            Parent root = null;

            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Manage users");

            ManageAppointmentController controller = fxmlLoader.getController();
            controller.initData(this);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleFilters() {
        List<String> branchFilters = new ArrayList<>();
        for(Node node:branchCheckBoxes.getChildren()){
            if(node instanceof CheckBox){
                if(((CheckBox) node).isSelected()){
                    branchFilters.add(node.getAccessibleText());
                }
            }
        }
        List<String> vetFilters = new ArrayList<>();
        for(Node node:vetCheckBoxes.getChildren()){
            if(node instanceof CheckBox){
                if(((CheckBox) node).isSelected()){
                    vetFilters.add(node.getAccessibleText());
                }
            }
        }
        System.out.println(vetFilters);

        clearAppointmentsGrid();
        int rows = 12;
        int cols = 7;

        LocalDate monday = Model.getInstance().mondayOfTheWeek;
        LocalDate sunday = monday.plusDays(6);
        List<Appointment> filteredAppointments = new AppointmentDAO().getFilteredAppointments(monday, sunday, branchFilters, vetFilters);
        String[] availableHours = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        // method to put in a label;
        for (Appointment a : filteredAppointments) {
            int dayIndex = a.getDate().getDayOfWeek().getValue();
            int hourIndex = a.getTime().getHour();
            String hourIndexString = (hourIndex + ":00");
            for (int i = 0; i < availableHours.length; i++) {
                if (availableHours[i].equals(hourIndexString)) {
                    hourIndex = i + 1;
                }
            }

            Label label = new Label(a.getService() + "-" + a.getPetName());
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            label.getStyleClass().add("appointment-label");

            if (a.getBranch() != null) {
                switch (a.getBranch()) {
                    case "Montejo":
                        label.setStyle("-fx-background-color: cyan");
                        break;

                    case "Urban":
                        label.setStyle("-fx-background-color: goldenrod");
                        break;

                    case "Harbor":
                        label.setStyle("-fx-background-color: greenyellow");
                        break;
                }
            }


            label.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                //editAppointment((VBox) label.getParent(), label.getText(), label.getAccessibleText());
                editAppointment((VBox) label.getParent(), a.getId());
            });

            // Mouse effects
            label.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                label.getScene().setCursor(Cursor.HAND);
            });

            label.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                label.getScene().setCursor(Cursor.DEFAULT);
            });


            VBox vBox = (VBox) getNodeFromGridPane(gridPane, dayIndex, hourIndex);

            vBox.getChildren().add(label);
        }


    }

    private void selectCheckBoxes(VBox vBox, boolean b) {
        for (Node node : vBox.getChildren()) {
            try{
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(b);
            } catch(ClassCastException ignore){

            }
        }
        handleFilters();
    }


    public void selectCheckBoxBranches() {
        selectCheckBoxes(branchCheckBoxes, true);
    }

    public void unselectCheckBoxBranches() {
        selectCheckBoxes(branchCheckBoxes, false);
    }

    public void selectCheckBoxVets() {
        selectCheckBoxes(vetCheckBoxes, true);
    }

    public void unselectCheckBoxVets() {
        selectCheckBoxes(vetCheckBoxes, false);
    }

}
