package com.JGG.WeeklyScheduler.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="lastName")
    private String lastName;

    @Column(name="user")
    private String user;

    @Column(name="pass")
    private String pass;

    @Column(name="isActive")
    private boolean isActive;

    public User() {
    }


    public User(int id) {
        this.id = id;
    }

    public User(String user) {
        this.user = user;
    }

    public User(String userName, String pass) {
        this.user = userName;
        this.pass = pass;
    }

    public User(int id, String name, String lastName, String user, String pass, boolean isActive) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.user = user;
        this.pass = pass;
        this.isActive = isActive;
    }

    // GETTERS and SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    // Another getters
    public User getUserbyId(int id) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        User tempUser = session.get(User.class, id);
        session.close();
        return tempUser;
    }


    public User getUserbyUserName(String username) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where user=:user");
        query.setParameter("user", user);
        User tempUser = (User) query.getSingleResult();
        System.out.println("get User 2" + tempUser);
        session.close();
        return tempUser;
    }


    public int getMaxID() throws SQLException {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(id) from User");
        int maxId= (Integer) query.getSingleResult();
        session.close();
        return maxId;

/*
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT MAX(ID) FROM USERS";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        resultSet.next();
        int maxId=resultSet.getInt(1);
        connectionDB.closeConnection();
        return maxId;
*/
    }

    public List<User> getUsers(){
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        org.hibernate.query.Query <User> query = session.createQuery("from User order by user", User.class);
        List<User> users = query.getResultList();
        System.out.println("getUsers()\n"+users);
        session.close();
        return users;
    }


    public ObservableList<String> getUsersNames() throws SQLException {
        List<User> users = this.getUsers();
        ObservableList<String> userNames = FXCollections.observableArrayList();
        for(User u:users){
            userNames.add(u.getUser());
        }
        userNames.sort((s1, s2)-> s1.compareTo(s2));
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


    // CRUD
    // TODO delete
/*    public void addUser() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connectionDB.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, this.id);
        preparedStatement.setString(2, this.name);
        preparedStatement.setString(3, this.lastName);
        preparedStatement.setString(4, this.user);
        preparedStatement.setString(5, this.pass);
        preparedStatement.setBoolean(6, this.isActive);

        System.out.println(preparedStatement);
        boolean isSuccessful = preparedStatement.execute();


        connectionDB.closeConnection();

    }*/

    // TODO 20200810
    public void createUser() {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        this.setId(0); // if the id is 0 then it creates one.
        session.save(this);
        session.getTransaction().commit();
        System.out.println("Inserting new user" + this);
        session.close();
    }



    // Another methods
    // TODO delete
/*    public boolean checkLogin() throws SQLException {
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE user = '" + user + "' AND pass ='" + pass + "'";
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isUser = resultSet.next();
        connectionDB.closeConnection();
        return isUser;
    }*/



    // TODO test hibernate
    public boolean checkLogin2() {
        User tempUser = getUserbyUserName(this.getName());
        if(this.getPass().equals(tempUser.getPass())) return true;
        else return false;
    }


    public boolean checkAvailableId() throws SQLException {
        User tempUser = this.getUserbyId(this.getId());
        if(tempUser==null){
            return true;
        } else{
            return false;
        }

/*        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE id = " + this.id;
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isAvailable = !resultSet.next();
        connectionDB.closeConnection();
        return isAvailable;*/
    }

    public boolean checkAvailableUser() throws SQLException {
        User tempUser = this.getUserbyUserName(this.getUser());
        if(tempUser==null){
            return true;
        } else{
            return false;
        }
/*
        ConnectionDB connectionDB = new ConnectionDB();
        String sql = "SELECT * FROM users WHERE user = '" + this.user+"'";
        System.out.println(sql);
        ResultSet resultSet = connectionDB.executeQuery(sql);
        boolean isAvailable = !resultSet.next();
        connectionDB.closeConnection();
        return isAvailable;
*/
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
