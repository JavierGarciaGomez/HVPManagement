package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Appointment;
import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final static AppointmentDAO instance = new AppointmentDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static AppointmentDAO getInstance() {
        return instance;
    }

    // todo delete static
    public List<Appointment> getAllApointments() {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment", Appointment.class);
        List<Appointment> appointments = query.getResultList();
        System.out.println("get Appoitnments()\n" + appointments);
        session.close();
        return appointments;
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
            List<Appointment> appointments = query.getResultList();
            return appointments;
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

    public List<Appointment> getAppointmenByDateTime(LocalDate localDate, LocalTime localTime) {
        List<Appointment> allApointments = this.getAllApointments();
        List<Appointment> appointments = new ArrayList<>();
        for (Appointment a : allApointments) {
            if (localDate.equals(a.getDate())) {
                if (localTime.getHour() == a.getTime().getHour()) {
                    appointments.add(a);
                }
            }
        }
        return appointments;
    }

    public void createAppointment(Appointment appointment) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(appointment);
            session.getTransaction().commit();
            System.out.println("Inserting new appointment" + this);
            session.close();
        }
    }

    public void deleteAppointment(Appointment appointment) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        session.delete(appointment);
        session.getTransaction().commit();
        System.out.println("deleting appointment" + this);
        session.close();
    }

    // todo delete

    public static void main(String[] args) {
        LocalDate monday = LocalDate.of(2020, 8, 10);

        List<Appointment> appointments1 = new AppointmentDAO().getAllApointments();
        List<Appointment> appointments2 = new AppointmentDAO().getAppointmentsBetweenDates(LocalDate.of(2020, 8, 10), LocalDate.of(2020, 8, 16));

        String[] availableHours = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        // method to put in a label;
        for (Appointment a : appointments2) {

            int dayIndex = a.getDate().getDayOfWeek().getValue();
            int hourIndex = a.getTime().getHour();
            String hourIndexString = (hourIndex + ":00");
            System.out.println(a + "dayIndex: " + dayIndex + " hourIndex: " + hourIndexString);
            for (int i = 0; i < availableHours.length; i++) {
                if (availableHours[i].equals(hourIndexString)) {
                    hourIndex = i + 1;
                }
            }
            System.out.println(a + "the index is. Day: " + dayIndex + " hour: " + hourIndex);


        }

    }
}
