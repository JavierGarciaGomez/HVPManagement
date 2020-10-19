package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Log;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;

public class LogDAO {
    // todo delete static
    // todo change all utilities instances
    private final static LogDAO instance = new LogDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static LogDAO getInstance() {
        return instance;
    }


    public void createLog(User loggedUser){
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Log log = new Log();
            log.setUser(loggedUser);
            log.setLogin(LocalDateTime.now());
            session.save(log);
            session.getTransaction().commit();
        }
    }

    public void exitSession(User loggedUser) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            org.hibernate.query.Query<Log> query = session.createQuery("from Log where " +
                    "id=(select max (id) from Log where user=:loggedUser)", Log.class);
            query.setParameter("loggedUser", loggedUser);
            Log log = query.getSingleResult();
            log.setLogout(LocalDateTime.now());
            session.update(log);
            session.getTransaction().commit();
        } catch (NoResultException ignore) {

        }
    }
}
