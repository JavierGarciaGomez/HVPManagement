package com.JGG.HVPManagement.entity;

import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="attendanceRegister")
public class TimeRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="userName")
    private String userName;
    @Column(name="branch")
    private String branch;
    @Column(name="action")
    private String action;
    @Column(name="time")
    private Timestamp timestamp;
    @Column(name="status")
    private String status;
    @Transient
    private LocalDateTime localDateTime;

    public TimeRegister() {
    }

    public TimeRegister(String userName, String branch, String action) {
        this.userName = userName;
        this.branch = branch;
        this.action = action;
    }

    public TimeRegister(int id, String userName, String branch, String action, LocalDateTime localDateTime) {
        this.id = id;
        this.userName = userName;
        this.branch = branch;
        this.action = action;
        this.localDateTime = localDateTime;
    }

    public TimeRegister(int id) {
        this.id=id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
    public String getBranch() {
        return branch;
    }
    public String getAction() {
        return action;
    }
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Other getters
    public String getDateAsString() {
        if(localDateTime==null){
            localDateTime=this.timestamp.toLocalDateTime();
        }
        return new Utilities().getDateAsString(this.localDateTime);
    }


    /*
    CRUD
     */

    // CREATE
    public void createTimeRegister() throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        this.setId(0); // if the id is 0 then it creates one.
        this.setTimestamp(this.convertLdtToTS(this.getLocalDateTime()));
        session.save(this);

        session.getTransaction().commit();
        System.out.println("Inserting new time" + this);
        session.close();
/*

        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "INSERT INTO attendanceRegister (userName, branch, action, time) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(this.localDateTime));
        System.out.println(preparedStatement);
        preparedStatement.execute();
        connectionDB.closeConnection();*/
    }

    // READERS
    public TimeRegister getLastTimeRegister(String userName) throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("from TimeRegister as tr where tr.userName=:userName and timestamp = " +
                "(select MAX (timestamp) from TimeRegister where userName=:userName)");
        query.setParameter("userName", userName);
        TimeRegister tempTimpeRegister = (TimeRegister) query.getSingleResult();
        session.close();
        return tempTimpeRegister;
/*



        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE time = " +
                "(SELECT MAX(time) FROM attendanceRegister WHERE username=?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, userName);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int id = resultSet.getInt(1);
        userName = resultSet.getString(2);
        String branch = resultSet.getString(3);
        String action = resultSet.getString(4);
        LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
        return new TimeRegister(id, userName, branch, action, localDateTime);
*/
    }

    public int getMaxID() throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(id) from TimeRegister ");
        int maxId= (Integer) query.getSingleResult();
        session.close();
        return maxId;
/*


        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT MAX(ID) FROM attendanceRegister";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        resultSet.next();
        int maxId=resultSet.getInt(1);
        connectionDB.closeConnection();
        return maxId;
*/
    }

    public List<TimeRegister> getTimeRegisters(){
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query <TimeRegister> query = session.createQuery("from TimeRegister order by timestamp asc", TimeRegister.class);
        List<TimeRegister> timeRegisters = query.getResultList();
        session.close();
        return timeRegisters;
    }


    public ObservableList<TimeRegister> getTimeRegistersObservableList() throws SQLException {
        List<TimeRegister> timeRegisters = this.getTimeRegisters();
        ObservableList<TimeRegister> timeRegisterObservableArrayList = FXCollections.observableArrayList(timeRegisters);
        return timeRegisterObservableArrayList;

/*
        ObservableList<TimeRegister> timeRegisters = FXCollections.observableArrayList();

        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            userName = resultSet.getString(2);
            String branch = resultSet.getString(3);
            String action = resultSet.getString(4);
            LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
*/
    }

    public ObservableList<TimeRegister> getTimeRegistersforUser() throws SQLException {
        ObservableList<TimeRegister> timeRegisters = FXCollections.observableArrayList();
        List<TimeRegister> allTimeRegisters = this.getTimeRegisters();
        for(TimeRegister t:allTimeRegisters){
            if(t.getUserName().equals(this.getUserName())){
                timeRegisters.add(t);
            }
        }
        return timeRegisters;

/*
        // SQL
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE username=?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, userName);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Loop the resultset
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            userName = resultSet.getString(2);
            String branch = resultSet.getString(3);
            String action = resultSet.getString(4);
            LocalDateTime localDateTime = resultSet.getTimestamp(5).toLocalDateTime();
            TimeRegister timeRegister = new TimeRegister(id, userName, branch, action, localDateTime);
            timeRegisters.add(timeRegister);
        }
        return timeRegisters;
*/
    }

    // UPDATE
    public void updateTimeRegister() throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        this.setTimestamp(this.convertLdtToTS(this.getLocalDateTime()));
        session.saveOrUpdate(this);

        session.getTransaction().commit();
        System.out.println("Inserting new time" + this);
        session.close();

/*

        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "UPDATE attendanceRegister SET username = ?, branch=?, action=?, time=? WHERE id=?";

        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.branch);
        preparedStatement.setString(3, this.action);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(this.localDateTime));
        preparedStatement.setInt(5, this.id);
        System.out.println(preparedStatement);
        preparedStatement.execute();
        connectionDB.closeConnection();
*/
    }

    // DELETE
    public void deleteTimeRegister() throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        session.delete(this);
        session.getTransaction().commit();
        session.close();

/*

        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "DELETE FROM attendanceRegister WHERE id=?";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, this.id);
        System.out.println(preparedStatement);
        preparedStatement.execute();
        connectionDB.closeConnection();
*/
    }


    // Other methods

    public boolean isDateAndActionRegistered() throws SQLException {
        List<TimeRegister> allTimeRegisters = this.getTimeRegisters();
        for(TimeRegister t:allTimeRegisters){
            if(t.localDateTime==null) t.localDateTime=t.timestamp.toLocalDateTime();
            System.out.println(this.localDateTime+" "+t.getLocalDateTime());
            if((t.getUserName().equals(this.getUserName()))&&
                    (t.getAction().equals(this.getAction()))&&
                    (t.getLocalDateTime().toLocalDate().equals(this.getLocalDateTime().toLocalDate()))){
                return true;
            }
        }
        return false;

        /*
        //First Try

        // Converting LocalDateTime as MySql date
        LocalDate localDate = this.localDateTime.toLocalDate();
        Date sqlDate = Date.valueOf(localDate);
        String tableName = TimeRegister.class.getAnnotation(Table.class).name();

        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();

        Query query = session.createSQLQuery("select * from attendanceRegister where userName=:userName and action =:action and cast(time as date) = DATE(:sqlDate)");
        //query.setParameter("tableName", tableName);
        query.setParameter("userName", userName);
        query.setParameter("action", action);
        query.setParameter("sqlDate", sqlDate);
        TimeRegister tempTimpeRegister = (TimeRegister) query.getSingleResult();
        System.out.println(tempTimpeRegister);
        session.close();
        return false;*/


/*
        // Converting LocalDateTime as MySql date
        LocalDate localDate = this.localDateTime.toLocalDate();
        Date sqlDate = Date.valueOf(localDate);

        // Connecting to DATABASE
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM attendanceRegister WHERE userName = ? " +
                "AND action = ? AND CAST(time AS DATE) = DATE(?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, this.userName);
        preparedStatement.setString(2, this.action);
        preparedStatement.setDate(3, sqlDate);
        System.out.println(preparedStatement);

        // Getting the resultset
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isRegistered = resultSet.next();
        resultSet.close();
        connectionDB.closeConnection();

        return isRegistered;
*/
    }

    private Timestamp convertLdtToTS(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }


    // To String
    @Override
    public String toString() {

        return this.userName + " " + this.action + " en " + this.branch + ", el " + this.timestamp.toString();
    }




}
