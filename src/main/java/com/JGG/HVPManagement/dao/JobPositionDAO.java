package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.util.List;

public class JobPositionDAO {
    // todo delete static
    // todo change all utilities instances
    private final static JobPositionDAO instance = new JobPositionDAO();
    private final HibernateConnection hibernateConnection = HibernateConnection.getInstance();

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


    public void deleteJobPosition(JobPosition jobPosition) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(jobPosition);
            session.getTransaction().commit();
        }
    }
}
