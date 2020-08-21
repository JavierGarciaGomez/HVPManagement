package com.JGG.HVPManagement.dao;


import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.User;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkScheduleDAO {
    private final static WorkScheduleDAO instance = new WorkScheduleDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static WorkScheduleDAO getInstance() {
        return instance;
    }

    public void createOrUpdateWorkSchedule(WorkSchedule workSchedule) {
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        session.saveOrUpdate(workSchedule);
        session.getTransaction().commit();
        System.out.println("Inserting new collaborator" + workSchedule);
        session.close();
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
        try (Session session = hibernateConnection.getSession();) {
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
                                && (retrievedWorkSchedule.getCollaborator().getId()==(tempWorkSchedule.getCollaborator().getId()))) {
                            retrievedWorkSchedule.setRegisteredBy(tempWorkSchedule.getRegisteredBy());
                            retrievedWorkSchedule.setWorkingDayType(tempWorkSchedule.getWorkingDayType());
                            retrievedWorkSchedule.setStartingTime(tempWorkSchedule.getStartingTime());
                            retrievedWorkSchedule.setEndingTime(tempWorkSchedule.getEndingTime());
                            retrievedWorkSchedule.setBranch(tempWorkSchedule.getBranch());
                            session.update(retrievedWorkSchedule);
                            System.out.println("updated " + retrievedWorkSchedule.getId());
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
        List<WorkSchedule> workSchedules = new ArrayList<>();
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


    public List<User> getUsers() {
        hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query<User> query = session.createQuery("from User order by userName", User.class);
        List<User> users = query.getResultList();
        System.out.println("getUsers()\n" + users);
        session.close();
        return users;
    }


    public ObservableList<String> getUsersNames() throws SQLException {
        List<User> users = this.getUsers();
        ObservableList<String> userNames = FXCollections.observableArrayList();
        for (User u : users) {
            userNames.add(u.getUserName());
        }
        userNames.sort((s1, s2) -> s1.compareTo(s2));
        return userNames;

/*


        ObservableList<String> userNames = FXCollections.observableArrayList();
        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT user FROM users";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            userNames.add(resultSet.getString(1));
        }
        userNames.sort((s1, s2) -> s1.compareTo(s2));
        return userNames;
*/
    }

    public User getUserbyUserName(String username) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", username);
            User tempUser = (User) query.getSingleResult();
            System.out.println("get User 2" + tempUser);
            return tempUser;
        } catch (NoResultException exception) {
            return null;
        }

    }

    public Collaborator getCollaboratorbyUserName(String userName) {
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();

            Query query = session.createQuery("from User where userName=:userName");
            query.setParameter("userName", userName);
            User tempUser = (User) query.getSingleResult();
            Collaborator collaborator = tempUser.getCollaborator();
            System.out.println("get Collaborator" + collaborator);
            return collaborator;
        } catch (NoResultException exception) {
            return null;
        }
    }


    // Another getters
    public Collaborator getCollaboratorbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try (Session session = hibernateConnection.getSession()) {
            session.beginTransaction();
            Collaborator collaborator = session.get(Collaborator.class, id);
            return collaborator;
        }
    }

    public int getMaxCollaboratorId() {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session = hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(collaboratorId) from Collaborator");
        int maxId;
        try {
            maxId = (Integer) query.getSingleResult();
        } catch (NullPointerException e) {
            maxId = 0;
        }
        session.close();
        return maxId;
    }


}
