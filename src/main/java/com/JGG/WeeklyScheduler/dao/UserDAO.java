package com.JGG.WeeklyScheduler.dao;


import com.JGG.WeeklyScheduler.entity.HibernateConnection;
import com.JGG.WeeklyScheduler.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

public class UserDAO {
    // todo delete static
    // todo change all utilities instances
    private final static UserDAO instance = new UserDAO();
    private HibernateConnection hibernateConnection = HibernateConnection.getInstance();

    public static UserDAO getInstance(){
        return instance;
    }

    public void createUser(User user) {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        System.out.println("Inserting new user" + this);
        session.close();
    }


    public List<User> getUsers(){
        hibernateConnection = HibernateConnection.getInstance();
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

    public User getUserbyUserName(String username) {
        hibernateConnection = HibernateConnection.getInstance();
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            Query query = session.createQuery("from User where user=:userName");
            query.setParameter("userName", username);
            User tempUser = (User) query.getSingleResult();
            System.out.println("get User 2" + tempUser);
            return tempUser;
        } catch (NoResultException exception){
            return null;
        }

    }

    // Another getters
    public User getUserbyId(int id) {
        hibernateConnection = HibernateConnection.getInstance();
        try(Session session= hibernateConnection.getSession()){
            session.beginTransaction();
            User tempUser = session.get(User.class, id);
            return tempUser;
        }
    }

    public int getMaxID() {
        HibernateConnection hibernateConnection = HibernateConnection.getInstance();
        Session session= hibernateConnection.getSession();
        session.beginTransaction();
        Query query = session.createQuery("select MAX(id) from User");
        int maxId= (Integer) query.getSingleResult();
        session.close();
        return maxId;
    }

}
