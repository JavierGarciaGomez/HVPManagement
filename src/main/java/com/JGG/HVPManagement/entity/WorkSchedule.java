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
    private LocalDate localDate;
    @Column
    private LocalTime startingTime;
    @Column
    private LocalTime endingTime;
    // todo delete

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private WorkingDayType workingDayType;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator registeredBy;
    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Collaborator collaborator;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private Branch Branch;



    public WorkSchedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public WorkingDayType getWorkingDayType() {
        return workingDayType;
    }

    public void setWorkingDayType(WorkingDayType workingDayTypeNew) {
        this.workingDayType = workingDayTypeNew;
    }

    public Branch getBranch() {
        return Branch;
    }

    public void setBranch(Branch newBranch) {
        this.Branch = newBranch;
    }

    @Override
    public String toString() {
        return "WorkSchedule{" +
                "id=" + id +
                ", localDate=" + localDate +
                ", startingTime=" + startingTime +
                ", endingTime=" + endingTime +
                ", workingDayType='" + workingDayType + '\'' +
                ", collaborator=" + collaborator.getUser().getUserName() +
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