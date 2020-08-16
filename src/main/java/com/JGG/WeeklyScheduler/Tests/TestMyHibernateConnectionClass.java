package com.JGG.WeeklyScheduler.Tests;

import com.JGG.WeeklyScheduler.entity.HibernateConnection;
import com.JGG.WeeklyScheduler.entity.User;
import org.hibernate.Session;

import javax.persistence.Query;

public class TestMyHibernateConnectionClass {
    public static void main(String[] args) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session currentSession = hibernateConnection.getSession();
        System.out.println(currentSession);
        User user = new User();
        //user.getUsers();

        // get max ID
        currentSession.close();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(id) from User");
        int maxId= (Integer) query.getSingleResult();
        session.close();
        System.out.println("The max id is "+maxId);




    }
}
