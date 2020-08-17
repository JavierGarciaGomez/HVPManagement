package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class WorkConditionsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private Integer weeklyWorkingHours;

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
    private String paymentForm;

    @Column
    private Boolean hasIMSS;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate startingIMSSDate;

    @Column
    private LocalDate endingDate;

    @Column
    private Integer workedDays;

    @Column
    private Integer quartersWorked;

    @OneToOne(mappedBy = "workConditionsInfo", cascade=CascadeType.ALL)
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

    public Integer getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(Integer workedDays) {
        this.workedDays = workedDays;
    }

    public Integer getQuartersWorked() {
        return quartersWorked;
    }

    public void setQuartersWorked(Integer quartersWorked) {
        this.quartersWorked = quartersWorked;
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

    @Override
    public String toString() {
        return "WorkConditionsInfo{" +
                "id=" + id +
                ", weeklyWorkingHours=" + weeklyWorkingHours +
                ", fixedWageBonus=" + fixedWageBonus +
                ", degreeBonus=" + degreeBonus +
                ", grossWage=" + grossWage +
                ", comissionBonusPercentage=" + commissionBonusPercentage +
                ", averageDailyWage=" + averageDailyWage +
                ", paymentForm='" + paymentForm + '\'' +
                ", hasIMSS=" + hasIMSS +
                ", startingDate=" + startingDate +
                ", startingIMSSDate=" + startingIMSSDate +
                ", endingDate=" + endingDate +
                ", workedDays=" + workedDays +
                ", quartersWorked=" + quartersWorked +
                ", collaborator=" + collaborator +
                '}';
    }
}
