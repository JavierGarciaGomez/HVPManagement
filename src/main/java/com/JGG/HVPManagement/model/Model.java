package com.JGG.HVPManagement.model;



import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.entity.WorkSchedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private final static Model instance = new Model();

    // Dates and time
    public int viewing_year;
    public int viewing_week;
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
    public String [] branches = {"Urban", "Harbor", "Montejo"};
    public String [] branchesAndNone = {"Urban", "Harbor", "Montejo", "None"};
    public final ObservableList<String> paymentForms = FXCollections.observableArrayList("Formal", "Informal", "Guaranteed", "Hourly", "Utilities");
    public ObservableList <String> roles = FXCollections.observableArrayList("Admin", "Manager", "User");
    public ObservableList<String> activeAndWorkersuserNamesAndNull;
    public List<Collaborator> activeAndWorkerCollaborators;
    public List<WorkSchedule> workSchedulesOfTheWeek;
    public List<String> activeAndWorkersUserNames;
    public final String [] workingDayTypes = {"ORD", "PER", "ASE", "DES", "VAC", "INC", "IMS", "JUE", "INJ", "PED", "NCO"};
    public final List<String> workingDayTypesWithHour = new ArrayList<>(Arrays.asList("ORD", "PER", "ASE", "INC", "IMS", "JUE", "INJ", "PED"));
    public final List<String> workingDayTypesWithBranch = new ArrayList<>(Arrays.asList("ORD", "PER"));
    public final String[] availableHours = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

    public List<WorkSchedule> tempWorkSchedules;


    // Other
    public final Double degreeBonus = 300.0;
    public collaboratorAccionTypes collaboratorAccionType;
    public enum collaboratorAccionTypes {UPDATE, ADD_NEW, SHOW};

    public boolean hibernateLoaded;




    public static Model getInstance(){
        return instance;
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
