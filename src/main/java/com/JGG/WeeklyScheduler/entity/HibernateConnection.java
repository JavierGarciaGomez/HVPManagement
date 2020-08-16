package com.JGG.WeeklyScheduler.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
