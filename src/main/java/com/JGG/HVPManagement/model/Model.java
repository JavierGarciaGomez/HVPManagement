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
import java.util.List;

public class Model {
    private final static Model instance = new Model();

    public final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public final DateTimeFormatter DTFHHMM = DateTimeFormatter.ofPattern("HH:mm");

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
    public List<WorkSchedule> workSchedulesDBCopy;


    public String [] branchesNamesOld = {"Urban", "Harbor", "Montejo"};
    public String [] branchesAndNone = {"Urban", "Harbor", "Montejo", "None"};
    public final ObservableList<String> paymentForms = FXCollections.observableArrayList("Formal", "Informal", "Guaranteed", "Hourly", "Utilities");
    public ObservableList <String> roles = FXCollections.observableArrayList("Admin", "Manager", "User");
    public ObservableList<String> activeAndWorkersUserNamesAndNullOld;
    public List<Collaborator> activeAndWorkerCollaborators;
    public List<String> activeAndWorkersUserNames;
    public List<String> activeAndWorkersUserNamesAndNull;
    // todo delete
    public final String[] availableHoursOld = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

    public List<WorkSchedule> tempWorkSchedules;


    // Other
    public final Double degreeBonus = 300.0;
    public collaboratorAccionTypes collaboratorAccionType;
    public Branch selectedBranch;
    // todo change name
    public List<WorkingDayType> workingDayTypes;
    public List<String> workingDayTypesAbbr;
    public List<JobPosition> jobPositions;
    public List<AttendanceRegister> attendanceRegisters;
    public List<Appointment> appointments;
    public List<Collaborator> collaborators;
    public List<User> users;
    public List<String> allUserNames;

    public enum collaboratorAccionTypes {UPDATE, ADD_NEW, SHOW};

    public boolean hibernateLoaded;
    public boolean openMainAfterLogin;
    public LocalTime testStart;
    public LocalTime testFinish;

    public static Model getInstance(){
        return instance;
    }

    public Model() {
        // todo check where to load all this things
        /*Runnables runnables =Runnables.getInstance();
        Thread branchesThread = runnables.runBranches();
        Thread collaboratorsThread = runnables.runCollaborators();
        Thread workingDayTypesThread = runnables.runWorkingDayTypes();
        Thread jobPositionsThread = runnables.runJobPositions();
        Thread attendanceRegistersThread = runnables.runAttendanceRegisters();
        Thread workSchedulesThread = runnables.runWorkSchedules();

        try {
            branchesThread.join();
            workingDayTypesThread.join();
            collaboratorsThread.join();

            branchesNames =new ArrayList<>();
            for(Branch branch: branches){
                branchesNames.add(branch.getName());
            }
            branchesNamesAndNone = new ArrayList<>(branchesNames);
            branchesNamesAndNone.add("None");
            workingDayTypesAbbr = new ArrayList<>();
            for(WorkingDayType workingDayType: workingDayTypes){
                workingDayTypesAbbr.add(workingDayType.getAbbr());
            }
            activeAndWorkerCollaborators = new ArrayList<>();
            for(Collaborator collaborator:collaborators){
                if(!"Asesor".equals(collaborator.getJobPosition().getName())){
                    if(collaborator.getActive()){
                        activeAndWorkerCollaborators.add(collaborator);
                    }
                }
            }
            activeAndWorkersUserNames = new ArrayList<>();
            for(Collaborator collaborator:activeAndWorkerCollaborators){
                activeAndWorkersUserNames.add(collaborator.getUser().getUserName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

*/

/*
        branches=BranchDAO.getInstance().getBranches();
        branchesNames =new ArrayList<>();
        for(Branch branch: branches){
            branchesNames.add(branch.getName());
        }
        branchesNamesAndNone = new ArrayList<>(branchesNames);
        branchesNamesAndNone.add("None");
        workingDayTypes = WorkingDayTypeDAO.getInstance().getWorkingDayTypes();

        workingDayTypesAbbr = new ArrayList<>();
        for(WorkingDayType workingDayType: workingDayTypes){
            workingDayTypesAbbr.add(workingDayType.getAbbr());
        }

        jobPositions = JobPositionDAO.getInstance().getJobPositions();
        attendanceRegisters = AttendanceRegisterDAO.getInstance().getAttendanceRegisters();
        workSchedules = WorkScheduleDAO.getInstance().getWorkSchedules();
        activeAndWorkerCollaborators = CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        activeAndWorkersUserNames = new ArrayList<>();
        for(Collaborator collaborator:activeAndWorkerCollaborators){
            activeAndWorkersUserNames.add(collaborator.getUser().getUserName());
        }*/





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
