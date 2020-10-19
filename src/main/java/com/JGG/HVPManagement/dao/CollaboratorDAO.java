package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.List;

public class CollaboratorDAO {
    // todo delete static
    // todo change all utilities instances
    private final static CollaboratorDAO instance = new CollaboratorDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static CollaboratorDAO getInstance() {
        return instance;
    }

    public void createOrUpdateCollaborator(Collaborator collaborator) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            session.saveOrUpdate(collaborator);
            session.getTransaction().commit();
            System.out.println("Inserting new collaborator" + collaborator);
            // 20200824         session.close();
        }
    }

    public List<Collaborator> getCollaborators() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c join fetch c.user join fetch c.workingConditions join fetch c.detailedCollaboratorInfo join fetch c.jobPosition", Collaborator.class);
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getActiveAndWorkerCollaborator()\n" + collaborators);
            // 20200824 session.close();
            return collaborators;
        }
    }

    public List<Collaborator> getActiveCollaborators() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c " +
                    "join fetch c.user join fetch c.workingConditions join fetch c.detailedCollaboratorInfo join fetch c.jobPosition " +
                    "where c.isActive=true", Collaborator.class);
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getActiveAndWorkerCollaborator()\n" + collaborators);
            // 20200824 session.close();
            return collaborators;
        }
    }


    public Collaborator getCollaboratorbyUserName(String userName) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator " +
                    "c left outer join fetch c.user left outer join fetch c.workingConditions left outer join fetch c.detailedCollaboratorInfo left outer join fetch c.jobPosition " +
                    "where c.user.userName=:userName", Collaborator.class);
            query.setParameter("userName", userName);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }


    // Another getters
    public Collaborator getCollaboratorbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            return session.get(Collaborator.class, id);
        }
    }



}
