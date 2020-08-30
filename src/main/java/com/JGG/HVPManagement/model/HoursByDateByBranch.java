package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.Branch;

import java.time.LocalDate;

public class HoursByDateByBranch {
    private Branch branch;
    private LocalDate localDate;
    private int minuteSum;

    public HoursByDateByBranch(Branch branch, LocalDate localDate, int minuteSum) {
        this.branch = branch;
        this.localDate = localDate;
        this.minuteSum = minuteSum;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public int getMinuteSum() {
        return minuteSum;
    }

    public void setMinuteSum(int minuteSum) {
        this.minuteSum = minuteSum;
    }

    public int getHours(){
        return this.minuteSum/60;
    }

    public String getDayOfWeek(){
        return String.valueOf(this.localDate.getDayOfWeek());
    }

}
