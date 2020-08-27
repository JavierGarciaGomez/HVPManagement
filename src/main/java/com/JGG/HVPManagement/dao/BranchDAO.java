package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BranchDAO {
    private final static BranchDAO instance = new BranchDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

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
            List<Branch> branches = query.getResultList();
            return branches;
        }
    }
}
