package com.JGG.HVPManagement.entity;

import javax.persistence.*;


@Entity
@Table(name="collaborators")
public class Collaborator {
    public Collaborator() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="is_active")
    private Boolean isActive;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="detailed_collaborator_info_id")
    private DetailedCollaboratorInfo detailedCollaboratorInfo;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="work_conditions_info_id")
    private WorkConditionsInfo workConditionsInfo;

    @ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="position_id")
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

    public Boolean isActive() {
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

    public WorkConditionsInfo getWorkConditionsInfo() {
        return workConditionsInfo;
    }

    public void setWorkConditionsInfo(WorkConditionsInfo workConditionsInfo) {
        this.workConditionsInfo = workConditionsInfo;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
