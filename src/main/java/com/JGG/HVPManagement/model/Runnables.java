package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.Collaborator;

import java.time.LocalDate;
import java.time.LocalTime;

public class Runnables {
    private final static Runnables instance = new Runnables();
    private final Utilities utilities = Utilities.getInstance();

    public static Runnables getInstance() {
        return instance;
    }

    public void loadMainDatabases() {
        Runnable runnable = () -> {
            runBranches();
            runWorkingDayTypes();
            runJobPositions();
            runAttendanceRegisters();
            runWorkSchedules();
            runOpeningHours();
            //runIncidents();
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    public Thread runAppointments() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                utilities.loadAppointments();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runAttendanceRegisters() {
        Runnable runnable = utilities::loadAttendanceRegisters;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runAttendanceRegistersBetweenDates(LocalDate startDate, LocalDate endDate) {
        Runnable runnable = () -> utilities.loadTempCollaboratorAttendanceRegisters(startDate, endDate);
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runAttendanceRegistersBetweenDatesByCollaborator(LocalDate startDate, LocalDate endDate, Collaborator collaborator){
        Runnable runnable = () -> utilities.loadTempCollaboratorAttendanceRegisters(startDate, endDate, collaborator);
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runBranches() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                utilities.loadBranches();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runCollaborators() {
        Runnable runnable = utilities::loadCollaborators;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runActiveCollaborators() {
        Runnable runnable = utilities::loadActiveCollaborators;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }


    private Thread runIncidents() {
        Runnable runnable = utilities::loadIncidents;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runJobPositions() {
        Runnable runnable = utilities::loadJobPositions;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runOpeningHours() {
        Runnable runnable = utilities::loadOpeningHours;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runUsers() {
        Runnable runnable = utilities::loadUsers;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkingDayTypes() {
        Runnable runnable = utilities::loadWorkingDayTypes;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkSchedules() {
        Runnable runnable = utilities::loadWorkSchedules;
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkSchedulesBetweenDates(LocalDate startDate, LocalDate endDate) {
        Runnable runnable = () -> utilities.loadTempWorkSchedules(startDate, endDate);
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkSchedulesBetweenDatesByCollaborator(LocalDate startDate, LocalDate endDate, Collaborator collaborator){
        Runnable runnable = () -> utilities.loadTempCollaboratorWorkSchedules(startDate, endDate, collaborator);
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}

