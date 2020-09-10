package com.JGG.HVPManagement.model;



import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private final static Model instance = new Model();

    // Formatters
    public final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public final DateTimeFormatter DTFday = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public final DateTimeFormatter DTFHHMM = DateTimeFormatter.ofPattern("HH:mm");

    //Databases and extracted lists
    public List<Appointment> appointments;
    public List<AttendanceRegister> attendanceRegisters;
    public List<Branch> branches;
    public List<Collaborator> collaborators;
    public List<Incident> incidents;
    public List<JobPosition> jobPositions;
    public List<OpeningHours> openingHoursList;
    public List<User> users;
    public List<WorkingDayType> workingDayTypes;
    public List<WorkSchedule> workSchedules;

    // created with databases
    public List<Branch> branchesAndNone;
    public List<String> branchesNames;
    public List<String> branchesNamesAndNone;
    public List<Collaborator> activeAndWorkerCollaborators;
    public List<Collaborator> activeAndWorkerCollaboratorsAndNull;
    public List<String> allUserNames;
    public List<String> activeAndWorkersUserNames;
    public List<String> activeAndWorkersUserNamesAndNull;
    public List<String> workingDayTypesAbbr;

    // temporary variables
    public List<Collaborator> activeCollaborators;
    public Collaborator selectedCollaborator;
    public Appointment appointmentToEdit;
    public User loggedUser;
    public List<WorkSchedule> tempWorkSchedules;
    public LocalDate selectedLocalDate;
    public LocalDate mondayOfTheWeek;
    public LocalDate lastDayOfMonth;
    public Branch selectedBranch;
    public role roleView;
    public Incident.incidentTypes incidentType;
    public List<OpeningHoursDetailed> tempOpeningHoursDetailedList;
    public String errorList;
    public String warningList;
    public boolean hasErrors;
    public boolean hasWarnings;
    public LocalDateTime appointmentDateTime;
    public List<LocalTime> availableHours;



    public enum views {BRANCH_VIEW, COLLABORATOR_VIEW}
    public views selectedView;


    // constants
    public final Double degreeBonus = 300.0;
    public final String [] weekDaysNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public final ObservableList<String> paymentForms = FXCollections.observableArrayList("Formal", "Informal", "Guaranteed", "Hourly", "Utilities");
    // other

    // todo delete
    public String [] branchesNamesOld = {"Urban", "Harbor", "Montejo"};
    // todo delete
    public final String[] availableHoursOld = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

    // Other
    public enum role {ADMIN, MANAGER, USER, GUEST_USER}
    public List <role> roles = new ArrayList<>(Arrays.asList(role.values()));
    public boolean openMainAfterLogin;
    public LocalTime testStart;
    public LocalTime testFinish;



    public static Model getInstance(){
        return instance;
    }

    public Model() {
    }

    public static void main(String[] args) {
        System.out.println(role.ADMIN);
        System.out.println(role.MANAGER);
    }

}

