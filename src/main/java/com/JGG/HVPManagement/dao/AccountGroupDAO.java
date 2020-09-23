package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.GroupAccount;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AccountGroupDAO {
    private final static AccountGroupDAO instance = new AccountGroupDAO();
    private final HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static AccountGroupDAO getInstance() {
        return instance;
    }

    public List<GroupAccount> getAllGroups() {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query<GroupAccount> query = session.createQuery("from GroupAccount", GroupAccount.class);
            return query.getResultList();
        }
    }

    public void createOrUpdate(GroupAccount groupAccount) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(groupAccount);
            session.getTransaction().commit();
        }
    }

    public void delete(GroupAccount selectedGroupAccount) {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.delete(selectedGroupAccount);
            session.getTransaction().commit();
        }
    }
}