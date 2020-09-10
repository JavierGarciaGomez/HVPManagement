package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
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
    private List<WorkSchedule> originalWeekWorkschedules;
    private List<WorkSchedule> destinationWeekWorkschedules;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        workScheduleDAO = WorkScheduleDAO.getInstance();
        model = Model.getInstance();
        utilities = Utilities.getInstance();
    }

    public void copy() {
        // check if fields are filled
        if (dtpOriginalWeek.getValue() == null || dtpDestinationWeek.getValue() == null) {
            utilities.showAlert(Alert.AlertType.ERROR, "Data not filled", "We can't process the copy because the dates are not properly selected");
            return;
        }

        int numberOfWeeks = spinNumberOfWeeks.getValue();
        LocalDate originalMonday = utilities.getMondayLocalDate(dtpOriginalWeek.getValue());
        LocalDate destinationFirstMonday = utilities.getMondayLocalDate(dtpDestinationWeek.getValue());
        LocalDate destinationLastSunday = destinationFirstMonday.plusDays((numberOfWeeks*7)-1);

        // make lists from the week data
        Runnable runnable = () -> originalWeekWorkschedules = workScheduleDAO.getWorkSchedulesBetweenDates(originalMonday, originalMonday.plusDays(6));
        Thread originalWorkScheduleThread = new Thread(runnable);
        runnable = () -> destinationWeekWorkschedules = workScheduleDAO.getWorkSchedulesBetweenDates(destinationFirstMonday, destinationLastSunday);
        Thread destinationWorkScheduleThread = new Thread(runnable);
        originalWorkScheduleThread.start();
        destinationWorkScheduleThread.start();

        try {
            originalWorkScheduleThread.join();
            destinationWorkScheduleThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        destinationWeekWorkschedules.clear();

        LocalTime startingTime = LocalTime.now();

        // made the copy
        int daysInBetween = (int) ChronoUnit.DAYS.between(originalMonday, destinationFirstMonday);
        workScheduleDAO.deleteRegistersByDate(destinationFirstMonday, destinationLastSunday);

        // Loop for each repetition
        for (int i = 0; i < numberOfWeeks; i++) {
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

        workScheduleDAO.saveWorkSchedules(destinationWeekWorkschedules);
        LocalTime endingTime = LocalTime.now();
        int secondsLong = (int) ChronoUnit.SECONDS.between(startingTime, endingTime);
        utilities.showAlert(Alert.AlertType.INFORMATION, "Success", "All the data was registered succesfully. It took" +
                secondsLong+" seconds.");
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
        model.selectedLocalDate=destinationFirstMonday;
    }


    public void cancel() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.hide();
    }

}
