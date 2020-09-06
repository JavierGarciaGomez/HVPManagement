package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String abbr;
    private String name;
    private LocalDate openingDay;
    private String address;
    private String phoneNumber;
    private String whatsappNumber;
    @OneToMany(mappedBy = "branch", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<OpeningHours> openingHours;
    @OneToMany(mappedBy = "branch", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedules;
    @OneToMany(mappedBy = "branch", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AttendanceRegister> attendanceRegisters;
    @OneToMany(mappedBy = "branch", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Incident> incidents;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(LocalDate openingDay) {
        this.openingDay = openingDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public List<WorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(List<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    public List<AttendanceRegister> getAttendanceRegisters() {
        return attendanceRegisters;
    }

    public void setAttendanceRegisters(List<AttendanceRegister> attendanceRegisters) {
        this.attendanceRegisters = attendanceRegisters;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    // LIST MANAGERS
    public void addOpeningHours(OpeningHours openingHours){
        if(this.openingHours==null){
            this.openingHours=new ArrayList<>();
        }
        this.openingHours.add(openingHours);
        openingHours.setBranch(this);
    }

    public void removeOpeningHours (OpeningHours openingHours){
        this.openingHours.remove(openingHours);
        openingHours.setBranch(null);
    }

    public void addWorkSchedule(WorkSchedule workSchedule){
        if(workSchedules==null){
            workSchedules=new ArrayList<>();
        }
        this.workSchedules.add(workSchedule);
        workSchedule.setBranch(this);
    }

    public void removeWorkSchedule (WorkSchedule workSchedule){
        this.workSchedules.remove(workSchedule);
        workSchedule.setBranch(null);
    }

    public void addAttendanceRegister(AttendanceRegister attendanceRegister){
        if(attendanceRegisters==null){
            attendanceRegisters=new ArrayList<>();
        }
        this.attendanceRegisters.add(attendanceRegister);
        attendanceRegister.setBranch(this);
    }

    public void removeAttendanceRegister (AttendanceRegister attendanceRegister){
        this.attendanceRegisters.remove(attendanceRegister);
        attendanceRegister.setBranch(null);
    }

    public void addIncident(Incident incident){
        if(incidents==null){
            incidents=new ArrayList<>();
        }
        this.incidents.add(incident);
        incident.setBranch(this);
    }

    public void removeIncident (Incident incident){
        this.incidents.remove(incident);
        incident.setBranch(null);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        if (!id.equals(branch.id)) return false;
        return Objects.equals(abbr, branch.abbr);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (abbr != null ? abbr.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
