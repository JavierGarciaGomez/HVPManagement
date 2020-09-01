package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class WorkScheduleDAO {
    private final static WorkScheduleDAO instance = new WorkScheduleDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static WorkScheduleDAO getInstance() {
        return instance;
    }


    public List<WorkSchedule> getWorkSchedules() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<WorkSchedule> query = session.createQuery("from WorkSchedule w join fetch " +
                    "w.branch join fetch w.workingDayType ", WorkSchedule.class);
            return query.getResultList();
        }
    }


    // first failed try
/*
    public void createOrReplaceRegisters(List<WorkSchedule> tempWorkSchedules) {
        try (Session session = hibernateConnection.getSession();) {
            session.beginTransaction();
            for (WorkSchedule workSchedule : tempWorkSchedules) {
                // check if already registered
                org.hibernate.query.Query<WorkSchedule> query = session.createQuery("from WorkSchedule where collaborator=:collaborator and localDate=:localDate", WorkSchedule.class);
                query.setParameter("collaborator", workSchedule.getCollaborator());
                query.setParameter("localDate", workSchedule.getLocalDate());
                List<WorkSchedule> resultWorkSchedules = query.getResultList();
                System.out.println("This is the result "+resultWorkSchedules);
                if(resultWorkSchedules.isEmpty()){
                    session.save(workSchedule);
                } else{
                    WorkSchedule retrievedWorkSchedule = resultWorkSchedules.get(0);
                    retrievedWorkSchedule=workSchedule;
                    session.update(workSchedule);
                }
            }
            session.getTransaction().commit();
        }
    }
*/
    // second try
    public void createOrReplaceRegisters(List<WorkSchedule> tempWorkSchedules) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<WorkSchedule> query = session.createQuery("from WorkSchedule", WorkSchedule.class);
            List<WorkSchedule> resultWorkSchedules = query.getResultList();
            // if empty register all
            if (resultWorkSchedules.isEmpty()) {
                for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                    System.out.println("LIST IS EMPTY");
                    session.save(tempWorkSchedule);
                }
            } else {
                for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                    boolean registerFound = false;
                    for (WorkSchedule retrievedWorkSchedule : resultWorkSchedules) {
                        // check if is already registered
                        if ((retrievedWorkSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))
                                && (retrievedWorkSchedule.getCollaborator().getId() == (tempWorkSchedule.getCollaborator().getId()))) {
                            if ((!Objects.equals(retrievedWorkSchedule.getBranch(), tempWorkSchedule.getBranch())
                                    || (!Objects.equals(retrievedWorkSchedule.getStartingTime(), tempWorkSchedule.getStartingTime()))
                                    || (!Objects.equals(retrievedWorkSchedule.getEndingTime(), tempWorkSchedule.getEndingTime()))
                                    || (!Objects.equals(retrievedWorkSchedule.getWorkingDayType(), tempWorkSchedule.getWorkingDayType())))) {
                                retrievedWorkSchedule.setRegisteredBy(tempWorkSchedule.getRegisteredBy());
                                retrievedWorkSchedule.setWorkingDayType(tempWorkSchedule.getWorkingDayType());
                                retrievedWorkSchedule.setStartingTime(tempWorkSchedule.getStartingTime());
                                retrievedWorkSchedule.setEndingTime(tempWorkSchedule.getEndingTime());
                                retrievedWorkSchedule.setBranch(tempWorkSchedule.getBranch());
                                session.update(retrievedWorkSchedule);
                                System.out.println("updated " + retrievedWorkSchedule.getId());
                            }
                            registerFound = true;
                            break;
                        }
                    }
                    if (!registerFound) {
                        session.save(tempWorkSchedule);
                    }

                }
                session.getTransaction().commit();
            }
        }
    }


    public List<WorkSchedule> getWorkSchedulesByDate(LocalDate firstDay, LocalDate lastDay) {
        List<WorkSchedule> workSchedules;
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from WorkSchedule where localDate>=:firstDay and" +
                    " localDate<=:lastDay", WorkSchedule.class);
            query.setParameter("firstDay", firstDay);
            query.setParameter("lastDay", lastDay);
            workSchedules = query.getResultList();
        }
        return workSchedules;
    }


    public void deleteRegistersByDate(LocalDate startingDate, LocalDate endingDate) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("delete WorkSchedule where localDate>=:startingDate and localDate<=:endingDate");
            query.setParameter("startingDate", startingDate);
            query.setParameter("endingDate", endingDate);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

}
