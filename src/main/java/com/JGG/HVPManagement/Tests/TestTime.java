package com.JGG.HVPManagement.Tests;

import com.JGG.HVPManagement.dao.AppointmentDAO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

public class TestTime {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now(); // today
        Locale locale = Locale.getDefault(); // Get the locale values of default (region where is executed)
        WeekFields weekFields = WeekFields.ISO.of(locale); // Get a WeekField standar
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear()); // Get the number of week by the standard

        System.out.println("The week number is"+ weekNumber);

        DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();

        System.out.println("The first day of the week is: "+firstDayOfWeek);
        LocalDate thisMondayDate = date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        System.out.println("The date of monday was "+thisMondayDate);

        // Trying to get the first day of another week
        LocalDate dateNextWeek = LocalDate.now().plusDays(7); // today
        weekNumber = dateNextWeek.get(weekFields.weekOfWeekBasedYear()); // Get the number of week by the standard
        System.out.println("The week number is"+ weekNumber);
        firstDayOfWeek = weekFields.of(locale).getFirstDayOfWeek();
        System.out.println("The first day of the week is: "+firstDayOfWeek);
        LocalDate nextMondayDate = dateNextWeek.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        System.out.println("The date of monday was "+nextMondayDate);

        if(dateNextWeek.compareTo(nextMondayDate)>0){
            System.out.println("date Next week is after");
        } else{
            System.out.println("date Next week is before");
        }

        // 20-08-11 Compare localtimes
        System.out.println("\nCompare local times");
        LocalTime localTime = LocalTime.now();
        LocalTime localTime1 = localTime.plusHours(5);

        System.out.println("Comparing 2 times with 5 hours difference: "+localTime.compareTo(localTime1));

        // 20-08-11 Test AppointTimeDAO getappointments and getappointment
        /*System.out.println("\n test AppointmentDAO getappointment");
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        System.out.println(appointmentDAO.getAllApointments());

        System.out.println(appointmentDAO.getAppointmenByDateTime
                (LocalDate.of(2020, 8, 11), LocalTime.of(18, 47)));*/

        // 20-08-21 Compare localtime null
        System.out.println("20-08-21 Compare localtime null");
        LocalTime aLocalTime = null;
        LocalTime bLocalTime = null;
//        if(aLocalTime.compareTo(bLocalTime)==0){
//            System.out.println("They are equal");
//        }

    }
}
