package com.JGG.HVPManagement.controller.schedule;

import com.JGG.HVPManagement.dao.AppointmentDAO;
import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class CalendarController implements Initializable {
    public GridPane gridPane;
    public DatePicker datePicker;
    public VBox branchCheckBoxes;
    public VBox vetCheckBoxes;
    private final Model model = Model.getInstance();
    private final Utilities utilities = Utilities.getInstance();
    private final AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();
    private List<Appointment> appointmentsInTheWeek;
    List<String> branchFilters;
    List<String> vetFilters;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshVariables();
        addCheckBoxes();
        initGrid();
        load();
    }

    private void refreshVariables() {
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        utilities.setMondayDate();
        utilities.setLastDayOfMonth();

        if(branchFilters==null && vetFilters==null){
            appointmentsInTheWeek = appointmentDAO.getAppointmentsBetweenDates(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
        } else{
            appointmentsInTheWeek = appointmentDAO.getFilteredAppointments(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6), branchFilters, vetFilters);
        }
        model.availableHours = utilities.getAvailableHoursByDates(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
    }

    // This methods set the constraints and insert a Pane for each cell
    private void addCheckBoxes() {
        CheckBox checkBox;
        // todo change branches
        for (Branch branch : model.branches) {
            checkBox = new CheckBox(branch.getName());
            branchCheckBoxes.getChildren().add(checkBox);
            checkBox.addEventHandler(ActionEvent.ACTION, (actionEvent -> handleFilters()));
            checkBox.setSelected(true);
        }

        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            checkBox = new CheckBox(collaborator.getUser().getUserName());
            vetCheckBoxes.getChildren().add(checkBox);
            checkBox.addEventHandler(ActionEvent.ACTION, (actionEvent -> handleFilters()));
            checkBox.setSelected(true);
        }
    }

    private void initGrid() {
        initGridAndSetConstraints();
        addHeadersDaysPanes();
        addHeaderHoursPanesAndLabels();
        addAppointmentsGridPanes();
    }

    private void initGridAndSetConstraints() {
        for (int i = 0; i < model.availableHours.size()+1; i++) {
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
        // loop for each day
        for (int i = 1; i < gridPane.getColumnCount(); i++) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("grid-header");

            HBox.setHgrow(stackPane, Priority.ALWAYS);

            stackPane.setMinWidth(gridPane.getPrefWidth() / 8);
            gridPane.add(stackPane, i, 0);
        }
    }

    private void addHeaderHoursPanesAndLabels() {
        for (int i = 1; i < gridPane.getRowCount(); i++) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("grid-header");

            HBox.setHgrow(stackPane, Priority.ALWAYS);
            stackPane.setMaxWidth(Double.MAX_VALUE);
            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            stackPane.setMinWidth(gridPane.getPrefWidth() / gridPane.getRowCount());
            // Create label and add it
            Label lbl = new Label(model.availableHours.get(i-1).toString());
            lbl.setPadding(new Insets(2));

            stackPane.getChildren().add(lbl);
            // add the pane to the grid
            gridPane.add(stackPane, 0, i);
            // Add weekday name
        }
    }

    private void addAppointmentsGridPanes() {
        // set a VBox for each grid
        for (int i = 1; i < gridPane.getRowCount(); i++) {
            for (int j = 1; j < gridPane.getColumnCount(); j++) {
                //Add a VBox for each cell
                VBox vBox = new VBox();
                //todo change the name and add styles
                vBox.getStyleClass().add("calendar_pane");
                // todo check if neccesary
                vBox.setMinWidth(gridPane.getPrefWidth() / 8);

                vBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent -> addAppointment(vBox)));
                // set vGrow to always expand or shrink
                GridPane.setVgrow(vBox, Priority.ALWAYS);
                // Add the Vbox to the grid
                gridPane.add(vBox, j, i);
            }
        }

    }

    private void load() {
        loadHoursHeaderLabels();
        loadWeekDaysHeaderLabels();
        loadAppointmentsGrid();
    }

    private void loadHoursHeaderLabels() {
        for (int i = 1; i < gridPane.getRowCount(); i++) {
            StackPane hourHeader = (StackPane) utilities.getNodeFromGridPane(gridPane, 0, i);
            hourHeader.getChildren().clear();
            Label lbl = new Label(model.availableHours.get(i-1).toString());
            lbl.setPadding(new Insets(2));
            hourHeader.getChildren().add(lbl);
        }
    }


    private void loadWeekDaysHeaderLabels() {
        for (int i = 1; i < gridPane.getColumnCount(); i++) {
            LocalDate localDate = model.mondayOfTheWeek.plusDays(i-1);
            // retrieve pane for each grid
            StackPane dayHeader = (StackPane) utilities.getNodeFromGridPane(gridPane, i, 0);
            dayHeader.getChildren().clear();

            Label lbl = new Label(model.weekDaysNames[i - 1] + "\n" + localDate.getDayOfMonth() +
                    "/" + localDate.getMonthValue());

            dayHeader.getChildren().add(lbl);
        }
    }

    private void loadAppointmentsGrid() {
        clearAppointmentsGrid();
        // method to put in a label;
        for (Appointment a : appointmentsInTheWeek) {
            LocalDate localDate = a.getDate();
            LocalTime localTime = a.getTime().withMinute(0);
            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
            localDate = utilities.getMexicanDate(localDateTime);
            int dayIndex = localDate.getDayOfWeek().getValue();
            int hourIndex = model.availableHours.indexOf(localTime)+1;
            loadAppointmentLabel(a, dayIndex, hourIndex);
        }
    }

    private void clearAppointmentsGrid() {
        for (int j = 1; j < gridPane.getColumnCount(); j++) {
            for (int i = 1; i < gridPane.getRowCount(); i++) {
                VBox vBox = (VBox) utilities.getNodeFromGridPane(gridPane, j, i);
                vBox.getChildren().clear();
            }
        }
    }

    private void loadAppointmentLabel(Appointment a, int dayIndex, int hourIndex) {
        Label label = new Label(a.getService() + "-" + a.getPetName());
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.getStyleClass().add("appointment-label");

        if (a.getBranch() != null) {
            switch (a.getBranch().getName()) {
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

        label.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> editAppointment(a.getId()));

        label.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> label.getScene().setCursor(Cursor.HAND));

        label.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> label.getScene().setCursor(Cursor.DEFAULT));

        VBox vBox = (VBox) utilities.getNodeFromGridPane(gridPane, dayIndex, hourIndex);
        vBox.getChildren().add(label);
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

    @FXML
    public void updateSchedule() {
        model.selectedLocalDate = datePicker.getValue();
        reLoad();
    }


    private void reLoad(){
        refreshVariables();
        load();
    }

    public void createAppointment() {
        VBox vBox= null;
        addAppointment(vBox);
    }

    public void generateSnapshot() {
        WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
        File file = new File("res\\saves\\" + model.mondayOfTheWeek.toString() + " Schedule.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addAppointment(VBox vBox) {
        if(vBox==null){
            model.appointmentDateTime=LocalDateTime.now();
        } else{
            int vBoxColumnIndex = GridPane.getColumnIndex(vBox);
            int vBoxRowIndex = GridPane.getRowIndex(vBox);
            LocalTime appointmentTime = model.availableHours.get(vBoxRowIndex-1);
            LocalDate appointmentDate = model.mondayOfTheWeek.plusDays(vBoxColumnIndex-1);
            LocalDateTime localDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
            model.appointmentDateTime = utilities.adjustDate(localDateTime);
        }

        // todo use an utility
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/schedule/ManageAppointment.fxml"));
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

    private void editAppointment(int appointmentId) {
        model.appointmentToEdit = appointmentDAO.getAppointmentbyId(appointmentId);
        try {
            // todo use utilities. and create open modalwindow
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/schedule/ManageAppointment.fxml"));
            Parent root;

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
        branchFilters = new ArrayList<>();
        for(Node node:branchCheckBoxes.getChildren()){
            if(node instanceof CheckBox){
                CheckBox chkBranch = (CheckBox) node;
                if(chkBranch.isSelected()){
                    branchFilters.add(chkBranch.getText());
                }
            }
        }

        vetFilters = new ArrayList<>();
        for(Node node:vetCheckBoxes.getChildren()){
            if(node instanceof CheckBox){
                CheckBox chkUser = (CheckBox) node;
                if(chkUser.isSelected()){
                    vetFilters.add(chkUser.getText());
                }
            }
        }
        reLoad();
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
