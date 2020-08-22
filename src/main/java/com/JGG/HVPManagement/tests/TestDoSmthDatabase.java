package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestDoSmthDatabase {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Collaborator collaborator = CollaboratorDAO.getInstance().getCollaboratorbyId(9);
        session.delete(collaborator);
        session.getTransaction().commit();
        System.out.println("Done");
    }
}
