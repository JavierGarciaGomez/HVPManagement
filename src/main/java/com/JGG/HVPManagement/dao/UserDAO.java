package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UserDAO {
    // todo delete static
    // todo change all utilities instances
    private final static UserDAO instance = new UserDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static UserDAO getInstance() {
        return instance;
    }

    public void createUser(User user) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            System.out.println("Inserting new user" + this);
            // 20200824 session.close();
        }
    }

    public List<User> getUsers() {
        try (Session session = hibernateConnection.getSession()) {
            /*session.beginTransaction();
            org.hibernate.query.Query<User> query = session.createQuery("from User order by userName", User.class);
            List<User> users = query.getResultList();
            return users;*/
            session.beginTransaction();
            org.hibernate.query.Query query = session.createQuery("from User u, Collaborator c join fetch c.jobPosition" +
                    " join fetch c.detailedCollaboratorInfo join fetch c.jobPosition" +
                    " join fetch c.workingConditions");
            return query.getResultList();
        }
    }

    public User getUserbyUserName(String username) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", username);
            return (User) query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }

    }

    // Another getters
    public User getUserbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            return session.get(User.class, id);
        }
    }

    public int getMaxID() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select MAX(id) from User");
            // 20200824 session.close();
            return (int) (Integer) query.getSingleResult();
        }


    }

    public void updatePassword(User tempUser) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("update User set pass=:pass where id=:id");
            query.setParameter("pass", tempUser.getPass());
            query.setParameter("id", tempUser.getId());
            query.executeUpdate();
            session.getTransaction().commit();
            // 20200824 session.close();
        }
    }
}
