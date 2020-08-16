package com.JGG.WeeklyScheduler.entity;

import java.sql.*;

public class ConnectionDB {

    private final Connection connection;


    public ConnectionDB() throws SQLException {
        String user = "test";
        String password = "secret";
        String dataBase = "exercises";
        String host = "localhost";
        String stringConnection = "jdbc:mysql://" + host + "/" + dataBase+"?&useLegacyDatetimeCode=false&serverTimezone=UTC";
        connection = DriverManager.getConnection(stringConnection, user, password);
        connection.setAutoCommit(true);
    }


    public Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(sql);
    }

    public int update(String sql) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
