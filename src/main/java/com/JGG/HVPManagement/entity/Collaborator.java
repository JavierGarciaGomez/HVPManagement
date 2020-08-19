package com.JGG.HVPManagement.entity;

import javax.persistence.*;


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
}
