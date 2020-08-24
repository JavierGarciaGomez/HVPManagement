package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.model.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestHibernateTiming {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("STARTING COLLABORATOR"+ LocalTime.now());
                CollaboratorDAO collaboratorDao = CollaboratorDAO.getInstance();
                Model.getInstance().activeAndWorkerCollaborators =collaboratorDao.getActiveAndWorkerCollaborators();
                System.out.println("FINISHED COLLABORATOR"+ LocalTime.now());
            }
        };
        new Thread(runnable).start();

        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("STARTING APPOINTMENT"+ LocalTime.now());
                AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();
                appointmentDAO.getAllApointments();
                System.out.println("FINISHED APPOINTMENT"+ LocalTime.now());
            }
        };
        new Thread(runnable).start();

        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("STARTING JOB POSITION"+ LocalTime.now());
                JobPositionDAO jobPositionDAO = JobPositionDAO.getInstance();
                jobPositionDAO.getJobPositions();
                System.out.println("FINISHED JOB POSITION"+ LocalTime.now());
            }
        };
        new Thread(runnable).start();

        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("STARTING USERS"+ LocalTime.now());
                UserDAO userDAO = UserDAO.getInstance();
                userDAO.getUsers();
                System.out.println("FINISHED USERS"+ LocalTime.now());
            }
        };
        new Thread(runnable).start();

        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("STARTING WORK SCHEDULES"+ LocalTime.now());
                WorkScheduleDAO workScheduleDAO = WorkScheduleDAO.getInstance();
                workScheduleDAO.getWorkSchedulesByDate(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
                System.out.println("FINISHED WORK SCHEDULES"+ LocalTime.now());
            }
        };
        new Thread(runnable).start();

        Model.getInstance().activeAndWorkerCollaborators =CollaboratorDAO.getInstance().getActiveAndWorkerCollaborators();
        for (Collaborator collaborator:Model.getInstance().activeAndWorkerCollaborators){
            System.out.println(collaborator.getUser().getUserName()+collaborator.getWorkingConditions().getStartingDate());
        }













    }
}
