package com.JGG.WeeklyScheduler.dao;


import com.JGG.WeeklyScheduler.entity.Appointment;
import com.JGG.WeeklyScheduler.entity.HibernateConnection;
import com.JGG.WeeklyScheduler.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // todo delete static

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

}
