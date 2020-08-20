package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.model.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

public class CalendarControllerOld implements Initializable {


    public BorderPane borderPane;
    public Label tue11;
    public GridPane gridPane;
    public HBox daysOfWeekHeader;
    public VBox hoursHeader;
    public DatePicker datePicker;
    private LocalDate firstWeekDay;

    public CalendarControllerOld() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initVariables
        initVariables();
        // Initialize the grid
        initWeekDaysHeaderPanes();
        loadWeekDaysHeaderLabels();
        initHoursHeader();
        initAppointmentsGrid();



        //todo change method name
        initDatePickerSelector();

        firstWeekDay = this.getFirstWeekDay(LocalDate.now());
        System.out.println(firstWeekDay);
        // todo Appointment appointment = new Appointment("ANV", "pet", "client", "Montejo", "Consulta", "Garrapatas", "2020-12-08", "08:00");
        // todo tue11.setText(appointment.getVeterinarian()+"\n "+appointment.getPetName()+"\n "+appointment.getClientName()+"\n "+appointment.getService());

    }

    private void initVariables() {
        if (Model.getInstance().selectedLocalDate == null) {
            Model.getInstance().selectedLocalDate = LocalDate.now();
        }
        Model.getInstance().setMondayDate();
        Model.getInstance().setLastDayOfMonth();
        System.out.println("Monday date and lastday of month setted: "+Model.getInstance().mondayOfTheWeek+" "+Model.getInstance().lastDayOfMonth);
    }


    private void initAppointmentsGrid() {
        int rows = 12;
        int cols = 7;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //Add a VBox for each cell
                VBox vBox = new VBox();
                //todo change the name and add styles
                vBox.getStyleClass().add("calendar_pane");
                // todo check if neccesary vBox.setMinWidth(wee);
                vBox.setMaxWidth(daysOfWeekHeader.getPrefWidth()/7);
                System.out.println(daysOfWeekHeader.getWidth());
                System.out.println(daysOfWeekHeader.getPrefWidth());
                System.out.println(daysOfWeekHeader.getMaxWidth());
                System.out.println(daysOfWeekHeader.getMinWidth());

                //todo delete label, just for testing
                Label label = new Label();
                label.setText("Label");
                vBox.getChildren().add(label);

                vBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent -> {
                    addAppointments(vBox);
                }));
                // set vGrow to always expand or shrink
                GridPane.setVgrow(vBox, Priority.ALWAYS);
                // Add the Vbox to the grid
                gridPane.add(vBox, j, i);
            }
        }
    }

    private void initWeekDaysHeaderPanes() {
        //todo change comments and variablenames
        // loop for each day
        for (int i = 0; i < Model.getInstance().weekDaysNames.length; i++) {
            // Make new pane
            StackPane stackPane = new StackPane();
            // Make panes take up equal space
            HBox.setHgrow(stackPane, Priority.ALWAYS);
            stackPane.setMaxWidth(Double.MAX_VALUE);
            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            stackPane.setMinWidth(daysOfWeekHeader.getPrefWidth() / 7);
            // Add it to the header
            daysOfWeekHeader.getChildren().add(stackPane);
            // Add weekday name
        }

    }

    private void loadWeekDaysHeaderLabels() {
        // set the first day
        int dayDateLabel = Model.getInstance().mondayOfTheWeek.getDayOfMonth();
        // get the last month day
        int lastDayOfMonth = Model.getInstance().lastDayOfMonth.getDayOfMonth();


        // change to the last month
        // add the label
        // add the week day
        // Go through calendar grid
        for (int i=0; i<Model.getInstance().weekDaysNames.length; i++) {
            StackPane dayHeader = (StackPane) daysOfWeekHeader.getChildren().get(i);
            dayHeader.getChildren().clear();
            // todo style
            dayHeader.setStyle("-fx-backgroud-color: white");
            dayHeader.setStyle("-fx-font: 14px \"System\" ");

            if(dayDateLabel>lastDayOfMonth) dayDateLabel=1;
            Label lbl = new Label(Model.getInstance().weekDaysNames[i]+"\n"+Integer.toString(dayDateLabel));

            lbl.setPadding(new Insets(5));
            lbl.setStyle("-fx-text-fill:darkslategray");

            dayHeader.getChildren().add(lbl);
            dayDateLabel++;
        }

        //lblCount++;
    }

    private void initHoursHeader() {
        //todo change comments and variablenames

        // 7 days in a week
        int hours = 12;

        // Weekday names
        String[] availableHours = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        // loop for each day
        for (int i = 0; i < hours; i++) {
            // Make new pane and label of hour
            StackPane stackPane = new StackPane();
            // todo review styles
            stackPane.getStyleClass().add("weekday-header");

            // Make panes take up equal space
            VBox.setVgrow(stackPane, Priority.ALWAYS);
            stackPane.setMaxWidth(Double.MAX_VALUE);

            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            stackPane.setMinWidth(hoursHeader.getPrefWidth() / 7);

            // Add it to the header
            hoursHeader.getChildren().add(stackPane);

            // Add weekday name
            stackPane.getChildren().add(new Label(availableHours[i]));
        }

    }

    // todo to use the changelistener is not reccomended
    private void initDatePickerSelector() {
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                // Check for null and detect clear() calls
                if (observableValue != null) {

                    //todo monthLabel.settext;
                    // Setting the date
                    Model.getInstance().selectedLocalDate = t1;
                    System.out.println("The date selected is " + Model.getInstance().selectedLocalDate);

                    initVariables();
                    loadWeekDaysHeaderLabels();
                }
            }
        });

    }


    private void addAppointments(VBox vBox) {
        // Todo
        System.out.println("Pushed");
    }


    private LocalDate getFirstWeekDay(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
    }

    private void retrieveData() {

    }


}
