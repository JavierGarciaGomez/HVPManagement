package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.model.HibernateConnection;

import java.time.LocalTime;

public class TestHibernateTiming {
    private static LocalTime startappointmnent;
    private static LocalTime endAppointment;
    private static LocalTime startAttendance;
    private static LocalTime endAttendance;
    private static LocalTime startBranch;
    private static LocalTime endBranch;
    private static LocalTime startCollaborator;
    private static LocalTime endCollaborator;
    private static LocalTime startJobPosition;
    private static LocalTime endJobPosition;
    private static LocalTime startOpeningHours;
    private static LocalTime endOpeningHours;
    private static LocalTime startUser;
    private static LocalTime endUser;
    private static LocalTime startWorkingDayType;
    private static LocalTime endWorkingDayType;
    private static LocalTime startWorkSchedules;
    private static LocalTime endWorkSchedules;



    public static void main(String[] args) throws InterruptedException {
        LocalTime timeTeststartingAll = LocalTime.now();
        LocalTime startingHibernate=LocalTime.now();
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        LocalTime endingHibernate=LocalTime.now();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startappointmnent=LocalTime.now();
                AppointmentDAO.getInstance().getAllApointments();
                endAppointment=LocalTime.now();
            }
        };
        Thread appointTHD = new Thread(runnable);
        appointTHD.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startAttendance=LocalTime.now();
                AttendanceRegisterDAO.getInstance().getAttendanceRegisters();
                endAttendance=LocalTime.now();
            }
        };
        Thread attendTh = new Thread(runnable);
        attendTh.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startBranch=LocalTime.now();
                BranchDAO.getInstance().getBranches();
                endBranch=LocalTime.now();
            }
        };
        Thread branchTh = new Thread(runnable);
        branchTh.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startCollaborator=LocalTime.now();
                CollaboratorDAO.getInstance().getCollaborators();
                endCollaborator=LocalTime.now();
            }
        };
        Thread colthread = new Thread(runnable);
        colthread.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startJobPosition=LocalTime.now();
                JobPositionDAO.getInstance().getJobPositions();
                endJobPosition=LocalTime.now();
            }
        };
        Thread jobth = new Thread(runnable);
        jobth.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startOpeningHours=LocalTime.now();
                OpeningHoursDAO.getInstance().getOpeningHoursList();
                endOpeningHours=LocalTime.now();
            }
        };
        Thread openingHours = new Thread(runnable);
        openingHours.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startUser=LocalTime.now();
                UserDAO.getInstance().getUsers();
                endUser=LocalTime.now();
            }
        };
        Thread userth = new Thread(runnable);
        userth.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startWorkingDayType=LocalTime.now();
                WorkingDayTypeDAO.getInstance().getWorkingDayTypes();
                endWorkingDayType=LocalTime.now();
            }
        };
        Thread wdtth = new Thread(runnable);
        wdtth.start();

        runnable = new Runnable() {
            @Override
            public void run() {
                startWorkSchedules=LocalTime.now();
                WorkScheduleDAO.getInstance().getWorkSchedules();
                endWorkSchedules=LocalTime.now();
            }
        };
        Thread workScheduleTH = new Thread(runnable);
        workScheduleTH.start();

        appointTHD.join();
        attendTh.join();
        branchTh.join();
        colthread.join();
        jobth.join();
        openingHours.join();
        userth.join();
        wdtth.join();
        workScheduleTH.join();
        System.out.println("TTSTARTING ALL"+timeTeststartingAll);
        System.out.println("TTSTARTING HIB"+startingHibernate);
        System.out.println("TTENDING HIB"+endingHibernate);
        System.out.println("Appointment"+startappointmnent);
        System.out.println("Appointment"+endAppointment);
        System.out.println("Attendance"+startAttendance);
        System.out.println("Attendance"+endAttendance);
        System.out.println("Branch"+startBranch);
        System.out.println("Branch"+endBranch);
        System.out.println("Collaborator"+startCollaborator);
        System.out.println("Collaborator"+endCollaborator);
        System.out.println("Job Position"+startJobPosition);
        System.out.println("Job Position"+endJobPosition);
        System.out.println("Opening"+startOpeningHours);
        System.out.println("Opening"+endOpeningHours);
        System.out.println("USER"+startUser);
        System.out.println("USER"+endUser);
        System.out.println("WDT"+startWorkingDayType);
        System.out.println("WDY"+endWorkingDayType);
        System.out.println("Workschedule"+startWorkSchedules);
        System.out.println("Workschedule"+endWorkSchedules);
        System.out.println("END ALL"+LocalTime.now());

    }
}
