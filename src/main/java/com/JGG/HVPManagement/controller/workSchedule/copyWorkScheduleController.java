package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Runnables;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class CopyWorkScheduleController implements Initializable {
    public DatePicker dtpOriginalWeek;
    public DatePicker dtpDestinationWeek;
    public Spinner<Integer> spinNumberOfWeeks;
    public AnchorPane rootPane;
    private WorkScheduleDAO workScheduleDAO;
    private Model model;
    private Utilities utilities;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        workScheduleDAO = WorkScheduleDAO.getInstance();
        model = Model.getInstance();
        utilities = Utilities.getInstance();
    }

    public void copy() {
        // check if fields are filled
        System.out.println("Getting value: original: " + dtpOriginalWeek.getValue() + ". Destination: " + dtpDestinationWeek.getValue());
        if (dtpOriginalWeek.getValue() == null || dtpDestinationWeek.getValue() == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Data not filled", "We can't process the copy because the dates are not properly selected");
            return;
        }

        // Retrieve the data
        LocalDate originalMonday = utilities.getMondayLocalDate(dtpOriginalWeek.getValue());
        LocalDate destinationMonday = utilities.getMondayLocalDate(dtpDestinationWeek.getValue());

        //make lists from the week data
        List<WorkSchedule> originalWeekWorkschedules = utilities.getWorkSchedulesBetweenDates(originalMonday, originalMonday.plusDays(6));
        List<WorkSchedule> destinationWeekWorkschedules = utilities.getWorkSchedulesBetweenDates(destinationMonday, destinationMonday.plusDays(6));

        // check if the original week has data
        if (originalWeekWorkschedules.isEmpty()) {
            utilities.showAlert(Alert.AlertType.ERROR, "Data can't be retrieved", "We can't process the copy because the selected original week has no data");
            return;
        }

        // check if the destination week has data
        if (!destinationWeekWorkschedules.isEmpty()) {
            boolean answer = utilities.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "The destination week has already data registered. Do you want to update the data?");
            if (!answer) {
                return;
            }
        }

        LocalTime startingTime = LocalTime.now();

        // made the copy
        int daysInBetween = (int) ChronoUnit.DAYS.between(originalMonday, destinationMonday);

        // Loop for each repetition
        for (int i = 0; i < spinNumberOfWeeks.getValue(); i++) {
            // add to the arraylist for each loop
            for (WorkSchedule copiedWorkSchedule : originalWeekWorkschedules) {
                WorkSchedule newWorkSchedule = new WorkSchedule();

                // change the date with two parameters: difference in days between original an destination, plus 7 days for each repetition
                LocalDate newLocalDate = copiedWorkSchedule.getLocalDate().plusDays(daysInBetween + (7 * i));
                newWorkSchedule.setLocalDate(newLocalDate);
                newWorkSchedule.setRegisteredBy(model.loggedUser.getCollaborator());
                newWorkSchedule.setBranch(copiedWorkSchedule.getBranch());
                newWorkSchedule.setCollaborator(copiedWorkSchedule.getCollaborator());
                newWorkSchedule.setWorkingDayType(copiedWorkSchedule.getWorkingDayType());
                if(copiedWorkSchedule.getStartingLDT()!=null && copiedWorkSchedule.getEndingLDT()!=null){
                    newWorkSchedule.setStartingLDT(copiedWorkSchedule.getStartingLDT().plusDays(daysInBetween + (7 * i)));
                    newWorkSchedule.setEndingLDT(copiedWorkSchedule.getEndingLDT().plusDays(daysInBetween + (7 * i)));
                }
                destinationWeekWorkschedules.add(newWorkSchedule);
            }
        }

        // save or update the data
        workScheduleDAO.createOrReplaceRegisters(destinationWeekWorkschedules);
        utilities.loadWorkSchedules();
        LocalTime endingTime = LocalTime.now();
        int secondsLong = (int) ChronoUnit.SECONDS.between(startingTime, endingTime);
        utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "All the data was registered succesfully. It took" +
                secondsLong+" seconds.");
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
        model.selectedLocalDate=destinationMonday;
    }


    public void cancel() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
    }

}
