package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.OpeningHours;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class OpeningHoursDAO {
    private final static OpeningHoursDAO instance = new OpeningHoursDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static OpeningHoursDAO getInstance() {
        return instance;
    }

    public void createOpeningHours(OpeningHours openingHours){
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(openingHours);
            session.getTransaction().commit();
        }
    }

    public List<OpeningHours> getOpeningHoursList() {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            Query<OpeningHours> query = session.createQuery("from OpeningHours o left outer join fetch o.branch order by startDate", OpeningHours.class);
            return query.getResultList();
        }
    }

    public void deleteOpeningHours(OpeningHours openingHours) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(openingHours);
            session.getTransaction().commit();
        }
    }

}
