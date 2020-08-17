package com.JGG.HVPManagement.model;



import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class Model {
    private final static Model instance = new Model();
    public int viewing_year;
    public int viewing_week;
    public LocalDate selectedLocalDate;
    public LocalDate mondayOfTheWeek;
    public LocalDate lastDayOfMonth;
    public final String [] weekDaysNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public LocalDate appointmentDate;
    public LocalTime appontimenTime;
    public Appointment appointmentToEdit;
    public String [] branches = {"Urban", "Harbor", "Montejo"};
    public boolean hibernateLoaded;
    public User loggedUser;
    public Collaborator selectedColaborator;
    public final ObservableList<String> paymentForms = FXCollections.observableArrayList("Formal", "Informal", "Guaranteed", "Hourly", "Utilities");


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
