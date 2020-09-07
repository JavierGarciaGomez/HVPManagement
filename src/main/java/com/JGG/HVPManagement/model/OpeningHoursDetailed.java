package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.Branch;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class OpeningHoursDetailed {
    private Branch branch;
    private LocalDate startDate;
    private LocalDateTime openingHour;
    private LocalDateTime closingHour;

    public OpeningHoursDetailed(Branch branch, LocalDate startDate, LocalDateTime openingHour, LocalDateTime closingHour) {
        this.branch = branch;
        this.startDate = startDate;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }
}
