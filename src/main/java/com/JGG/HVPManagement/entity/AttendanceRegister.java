package com.JGG.HVPManagement.entity;

import com.JGG.HVPManagement.model.HibernateConnection;
import com.JGG.HVPManagement.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

// todo review and delete unusued methods
@Entity
public class AttendanceRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String action;
    private LocalDateTime localDateTime;
    private String status;
    private Integer minutesLate;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator collaborator;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;

    // Constructors
    public AttendanceRegister() {
    }

    public AttendanceRegister(String action, LocalDateTime localDateTime, String status, Integer minutesLate, Collaborator collaborator, Branch branch) {
        this.action = action;
        this.localDateTime = localDateTime;
        this.status = status;
        this.minutesLate = minutesLate;
        this.collaborator = collaborator;
        this.branch = branch;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
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

    public Integer getMinutesLate() {
        return minutesLate;
    }

    public void setMinutesLate(Integer minutesLate) {
        this.minutesLate = minutesLate;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // Other methods
    public String getDateAsString() {
        return Model.getInstance().DTF.format(localDateTime);
    }
    public String getUserName() {
        return this.getCollaborator().getUser().getUserName();
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AttendanceRegister)) return false;
        return id !=null && id.equals(((AttendanceRegister)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return action +
               " " + Model.getInstance().DTF.format(localDateTime) +
                " " + branch;
    }
}
