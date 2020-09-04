package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate localDate;
    private LocalTime startingTime;
    private LocalTime endingTime;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private WorkingDayType workingDayType;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator registeredBy;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator collaborator;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public WorkingDayType getWorkingDayType() {
        return workingDayType;
    }

    public void setWorkingDayType(WorkingDayType workingDayType) {
        this.workingDayType = workingDayType;
    }

    public Collaborator getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(Collaborator registeredBy) {
        this.registeredBy = registeredBy;
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
    public boolean equalsCollaboratorAndDate(WorkSchedule otherWorkSchedule){
        if(this== otherWorkSchedule) return true;
        return this.getCollaborator().getId().equals(otherWorkSchedule.getCollaborator().getId()) &&
                this.getLocalDate().equals(otherWorkSchedule.getLocalDate());
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof WorkSchedule)) return false;
        return id !=null && id.equals(((WorkSchedule)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "'"+collaborator.getUser().getUserName() + " "+ workingDayType.getAbbr() + " "+startingTime + " "+endingTime+"'";
    }
}