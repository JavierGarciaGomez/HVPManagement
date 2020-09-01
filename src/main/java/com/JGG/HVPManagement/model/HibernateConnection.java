package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalTime;

public class HibernateConnection {
    private static SessionFactory factory;
    private Session session;
    private static HibernateConnection hibernateConnection;

    private HibernateConnection() {
        System.out.println("Before configuration "+ LocalTime.now());
        factory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AttendanceRegister.class)
                .addAnnotatedClass(Appointment.class)
                .addAnnotatedClass(Collaborator.class)
                .addAnnotatedClass(DetailedCollaboratorInfo.class)
                .addAnnotatedClass(WorkingConditions.class)
                .addAnnotatedClass(JobPosition.class)
                .addAnnotatedClass(WorkSchedule.class)
                .addAnnotatedClass(WorkingDayType.class)
                .addAnnotatedClass(Branch.class)
                .addAnnotatedClass(OpeningHours.class)
                .buildSessionFactory();
    }

    public static HibernateConnection getInstance() {
        if (hibernateConnection == null) {
            hibernateConnection = new HibernateConnection();
        }
        return hibernateConnection;
    }

    public Session getSession() {
        session = factory.getCurrentSession();
        return session = factory.getCurrentSession();
    }



}
