package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.OpeningHours;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
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
            Query<OpeningHours> query = session.createQuery("from OpeningHours o left outer join fetch o.branch order by o.startDate", OpeningHours.class);
            return query.getResultList();
        }
    }

    public void updateLastOpeningHours(Branch branch, LocalDate endDate) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<OpeningHours> query = session.createQuery("from OpeningHours where branch=:branch" +
                    " and startDate=(select max (startDate) from OpeningHours where branch=:branch and startDate<:endDate)", OpeningHours.class);
            query.setParameter("branch", branch);
            query.setParameter("endDate", endDate);
            OpeningHours openingHours = query.getSingleResult();
            openingHours.setEndDate(endDate.minusDays(1));
            session.update(openingHours);
            session.getTransaction().commit();
        } catch (NoResultException ignore) {
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
