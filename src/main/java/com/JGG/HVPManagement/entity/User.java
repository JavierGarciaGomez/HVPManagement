package com.JGG.HVPManagement.entity;

import javax.persistence.*;


@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    // todo delete
    @Column
    private String name;

    // todo delete
    @Column
    private String lastName;

    @Column
    private String userName;

    @Column
    private String pass;

    @Column
    private String role;

    // todo delete
    @Column
    private boolean isActive;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    private Collaborator collaborator;

    public User() {
    }


    public User(int id) {
        this.id = id;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, String pass) {
        this.userName = userName;
        this.pass = pass;
    }

    public User(int id, String name, String lastName, String userName, String pass, boolean isActive) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", pass='" + pass + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
