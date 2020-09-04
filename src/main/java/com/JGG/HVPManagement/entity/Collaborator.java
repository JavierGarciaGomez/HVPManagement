package com.JGG.HVPManagement.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int collaboratorId;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    @OneToOne(mappedBy = "collaborator", cascade = CascadeType.ALL)
    private DetailedCollaboratorInfo detailedCollaboratorInfo;
    @OneToOne(mappedBy = "collaborator", cascade = CascadeType.ALL)
    private User user;
    @OneToOne(mappedBy = "collaborator", cascade = CascadeType.ALL)
    private WorkingConditions workingConditions;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private JobPosition jobPosition;
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedules;
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedulesRegistered;
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AttendanceRegister> attendanceRegisters;

    // GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(int collaboratorId) {
        this.collaboratorId = collaboratorId;
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

    public List<AttendanceRegister> getAttendanceRegisters() {
        return attendanceRegisters;
    }

    public void setAttendanceRegisters(List<AttendanceRegister> attendanceRegisters) {
        this.attendanceRegisters = attendanceRegisters;
    }

    // LIST MANAGERS
    public void addWorkSchedule(WorkSchedule workSchedule){
        if(workSchedules==null){
            workSchedules=new ArrayList<>();
        }
        this.workSchedules.add(workSchedule);
        workSchedule.setCollaborator(this);
    }

    public void removeWorkSchedule (WorkSchedule workSchedule){
        this.workSchedules.remove(workSchedule);
        workSchedule.setCollaborator(null);
    }

    public void addWorkScheduleRegistered(WorkSchedule workSchedule){
        if(workSchedulesRegistered==null){
            workSchedulesRegistered=new ArrayList<>();
        }
        this.workSchedulesRegistered.add(workSchedule);
        workSchedule.setCollaborator(this);
    }

    public void removeWorkScheduleRegistered (WorkSchedule workSchedule){
        this.workSchedulesRegistered.remove(workSchedule);
        workSchedule.setCollaborator(null);
    }

    public void addAttendanceRegister(AttendanceRegister attendanceRegister){
        if(attendanceRegisters==null){
            attendanceRegisters=new ArrayList<>();
        }
        this.attendanceRegisters.add(attendanceRegister);
        attendanceRegister.setCollaborator(this);
    }

    public void removeAttendanceRegister (AttendanceRegister attendanceRegister){
        this.attendanceRegisters.remove(attendanceRegister);
        attendanceRegister.setCollaborator(null);
    }


    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Collaborator)) return false;
        return id !=null && id.equals(((Collaborator)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                ", collaboratorId=" + collaboratorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", user=" + user.getUserName() +
                ", jobPosition=" + jobPosition.getName() +
                '}';
    }
}
