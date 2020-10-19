package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.model.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BranchDAO {
    private final static BranchDAO instance = new BranchDAO();
    private final HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static BranchDAO getInstance() {
        return instance;
    }

    public void createBranch(Branch branch){
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            session.saveOrUpdate(branch);
            session.getTransaction().commit();
        }
    }


    public List<Branch> getBranches() {
        try(Session session = hibernateConnection.getSession()){
            session.beginTransaction();
            Query<Branch> query = session.createQuery("from Branch order by name", Branch.class);
            return query.getResultList();
        }
    }
}
