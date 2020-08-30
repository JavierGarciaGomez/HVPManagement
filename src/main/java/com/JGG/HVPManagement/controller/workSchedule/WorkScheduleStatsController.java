package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.dao.OpeningHoursDAO;
import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.OpeningHours;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HoursByDateByBranch;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkScheduleStatsController implements Initializable {
    private Model model;
    private Utilities utilities;
    private List<HoursByDateByBranch> hoursByDateByBranches;

    public WorkScheduleStatsController() {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();

    }

    public void countHoursByJobPosition() {
        int hoursDirAdm = 0;
        int hoursDirMed = 0;
        int hoursManager = 0;
        int hoursVetA = 0;
        int hoursVetB = 0;
        int hoursAsistA = 0;
        int hoursAsistB = 0;
        int hoursReceptionist = 0;
        int hoursIntern = 0;
        int hoursCounselor = 0;

        System.out.println(model.tempWorkSchedules);
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Directora administrativa"))) {
                hoursDirAdm++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Director médico"))) {
                hoursDirMed++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Gerente"))) {
                hoursManager++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Veterinario A"))) {
                hoursVetA++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Veterinario B"))) {
                hoursVetB++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Asistente A"))) {
                hoursAsistA++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Asistente B"))) {
                hoursAsistB++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Recepcionista"))) {
                hoursReceptionist++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Pasante médico"))) {
                hoursIntern++;
            }
            if (tempWorkSchedule.getCollaborator().getJobPosition()
                    .equals(utilities.getJobPositionByName("Asesor"))) {
                hoursCounselor++;
            }


        }
        System.out.println("Printing hours " + hoursDirAdm + " " + hoursVetA + " " + hoursVetB);
    }



    public void countMedicalHoursByDateAndBranch() {
        List<JobPosition> jobPositions = new ArrayList<>();
        jobPositions.add(utilities.getJobPositionByName("Director médico"));
        jobPositions.add(utilities.getJobPositionByName("Veterinario A"));
        jobPositions.add(utilities.getJobPositionByName("Veterinario B"));
        jobPositions.add(utilities.getJobPositionByName("Asistente A"));
        jobPositions.add(utilities.getJobPositionByName("Asistente B"));
        jobPositions.add(utilities.getJobPositionByName("Pasante médico"));

        // made a list branch, day, hours
        hoursByDateByBranches = new ArrayList<>();
        for (WorkSchedule workSchedule : model.tempWorkSchedules) {
            if (workSchedule.getWorkingDayType().getItNeedBranches()) {
                for(Branch branch:model.branches){
                    if (workSchedule.getBranch().equals(branch)) {
                        int minutesWorked = (int) ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime());
                        HoursByDateByBranch hoursByDateByBranch = getHoursByDateByBranch(workSchedule.getLocalDate(), workSchedule.getBranch());
                        hoursByDateByBranch.setMinuteSum(hoursByDateByBranch.getMinuteSum() + minutesWorked);
                    }

                }
            }
        }
        for(HoursByDateByBranch hoursByDateByBranch:hoursByDateByBranches){
            System.out.println(hoursByDateByBranch.getBranch());
            System.out.println(hoursByDateByBranch.getLocalDate());
            System.out.println(hoursByDateByBranch.getMinuteSum()/60);
        }
    }

    private HoursByDateByBranch getHoursByDateByBranch(LocalDate localDate, Branch branch) {
        for (HoursByDateByBranch hoursByDateByBranch : hoursByDateByBranches) {
            if (hoursByDateByBranch.getBranch().equals(branch) && hoursByDateByBranch.getLocalDate().equals(localDate)) {
                return hoursByDateByBranch;
            }
        }
        HoursByDateByBranch hoursByDateByBranch = new HoursByDateByBranch(branch, localDate, 0);
        hoursByDateByBranches.add(hoursByDateByBranch);
        return hoursByDateByBranch;

    }





    public static void main(String[] args) {
        System.out.println("STARTING " + LocalTime.now());
        WorkScheduleStatsController workScheduleStatsController = new WorkScheduleStatsController();
        Model model = Model.getInstance();
        if (model.selectedLocalDate == null) {
            model.selectedLocalDate = LocalDate.now();
        }
        model.setMondayDate();
        model.tempWorkSchedules = WorkScheduleDAO.getInstance().getWorkSchedulesByDate(model.mondayOfTheWeek, model.mondayOfTheWeek.plusDays(6));
        System.out.println(model.tempWorkSchedules);
        model.openingHoursList = OpeningHoursDAO.getInstance().getOpeningHoursList();
        System.out.println("STARTING COUNT HOURS " + LocalTime.now());
        workScheduleStatsController.countHoursByJobPosition();
        System.out.println("STARTING RECEPTIONIST" + LocalTime.now());


        System.out.println("FINISHED " + LocalTime.now());
        workScheduleStatsController.countMedicalHoursByDateAndBranch();
    }


}
