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
    // todo delete
    @Column
    private String workingDayType;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private WorkingDayType workingDayTypeNew;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator registeredBy;
    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator collaborator;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Branch newBranch;



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

    public WorkingDayType getWorkingDayTypeNew() {
        return workingDayTypeNew;
    }

    public void setWorkingDayTypeNew(WorkingDayType workingDayTypeNew) {
        this.workingDayTypeNew = workingDayTypeNew;
    }

    public Branch getNewBranch() {
        return newBranch;
    }

    public void setNewBranch(Branch newBranch) {
        this.newBranch = newBranch;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkSchedule that = (WorkSchedule) o;

        if (id != that.id) return false;
        if (localDate != null ? !localDate.equals(that.localDate) : that.localDate != null) return false;
        return collaborator != null ? collaborator.equals(that.collaborator) : that.collaborator == null;
    }
}