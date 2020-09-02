package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.WorkingDayType;

import java.time.LocalTime;
import java.util.ArrayList;

public class Runnables {
    private final static Runnables instance = new Runnables();
    private Model model = Model.getInstance();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static Runnables getInstance() {
        return instance;
    }

    public void loadMainDatabases() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("STARTING TO RUN RUNNABLES"+LocalTime.now());
                    Thread branchesThread = runBranches();
                    Thread workingDayTypesThread = runWorkingDayTypes();
                    Thread jobPositionsThread = runJobPositions();
                    Thread attendanceRegistersThread = runAttendanceRegisters();
                    Thread workSchedulesThread = runWorkSchedules();
                    Thread openingHoursThread = runOpeningHours();
                    branchesThread.join();
                    workingDayTypesThread.join();

                    model.branchesNames = new ArrayList<>();
                    for (Branch branch : model.branches) {
                        model.branchesNames.add(branch.getName());
                    }
                    model.branchesNamesAndNone = new ArrayList<>(model.branchesNames);
                    model.branchesNamesAndNone.add("None");
                    model.workingDayTypesAbbr = new ArrayList<>();
                    for (WorkingDayType workingDayType : model.workingDayTypes) {
                        model.workingDayTypesAbbr.add(workingDayType.getAbbr());
                    }
                    model.activeAndWorkerCollaborators = new ArrayList<>();
                    for (Collaborator collaborator : model.collaborators) {
                        if (!"Asesor".equals(collaborator.getJobPosition().getName())) {
                            if (collaborator.getActive()) {
                                model.activeAndWorkerCollaborators.add(collaborator);
                            }
                        }
                    }
                    model.activeAndWorkersUserNames = new ArrayList<>();
                    for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
                        model.activeAndWorkersUserNames.add(collaborator.getUser().getUserName());
                    }
                    model.activeAndWorkersUserNamesAndNull = new ArrayList<>(model.activeAndWorkersUserNames);
                    model.activeAndWorkersUserNamesAndNull.add(null);

                    System.out.println("FINISHED TO RUN RUNNABLES"+LocalTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread thread = new Thread(runnable);
        System.out.println("STARTING TO RUN: "+ LocalTime.now());
        thread.start();
        System.out.println("END RUN: "+LocalTime.now());
    }


    public Thread runAppointments() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.appointments = AppointmentDAO.getInstance().getAllApointments();
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
                model.attendanceRegisters = AttendanceRegisterDAO.getInstance().getAttendanceRegisters();
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
                model.branches = BranchDAO.getInstance().getBranches();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }


    public Thread runCollaborators() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.collaborators = CollaboratorDAO.getInstance().getCollaborators();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }


    public Thread runJobPositions() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.jobPositions = JobPositionDAO.getInstance().getJobPositions();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runOpeningHours() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.openingHoursList = OpeningHoursDAO.getInstance().getOpeningHoursList();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runUsers() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.users = UserDAO.getInstance().getUsers();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkingDayTypes() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.workingDayTypes = WorkingDayTypeDAO.getInstance().getWorkingDayTypes();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    public Thread runWorkSchedules() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                model.workSchedulesDBCopy = WorkScheduleDAO.getInstance().getWorkSchedules();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }
}

