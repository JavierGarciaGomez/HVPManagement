package com.JGG.HVPManagement.model;



import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private final static Model instance = new Model();

    public final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    // Dates and time
    public LocalDate selectedLocalDate;
    public LocalDate mondayOfTheWeek;
    public LocalDate lastDayOfMonth;
    public final String [] weekDaysNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public LocalDate appointmentDate;
    public LocalTime appontimenTime;

    // Entities
    public  Collaborator selectedCollaborator;
    public Appointment appointmentToEdit;
    public User loggedUser;

    // Lists
    public List<Branch> branches;
    public List<String> branchesNames;
    public List<String> branchesNamesAndNone;
    public List<OpeningHours> openingHoursList;
    public List<WorkSchedule> workSchedules;


    public String [] branchesNamesOld = {"Urban", "Harbor", "Montejo"};
    public String [] branchesAndNone = {"Urban", "Harbor", "Montejo", "None"};
    public final ObservableList<String> paymentForms = FXCollections.observableArrayList("Formal", "Informal", "Guaranteed", "Hourly", "Utilities");
    public ObservableList <String> roles = FXCollections.observableArrayList("Admin", "Manager", "User");
    public ObservableList<String> activeAndWorkersuserNamesAndNull;
    public List<Collaborator> activeAndWorkerCollaborators;
    public List<String> activeAndWorkersUserNames;
    // todo delete
    public final String[] availableHoursOld = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

    public List<WorkSchedule> tempWorkSchedules;


    // Other
    public final Double degreeBonus = 300.0;
    public collaboratorAccionTypes collaboratorAccionType;
    public Branch selectedBranch;
    // todo change name
    public List<WorkingDayType> workingDayTypeList;
    public List<String> workingDayTypesAbbr;
    public List<JobPosition> jobPositions;
    public List<AttendanceRegister> attendanceRegisters;

    public enum collaboratorAccionTypes {UPDATE, ADD_NEW, SHOW};

    public boolean hibernateLoaded;
    public boolean openMainAfterLogin;

    public static Model getInstance(){
        return instance;
    }

    public Model() {
        // todo check where to load all this things
        branches=BranchDAO.getInstance().getBranches();
        branchesNames =new ArrayList<>();
        for(Branch branch: branches){
            branchesNames.add(branch.getName());
        }
        branchesNamesAndNone = new ArrayList<>(branchesNames);
        branchesNamesAndNone.add("None");
        workingDayTypeList = WorkingDayTypeDAO.getInstance().getWorkingDayTypes();

        workingDayTypesAbbr = new ArrayList<>();
        for(WorkingDayType workingDayType: workingDayTypeList){
            workingDayTypesAbbr.add(workingDayType.getAbbr());
        }

        jobPositions = JobPositionDAO.getInstance().getJobPositions();
        attendanceRegisters = AttendanceRegisterDAO.getInstance().getAttendanceRegisters();
        workSchedules = WorkScheduleDAO.getInstance().getWorkSchedules();
        activeAndWorkerCollaborators = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        activeAndWorkersUserNames = new ArrayList<>();
        for(Collaborator collaborator:activeAndWorkerCollaborators){
            activeAndWorkersUserNames.add(collaborator.getUser().getUserName());
        }





    }


    public void setMondayDate() {
        if(selectedLocalDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) mondayOfTheWeek=selectedLocalDate;
        else mondayOfTheWeek = selectedLocalDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
    }

    public void setLastDayOfMonth(){
        lastDayOfMonth = selectedLocalDate.with(TemporalAdjusters.lastDayOfMonth());
    }


    public String formatToMoney(double positionWage) {
        String string = "$ "+String.format("%.2f", positionWage);
        return string;
    }
}
