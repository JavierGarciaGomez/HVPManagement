package com.JGG.HVPManagement.entity;

import com.JGG.HVPManagement.model.Model;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User {
    @Id
    private Integer id;
    private String userName;
    private String pass;
    private Model.role role;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Collaborator collaborator;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Log> logs;



    // GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Model.role getRole() {
        return role;
    }

    public void setRole(Model.role role) {
        this.role = role;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    // LIST MANAGERS
    public void addLog(Log log){
        if(logs==null){
            logs=new ArrayList<>();
        }
        this.logs.add(log);
        log.setUser(this);
    }

    public void removeLog (Log log){
        this.logs.remove(log);
        log.setUser(null);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
