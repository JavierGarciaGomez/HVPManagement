package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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
        try (Session session = hibernateConnection.getSession();) {
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
            // org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c where c.isActive=true and jobPosition.name<>:asesor", Collaborator.class);
            // org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c fetch all properties where c.isActive=true and jobPosition.name<>:asesor", Collaborator.class);
            org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c join fetch c.user join fetch c.workingConditions join fetch c.detailedCollaboratorInfo join fetch c.jobPosition", Collaborator.class);
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getActiveAndWorkerCollaborator()\n" + collaborators);
            // 20200824 session.close();
            return collaborators;
        }
    }

    public List<Collaborator> getActiveAndWorkerCollaborators() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            // org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c where c.isActive=true and jobPosition.name<>:asesor", Collaborator.class);
            // org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c fetch all properties where c.isActive=true and jobPosition.name<>:asesor", Collaborator.class);
            org.hibernate.query.Query<Collaborator> query = session.createQuery("from Collaborator c join fetch c.user join fetch c.workingConditions join fetch c.detailedCollaboratorInfo where c.isActive=true and c.jobPosition.name<>:asesor order by c.user.userName", Collaborator.class);

            query.setParameter("asesor", "Asesor");
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getActiveAndWorkerCollaborator()\n" + collaborators);
            // 20200824 session.close();
            return collaborators;
        }
    }

    public Collaborator getCollaboratorbyUserName(String userName) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:userName", Collaborator.class);
            query.setParameter("userName", userName);
            User tempUser = (User) query.getSingleResult();
            Collaborator collaborator = tempUser.getCollaborator();
            System.out.println("get Collaborator" + collaborator);
            return collaborator;
        } catch (NoResultException exception) {
            return null;
        }
    }


    // Another getters
    public Collaborator getCollaboratorbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Collaborator collaborator = session.get(Collaborator.class, id);
            return collaborator;
        }
    }

    public Collaborator getCollaboratorbyCollaboratorId(int collaboratorId) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Collaborator where" +
                    " collaboratorId=:collaboratorId");
            query.setParameter("collaboratorId", collaboratorId);
            List results = query.getResultList();
            if (results.isEmpty()) return null;
            else return (Collaborator) results.get(0);
        }
    }

    public int getMaxCollaboratorId() {
        try (Session session = hibernateConnection.getSession()) {

            session.beginTransaction();
            Query query = session.createQuery("select MAX(collaboratorId) from Collaborator");
            int maxId;
            try {
                maxId = (Integer) query.getSingleResult();
            } catch (NullPointerException e) {
                maxId = 0;
            }
            // 20200824 session.close();
            return maxId;
        }
    }


}
