package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import org.hibernate.Session;

import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalTime;
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

            Model.getInstance().testStart = LocalTime.now();
            session.beginTransaction();
            org.hibernate.query.Query<WorkSchedule> query = session.createQuery("from WorkSchedule w " +
                    "left outer join fetch w.collaborator c left outer join fetch c.user left outer join fetch c.workingConditions left join fetch c.detailedCollaboratorInfo" +
                    " left outer join fetch c.jobPosition left outer join fetch w.branch left outer join fetch w.workingDayType", WorkSchedule.class);
            Model.getInstance().testFinish = LocalTime.now();
            System.out.println(query.getResultList().size());
            return query.getResultList();


        }
    }


    // first failed try
/*
    public void createOrReplaceRegisters(List<WorkScheduleService> tempWorkSchedules) {
        try (Session session = hibernateConnection.getSession();) {
            session.beginTransaction();
            for (WorkScheduleService workSchedule : tempWorkSchedules) {
                // check if already registered
                org.hibernate.query.Query<WorkScheduleService> query = session.createQuery("from WorkScheduleService where collaborator=:collaborator and localDate=:localDate", WorkScheduleService.class);
                query.setParameter("collaborator", workSchedule.getCollaborator());
                query.setParameter("localDate", workSchedule.getLocalDate());
                List<WorkScheduleService> resultWorkSchedules = query.getResultList();
                System.out.println("This is the result "+resultWorkSchedules);
                if(resultWorkSchedules.isEmpty()){
                    session.save(workSchedule);
                } else{
                    WorkScheduleService retrievedWorkSchedule = resultWorkSchedules.get(0);
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
            //org.hibernate.query.Query<WorkScheduleService> query = session.createQuery("from WorkScheduleService", WorkScheduleService.class);

            /*org.hibernate.query.Query<WorkScheduleService> query = session.createQuery("from WorkScheduleService w " +
                    "join fetch w.branch join fetch w.workingDayType", WorkScheduleService.class);
            List<WorkScheduleService> resultWorkSchedules = query.getResultList();*/

            List<WorkSchedule> allWorkSchedules = Model.getInstance().workSchedules;
            // if empty register all
            if (allWorkSchedules.isEmpty()) {
                for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                    System.out.println("LIST IS EMPTY");
                    session.save(tempWorkSchedule);
                }
            } else {
                for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                    boolean registerFound = false;

                    for (WorkSchedule retrievedWorkSchedule : allWorkSchedules) {
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

    public void updateWorkSchedules(List<WorkSchedule> workSchedulesToUpdate) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            for (WorkSchedule workScheduleToUpdate : workSchedulesToUpdate) {
                session.update(workScheduleToUpdate);
            }
            session.getTransaction().commit();
        }
    }

    public void saveWorkSchedules(List<WorkSchedule> workschedulesToSave) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            for (WorkSchedule workScheduleToSave : workschedulesToSave) {
                session.save(workScheduleToSave);
            }
            session.getTransaction().commit();
        }
    }

    public List<WorkSchedule> getWorkSchedulesByDate(LocalDate firstDay, LocalDate lastDay) {
        List<WorkSchedule> workSchedules;
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from WorkSchedule ws join fetch ws.collaborator join fetch ws.registeredBy where ws.localDate>=:firstDay and" +
                    " ws.localDate<=:lastDay", WorkSchedule.class);
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
