package com.JGG.HVPManagement.dao;

import com.JGG.HVPManagement.entity.AttendanceRegister;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceRegisterDAO {

    private final static AttendanceRegisterDAO instance = new AttendanceRegisterDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static AttendanceRegisterDAO getInstance() {
        return instance;
    }


    public void createAttendanceRegister(AttendanceRegister attendanceRegister) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            session.saveOrUpdate(attendanceRegister);
            session.getTransaction().commit();
        }
    }

    public List<AttendanceRegister> getAttendanceRegisters() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<AttendanceRegister> query = session.createQuery("from AttendanceRegister a " +
                    "left outer join fetch a.branch left outer join fetch a.collaborator c left outer join fetch c.user " +
                    "left outer join fetch c.workingConditions left join fetch c.detailedCollaboratorInfo" +
                    " left outer join fetch c.jobPosition ", AttendanceRegister.class);
            return query.getResultList();
        }
    }

    public AttendanceRegister getLastAttendanceRegisterByCollaborator(Collaborator collaborator) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<AttendanceRegister> query = session.createQuery("from AttendanceRegister a " +
                    "left outer join fetch a.branch left outer join fetch a.collaborator c left outer join fetch c.user " +
                    "left outer join fetch c.workingConditions left outer join fetch c.detailedCollaboratorInfo left outer join fetch c.jobPosition " +
                    "where c=:collaborator and a.localDateTime=(select max(a.localDateTime) from a where c=collaborator)", AttendanceRegister.class);
            query.setParameter("collaborator", collaborator);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public void deleteAttendanceRegister(AttendanceRegister selectedAttendanceRegister) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(selectedAttendanceRegister);
            session.getTransaction().commit();
        }
    }

}
