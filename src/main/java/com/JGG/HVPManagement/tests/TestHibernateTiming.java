package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.*;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestHibernateTiming {
    public static void main(String[] args) {
        System.out.println("STARTING APPOINTMENT"+ LocalTime.now());
        AppointmentDAO appointmentDAO = AppointmentDAO.getInstance();
        appointmentDAO.getAllApointments();

        System.out.println("STARTING COLLABORATOR"+ LocalTime.now());
        CollaboratorDAO collaboratorDao = CollaboratorDAO.getInstance();
        collaboratorDao.getAllCollaborators();

        System.out.println("STARTING JOB POSITION"+ LocalTime.now());
        JobPositionDAO jobPositionDAO = JobPositionDAO.getInstance();
        jobPositionDAO.getJobPositions();

        System.out.println("STARTING USERS"+ LocalTime.now());
        UserDAO userDAO = UserDAO.getInstance();
        userDAO.getUsers();

        System.out.println("STARTING WORK SCHEDULES"+ LocalTime.now());
        WorkScheduleDAO workScheduleDAO = WorkScheduleDAO.getInstance();
        workScheduleDAO.getWorkSchedulesByDate(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));




    }
}
