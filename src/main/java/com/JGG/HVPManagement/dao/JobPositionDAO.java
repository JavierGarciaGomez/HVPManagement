package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.WorkingDayType;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class JobPositionDAO {
    // todo delete static
    // todo change all utilities instances
    private final static JobPositionDAO instance = new JobPositionDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static JobPositionDAO getInstance() {
        return instance;
    }

    public void createJobPosition(JobPosition jobPosition){
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(jobPosition);
            session.getTransaction().commit();
        }
    }


    public List<JobPosition> getJobPositions() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<JobPosition> query = session.createQuery("from JobPosition ", JobPosition.class);
            return query.getResultList();
        }
    }


    public ObservableList<String> getJobPositionsNames() {
        List<JobPosition> jobPositions = this.getJobPositions();
        ObservableList<String> jobPositionsNames = FXCollections.observableArrayList();
        for (JobPosition jobPosition : jobPositions) {
            jobPositionsNames.add(jobPosition.getName());
        }
        jobPositionsNames.sort(String::compareTo);
        return jobPositionsNames;
    }

    public JobPosition getJobPositionbyName(String name) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from JobPosition where name=:name");
            query.setParameter("name", name);
            return (JobPosition) query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }

    }


    public void deleteJobPosition(JobPosition jobPosition) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(jobPosition);
            session.getTransaction().commit();
        }
    }
}
