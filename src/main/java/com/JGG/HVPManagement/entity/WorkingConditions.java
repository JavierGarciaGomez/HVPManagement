package com.JGG.HVPManagement.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class WorkingConditions {
    @Id
    private int id;
    private Integer weeklyWorkingHours;
    private Double wageProportion;
    private Double fixedWageBonus;
    private Double degreeBonus;
    private Double seniorityWageBonus;
    private Double grossWage;
    private Double monthlyMinimumIncome;
    private Double commissionBonusPercentage;
    private Double averageDailyWage;
    private Double contributionBaseWage;
    private String paymentForm;
    private Boolean hasIMSS;
    private LocalDate startingDate;
    private LocalDate startingIMSSDate;
    private LocalDate endingDate;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Collaborator collaborator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getWeeklyWorkingHours() {
        return weeklyWorkingHours;
    }

    public void setWeeklyWorkingHours(Integer weeklyWorkingHours) {
        this.weeklyWorkingHours = weeklyWorkingHours;
    }

    public Double getFixedWageBonus() {
        return fixedWageBonus;
    }

    public void setFixedWageBonus(Double fixedWageBonus) {
        this.fixedWageBonus = fixedWageBonus;
    }

    public Double getDegreeBonus() {
        return degreeBonus;
    }

    public void setDegreeBonus(Double degreeBonus) {
        this.degreeBonus = degreeBonus;
    }

    public Double getGrossWage() {
        return grossWage;
    }

    public void setGrossWage(Double grossWage) {
        this.grossWage = grossWage;
    }

    public Double getCommissionBonusPercentage() {
        return commissionBonusPercentage;
    }

    public void setCommissionBonusPercentage(Double comissionBonusPercentage) {
        this.commissionBonusPercentage = comissionBonusPercentage;
    }

    public Double getAverageDailyWage() {
        return averageDailyWage;
    }

    public void setAverageDailyWage(Double averageDailyWage) {
        this.averageDailyWage = averageDailyWage;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public Boolean isHasIMSS() {
        return hasIMSS;
    }

    public void setHasIMSS(Boolean hasIMSS) {
        this.hasIMSS = hasIMSS;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getStartingIMSSDate() {
        return startingIMSSDate;
    }

    public void setStartingIMSSDate(LocalDate startingIMSSDate) {
        this.startingIMSSDate = startingIMSSDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public Double getSeniorityWageBonus() {
        return seniorityWageBonus;
    }

    public void setSeniorityWageBonus(Double yearlyPercentageBonus) {
        this.seniorityWageBonus = yearlyPercentageBonus;
    }

    public Boolean getHasIMSS() {
        return hasIMSS;
    }

    public Double getMonthlyMinimumIncome() {
        return monthlyMinimumIncome;
    }

    public void setMonthlyMinimumIncome(Double monthlyMinimumIncome) {
        this.monthlyMinimumIncome = monthlyMinimumIncome;
    }

    public Double getContributionBaseWage() {
        return contributionBaseWage;
    }

    public void setContributionBaseWage(Double contributionBaseWage) {
        this.contributionBaseWage = contributionBaseWage;
    }

    public Double getWageProportion() {
        return wageProportion;
    }

    public void setWageProportion(Double wageProportion) {
        this.wageProportion = wageProportion;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    @Override
    public String toString() {
        return "WorkingConditions{" +
                "id=" + id +
                ", weeklyWorkingHours=" + weeklyWorkingHours +
                ", wageProportion=" + wageProportion +
                ", fixedWageBonus=" + fixedWageBonus +
                ", degreeBonus=" + degreeBonus +
                ", seniorityWageBonus=" + seniorityWageBonus +
                ", grossWage=" + grossWage +
                ", monthlyMinimumIncome=" + monthlyMinimumIncome +
                ", commissionBonusPercentage=" + commissionBonusPercentage +
                ", averageDailyWage=" + averageDailyWage +
                ", contributionBaseWage=" + contributionBaseWage +
                ", paymentForm='" + paymentForm + '\'' +
                ", hasIMSS=" + hasIMSS +
                ", startingDate=" + startingDate +
                ", startingIMSSDate=" + startingIMSSDate +
                ", endingDate=" + endingDate +
                '}';
    }
}
