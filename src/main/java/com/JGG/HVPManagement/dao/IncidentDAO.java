package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.Incident;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class IncidentDAO {
    // todo delete static
    // todo change all utilities instances
    private final static IncidentDAO instance = new IncidentDAO();
    private final HibernateConnection hibernateConnection = HibernateConnection.getInstance();

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

    public List<Incident> getIncidentsByDate(LocalDate startDate, LocalDate endDate) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Incident> query = session.createQuery("from Incident i join fetch i.collaborator c " +
                    "left outer join fetch c.jobPosition left outer join fetch c.workingConditions left outer join fetch c.user left outer join fetch c.detailedCollaboratorInfo " +
                    "left outer join fetch i.solvedBy s " +
                    "left outer join fetch s.jobPosition left outer join fetch s.workingConditions left outer join fetch s.user left outer join fetch s.detailedCollaboratorInfo " +
                    "left outer join fetch i.branch " +
                    "where i.dateOfOccurrence>=:startDate and i.dateOfOccurrence<=:endDate", Incident.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        }
    }

    public List<Incident> getIncidentsByCollaboratorAndDate(Collaborator selectedCollaborator, LocalDate startDate, LocalDate endDate) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Incident> query = session.createQuery("from Incident " +
                    "where dateOfOccurrence>=:startDate and dateOfOccurrence<:endDate and collaborator=:collaborator", Incident.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("collaborator", selectedCollaborator);
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

    public void solveIncident(Incident selectedIncident) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            selectedIncident.setSolved(true);
            session.saveOrUpdate(selectedIncident);
            session.getTransaction().commit();
        }
    }
}
