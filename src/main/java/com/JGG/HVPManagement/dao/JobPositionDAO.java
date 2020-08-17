package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

public class JobPositionDAO {
    // todo delete static
    // todo change all utilities instances
    private final static JobPositionDAO instance = new JobPositionDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static JobPositionDAO getInstance(){
        return instance;
    }

    public void createUser(User user) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        System.out.println("Inserting new user" + this);
        session.close();
    }


    public List<JobPosition> getJobPositions(){
        hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query <JobPosition> query = session.createQuery("from JobPosition ", JobPosition.class);
        List<JobPosition> jobPositions = query.getResultList();
        session.close();
        return jobPositions;
    }


    public ObservableList<String> getJobPositionsNames() throws SQLException {
        List<JobPosition> jobPositions = this.getJobPositions();
        ObservableList<String> jobPositionsNames = FXCollections.observableArrayList();
        for(JobPosition jobPosition:jobPositions){
            jobPositionsNames.add(jobPosition.getName());
        }
        jobPositionsNames.sort(String::compareTo);
        return jobPositionsNames;
    }

    public User getUserbyUserName(String username) {
        hibernateConnection = HibernateConnection.getInstance();
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            Query query = session.createQuery("from User where user=:userName");
            query.setParameter("userName", username);
            User tempUser = (User) query.getSingleResult();
            System.out.println("get User 2" + tempUser);
            return tempUser;
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

    public int getMaxID() {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(id) from User");
        int maxId= (Integer) query.getSingleResult();
        session.close();
        return maxId;
    }

}
