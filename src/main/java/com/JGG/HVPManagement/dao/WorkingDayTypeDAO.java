package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.WorkingDayType;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class WorkingDayTypeDAO {
    private final static WorkingDayTypeDAO instance = new WorkingDayTypeDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static WorkingDayTypeDAO getInstance() {
        return instance;
    }

    public void createWorkingDayType(WorkingDayType workingDayType){
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(workingDayType);
            session.getTransaction().commit();
        }
    }


    public List<WorkingDayType> getWorkingDayTypes() {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            Query<WorkingDayType> query = session.createQuery("from WorkingDayType order by name", WorkingDayType.class);
            List<WorkingDayType> workingDayTypes = query.getResultList();
            return workingDayTypes;
        }
    }

    public void deleteWorkingDayType(WorkingDayType workingDayType) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(workingDayType);
            session.getTransaction().commit();
        }
    }


}
