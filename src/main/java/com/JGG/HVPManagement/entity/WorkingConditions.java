package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class WorkingConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private Integer weeklyWorkingHours;

    @Column
    private Double wageProportion;

    @Column
    private Double fixedWageBonus;

    @Column
    private Double degreeBonus;

    @Column
    private Double seniorityWageBonus;

    @Column
    private Double grossWage;

    @Column
    private Double monthlyMinimumIncome;

    @Column
    private Double commissionBonusPercentage;

    @Column
    private Double averageDailyWage;

    @Column
    private Double contributionBaseWage;


    @Column
    private String paymentForm;

    @Column
    private Boolean hasIMSS;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate startingIMSSDate;

    @Column
    private LocalDate endingDate;

    @OneToOne(mappedBy = "workingConditions", cascade=CascadeType.ALL)
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

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
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
