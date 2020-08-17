package com.JGG.WeeklyScheduler.model;



import com.JGG.WeeklyScheduler.entity.Appointment;
import com.JGG.WeeklyScheduler.entity.User;

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


}
