package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
            org.hibernate.query.Query<User> query = session.createQuery("from User u, Collaborator c join fetch c.jobPosition" +
                    " join fetch c.detailedCollaboratorInfo join fetch c.jobPosition" +
                    " join fetch c.workingConditions");
           List<User> users = query.getResultList();
            return users;
        }
    }


    public ObservableList<String> getUsersNames() {
        ObservableList<String> userNames = FXCollections.observableArrayList();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select userName from User order by userName");
            List resultList = query.getResultList();
            userNames.addAll(resultList);
        }
        return userNames;


/*


        ObservableList<String> userNames = FXCollections.observableArrayList();
        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT user FROM users";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            userNames.add(resultSet.getString(1));
        }
        userNames.sort((s1, s2) -> s1.compareTo(s2));
        return userNames;
*/
    }

    public ObservableList<String> getObservableListOfActiveAndWorkersUserNames() {
        ObservableList<String> activeUserNames = FXCollections.observableArrayList();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select u.userName from User u, Collaborator c, JobPosition j " +
                    "where u=c.user and c.isActive=true and c.jobPosition=j and j.name<>:asesor order by userName");
            query.setParameter("asesor", "Asesor");
            List<String> resultList = query.getResultList();
            activeUserNames.addAll(resultList);
        }
        System.out.println("ACTIVE USERNAMES: " + activeUserNames);
        return activeUserNames;
    }

    public List<String> getActiveAndWorkersUserNames() {
        List<String> activeUserNames = FXCollections.observableArrayList();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select u.userName from User u, Collaborator c, JobPosition j " +
                    "where u=c.user and c.isActive=true and c.jobPosition=j and j.name<>:asesor order by userName");
            query.setParameter("asesor", "Asesor");
            List<String> resultList = query.getResultList();
            activeUserNames.addAll(resultList);
        }
        return activeUserNames;
    }


    public User getUserbyUserName(String username) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", username);
            User tempUser = (User) query.getSingleResult();
            return tempUser;
        } catch (NoResultException exception) {
            return null;
        }

    }

    // Another getters
    public User getUserbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            User tempUser = session.get(User.class, id);
            return tempUser;
        }
    }

    public int getMaxID() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select MAX(id) from User");
            int maxId = (Integer) query.getSingleResult();
            // 20200824 session.close();
            return maxId;
        }


    }

    public void updateUser(User tempUser) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            session.update(tempUser);
            session.getTransaction().commit();
            // 20200824 session.close();
        }
    }
}
