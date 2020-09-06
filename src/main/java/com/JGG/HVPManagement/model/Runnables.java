package com.JGG.HVPManagement.model;

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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                utilities.loadAttendanceRegisters();
            }
        };
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
        Runnable runnable = () -> utilities.loadOpeningHours();
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
}

