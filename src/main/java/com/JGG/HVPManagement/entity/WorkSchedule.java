package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String branch;
    @Column
    private LocalDate localDate;
    @Column
    private LocalTime startingTime;
    @Column
    private LocalTime endingTime;
    @Column
    private String workingDayType;
    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator registeredBy;
    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator collaborator;



    public WorkSchedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

    public String getWorkingDayType() {
        return workingDayType;
    }

    public void setWorkingDayType(String workingDayType) {
        this.workingDayType = workingDayType;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public Collaborator getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Collaborator registeredBy) {
        this.registeredBy = registeredBy;
    }

    @Override
    public String toString() {
        return "WorkSchedule{" +
                "id=" + id +
                ", branch='" + branch + '\'' +
                ", localDate=" + localDate +
                ", startingTime=" + startingTime +
                ", endingTime=" + endingTime +
                ", workingDayType='" + workingDayType + '\'' +
                ", registeredBy=" + registeredBy +
                ", collaborator=" + collaborator +
                '}';
    }


}