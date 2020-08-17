package com.JGG.HVPManagement.Tests;

import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

public class TestHibernate {
    public static void main(String[] args) {
        Session session = HibernateConnection.getInstance().getSession();
        System.out.println(session);
        session.beginTransaction();
        User user = session.get(User.class, 1);
        System.out.println(user);
        session.getTransaction().commit();

        System.out.println(user);

    }
}
