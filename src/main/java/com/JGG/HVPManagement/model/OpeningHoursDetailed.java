package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.Branch;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class OpeningHoursDetailed {
    private Branch branch;
    private LocalDate localDate;
    private LocalDateTime openingHour;
    private LocalDateTime closingHour;

    public OpeningHoursDetailed() {
    }

    public OpeningHoursDetailed(Branch branch, LocalDate localDate, LocalDateTime openingHour, LocalDateTime closingHour) {
        this.branch = branch;
        this.localDate = localDate;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
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

    public LocalDateTime getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(LocalDateTime openingHour) {
        this.openingHour = openingHour;
    }

    public LocalDateTime getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(LocalDateTime closingHour) {
        this.closingHour = closingHour;
    }
}
