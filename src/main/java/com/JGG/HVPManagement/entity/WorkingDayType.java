package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class WorkingDayType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String abbr;
    private String name;
    private String description;
    private boolean itNeedHours;
    private boolean itNeedBranches;
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedules;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isItNeedHours() {
        return itNeedHours;
    }

    public void setItNeedHours(boolean itNeedHours) {
        this.itNeedHours = itNeedHours;
    }

    public boolean isItNeedBranches() {
        return itNeedBranches;
    }

    public void setItNeedBranches(boolean itNeedBranches) {
        this.itNeedBranches = itNeedBranches;
    }

    public List<WorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(List<WorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    // LIST MANAGERS
    public void addWorkSchedule(WorkSchedule workSchedule){
        if(workSchedules==null){
            workSchedules=new ArrayList<>();
        }
        this.workSchedules.add(workSchedule);
        workSchedule.setWorkingDayType(this);
    }

    public void removeWorkSchedule (WorkSchedule workSchedule){
        this.workSchedules.remove(workSchedule);
        workSchedule.setWorkingDayType(null);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingDayType that = (WorkingDayType) o;
        if (!id.equals(that.id)) return false;
        return Objects.equals(abbr, that.abbr);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (abbr != null ? abbr.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return abbr;
    }
}
