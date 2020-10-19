package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final static AppointmentDAO instance = new AppointmentDAO();
    private final HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static AppointmentDAO getInstance() {
        return instance;
    }

    // todo delete static
    public List<Appointment> getAllApointments() {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment", Appointment.class);
            List<Appointment> appointments = query.getResultList();
            System.out.println("get Appoitnments()\n" + appointments);
            return appointments;
        }
    }

    // todo delete static
    public List<Appointment> getAppointmentsBetweenDates(LocalDate firstDate, LocalDate lastDate) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment a " +
                    "left outer join fetch a.branch left outer join fetch a.collaborator c " +
                    "left outer join fetch c.user left outer join fetch c.workingConditions left outer join fetch c.jobPosition left outer join fetch c.detailedCollaboratorInfo" +
                    " where a.date>=:firstDate and a.date<=:lastDate", Appointment.class);
            query.setParameter("firstDate", firstDate);
            query.setParameter("lastDate", lastDate);
            return query.getResultList();
        }
    }

    public List<Appointment> getFilteredAppointments(LocalDate monday, LocalDate sunday, List<String> branchFilters, List<String> vetFilters) {
        List<Appointment> appointments = getAppointmentsBetweenDates(monday, sunday);
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            for (String branch : branchFilters) {
                if (appointment.getBranch().getName().equals(branch)) {
                    for (String vetName : vetFilters) {
                        if (appointment.getCollaborator() != null) {
                            if (appointment.getCollaborator().getUser().getUserName().equals(vetName)) {
                                filteredAppointments.add(appointment);
                            }
                        }
                    }
                }
            }
        }
        return filteredAppointments;
    }

    public Appointment getAppointmentbyId(int id) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment a " +
                    "left outer join fetch a.branch left outer join fetch a.collaborator c " +
                    "left outer join fetch c.user left outer join fetch c.workingConditions left outer join fetch c.jobPosition left outer join fetch c.detailedCollaboratorInfo " +
                    "where a.id=:id", Appointment.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
    }

    public void createAppointment(Appointment appointment) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(appointment);
            session.getTransaction().commit();
        }
    }

    public void deleteAppointment(Appointment appointment) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        session.delete(appointment);
        session.getTransaction().commit();
    }

}
