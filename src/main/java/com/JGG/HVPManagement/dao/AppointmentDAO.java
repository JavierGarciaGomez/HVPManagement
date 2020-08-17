package com.JGG.WeeklyScheduler.dao;


import com.JGG.WeeklyScheduler.entity.Appointment;
import com.JGG.WeeklyScheduler.model.HibernateConnection;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
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
    public static List<Appointment> getAppointmentsBetweenDates(LocalDate firstDate, LocalDate lastDate) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment" +
                " where date>=:firstDate and date<=:lastDate", Appointment.class);
        query.setParameter("firstDate", firstDate);
        query.setParameter("lastDate", lastDate);
        List<Appointment> appointments = query.getResultList();
        System.out.println("get AppoitnmentsBetweenDates()\n" + appointments);
        session.close();
        return appointments;
    }

    public Appointment getAppointmentbyId(int id) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        Appointment appointment = session.get(Appointment.class, id);
        session.close();
        return appointment;
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
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        session.saveOrUpdate(appointment);
        session.getTransaction().commit();
        System.out.println("Inserting new appointment" + this);
        session.close();
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

    public List<Appointment> getFilteredAppointments(LocalDate monday, LocalDate sunday, List<String> branchFilters, List<String> vetFilters) {
        List<Appointment> appointments = getAppointmentsBetweenDates(monday, sunday);
        List<Appointment> filteredAppointments = new ArrayList<>();
        for(Appointment appointment:appointments){
            for(String branch:branchFilters){
                if(appointment.getBranch().equals(branch)){
                    for(String vetName:vetFilters){
                        if (appointment.getVeterinarian()!=null){
                            if(appointment.getVeterinarian().equals(vetName)){
                                filteredAppointments.add(appointment);
                            }
                        }
                    }
                }
            }
        }
/*
        for (String branch : branchFilters) {
            for(String vet:vetFilters){



                Session session = hibernateConnection.getSession();
                session.beginTransaction();
                org.hibernate.query.Query<Appointment> query = session.createQuery("from Appointment" +
                        " where date>=:firstDate and date<=:lastDate and branch=:branch and veterinarian=:vet", Appointment.class);
                query.setParameter("firstDate", monday);
                query.setParameter("lastDate", sunday);
                query.setParameter("branch", branch);
                query.setParameter("vet", vet);
                appointments.addAll(query.getResultList());
                System.out.println("get filteredAppointments()\n" + appointments);
                session.close();
            }
        }
*/
        return filteredAppointments;
    }
}
