package com.JGG.WeeklyScheduler.Tests;

import com.JGG.WeeklyScheduler.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestHibernate {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        System.out.println(session);
        session.beginTransaction();
        User user = session.get(User.class, 1);
        System.out.println(user);
        session.getTransaction().commit();

        System.out.println(user);

    }
}
