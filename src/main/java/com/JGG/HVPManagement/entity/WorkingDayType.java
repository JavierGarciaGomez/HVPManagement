package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class WorkingDayType {
    public WorkingDayType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String abbr;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean itNeedHours;

    @Column
    private boolean itNeedBranches;

    @OneToMany(mappedBy = "workingDayTypeNew", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<WorkSchedule> workSchedules;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void addWorkSchedules(WorkSchedule workSchedule){
        if(workSchedules==null){
            workSchedules=new ArrayList<>();
        }
        this.workSchedules.add(workSchedule);
        workSchedule.setWorkingDayTypeNew(this);
    }
}
