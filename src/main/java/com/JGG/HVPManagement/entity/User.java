package com.JGG.HVPManagement.entity;

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

    @Column(name="role")
    private String role;

    @Column(name="isActive")
    private boolean isActive;

    @OneToOne(mappedBy = "user", cascade=CascadeType.ALL)
    private Collaborator collaborator;

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
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
