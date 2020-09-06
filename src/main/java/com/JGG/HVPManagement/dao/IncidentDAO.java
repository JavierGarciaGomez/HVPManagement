package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Incident;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.util.List;

public class IncidentDAO {
    // todo delete static
    // todo change all utilities instances
    private final static IncidentDAO instance = new IncidentDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static IncidentDAO getInstance() {
        return instance;
    }


    public List<Incident> getIncidents() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Incident> query = session.createQuery("from Incident ", Incident.class);
            return query.getResultList();
        }
    }

    public void createIncident(Incident incident) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            session.saveOrUpdate(incident);
            session.getTransaction().commit();
        }
    }


}
