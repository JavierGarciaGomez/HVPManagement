package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate startDate;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(LocalTime openingHour) {
        this.openingHour = openingHour;
    }

    public LocalTime getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(LocalTime closingHour) {
        this.closingHour = closingHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof OpeningHours)) return false;
        return id !=null && id.equals(((OpeningHours)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", openingHour=" + openingHour +
                ", closingHour=" + closingHour +
                ", branch=" + branch +
                '}';
    }
}
