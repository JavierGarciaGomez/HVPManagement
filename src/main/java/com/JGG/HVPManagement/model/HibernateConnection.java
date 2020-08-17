package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;

public class HibernateConnection {
    private static SessionFactory factory;
    private Session session;
    private static HibernateConnection hibernateConnection;

    private HibernateConnection(){
        factory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(TimeRegister.class)
                .addAnnotatedClass(Appointment.class)
                .addAnnotatedClass(Collaborator.class)
                .addAnnotatedClass(DetailedCollaboratorInfo.class)
                .addAnnotatedClass(WorkConditionsInfo.class)
                .addAnnotatedClass(JobPosition.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        System.out.println("Printing from constructor "+session);
    }

    public static HibernateConnection getInstance(){
        if (hibernateConnection==null){
            hibernateConnection = new HibernateConnection();
        }
        return hibernateConnection;
    }

    public Session getSession(){
        session = factory.getCurrentSession();
        return session = factory.getCurrentSession();
    }

    public void closeSession(){
        session.close();
    }


}
