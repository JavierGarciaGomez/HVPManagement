package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Collaborator {
    public Collaborator() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int collaboratorId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Boolean isActive;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private DetailedCollaboratorInfo detailedCollaboratorInfo;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private User user;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn
    private WorkingConditions workingConditions;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn
    private JobPosition jobPosition;

    @OneToMany(mappedBy = "collaborator", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedules;

    @OneToMany(mappedBy = "registeredBy", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedulesRegistered;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public DetailedCollaboratorInfo getDetailedCollaboratorInfo() {
        return detailedCollaboratorInfo;
    }

    public void setDetailedCollaboratorInfo(DetailedCollaboratorInfo detailedCollaboratorInfo) {
        this.detailedCollaboratorInfo = detailedCollaboratorInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WorkingConditions getWorkingConditions() {
        return workingConditions;
    }

    public void setWorkingConditions(WorkingConditions workingConditions) {
        this.workingConditions = workingConditions;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    public int getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(int collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

    public List<WorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(List<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    public List<WorkSchedule> getWorkSchedulesRegistered() {
        return workSchedulesRegistered;
    }

    public void setWorkSchedulesRegistered(List<WorkSchedule> workSchedulesRegistered) {
        this.workSchedulesRegistered = workSchedulesRegistered;
    }

    public void addWorkSchedule(WorkSchedule workSchedule){
        if(workSchedules==null){
            workSchedules=new ArrayList<>();
        }
        this.workSchedules.add(workSchedule);
        workSchedule.setCollaborator(this);
    }

    public void addWorkSchedulesRegistered(WorkSchedule workSchedule){
        if(workSchedulesRegistered==null){
            workSchedulesRegistered=new ArrayList<>();
        }
        this.workSchedulesRegistered.add(workSchedule);
        workSchedule.setCollaborator(this);
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                ", collaboratorId=" + collaboratorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", detailedCollaboratorInfo=" + detailedCollaboratorInfo +
                ", user=" + user +
                ", workingConditions=" + workingConditions +
                ", jobPosition=" + jobPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collaborator that = (Collaborator) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
