package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CollaboratorDAO {
    // todo delete static
    // todo change all utilities instances
    private final static CollaboratorDAO instance = new CollaboratorDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static CollaboratorDAO getInstance(){
        return instance;
    }

    public void createOrUpdateCollaborator(Collaborator collaborator) {
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        session.saveOrUpdate(collaborator);
        session.getTransaction().commit();
        System.out.println("Inserting new collaborator" + collaborator);
        session.close();
    }


    public List<User> getUsers(){
        hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query <User> query = session.createQuery("from User order by userName", User.class);
        List<User> users = query.getResultList();
        System.out.println("getUsers()\n"+users);
        session.close();
        return users;
    }

    public List<Collaborator> getAllCollaborators() {
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            org.hibernate.query.Query <Collaborator> query = session.createQuery("from Collaborator", Collaborator.class);
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getCollaborator()\n"+collaborators);
            session.close();
            return collaborators;
        }
    }


    public List<Collaborator> getActiveAndWorkerCollaborators() {
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            org.hibernate.query.Query <Collaborator> query = session.createQuery("from Collaborator c where " +
                    "c.isActive=true and jobPosition.name<>:asesor", Collaborator.class);
            query.setParameter("asesor", "Asesor");
            List<Collaborator> collaborators = query.getResultList();
            System.out.println("getActiveAndWorkerCollaborator()\n"+collaborators);
            session.close();
            return collaborators;
        }
    }


    public ObservableList<String> getUsersNames() throws SQLException {
        List<User> users = this.getUsers();
        ObservableList<String> userNames = FXCollections.observableArrayList();
        for(User u:users){
            userNames.add(u.getUserName());
        }
        userNames.sort((s1, s2)-> s1.compareTo(s2));
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

    public User getUserbyUserName(String username) {
        hibernateConnection = HibernateConnection.getInstance();
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", username);
            User tempUser = (User) query.getSingleResult();
            System.out.println("get User 2" + tempUser);
            return tempUser;
        } catch (NoResultException exception){
            return null;
        }

    }

    public Collaborator getCollaboratorbyUserName(String userName) {
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();

            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", userName);
            User tempUser = (User) query.getSingleResult();
            Collaborator collaborator = tempUser.getCollaborator();
            System.out.println("get Collaborator" + collaborator);
            return collaborator;
        } catch (NoResultException exception){
            return null;
        }
    }


    // Another getters
    public Collaborator getCollaboratorbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            Collaborator collaborator = session.get(Collaborator.class, id);
            return collaborator;
        }
    }

    public Collaborator getCollaboratorbyCollaboratorId(int collaboratorId) {
        try(Session session= hibernateConnection.getSession()){
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
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(collaboratorId) from Collaborator");
        int maxId;
        try{
            maxId= (Integer) query.getSingleResult();
        } catch(NullPointerException e){
            maxId=0;
        }
        session.close();
        return maxId;
    }


}
