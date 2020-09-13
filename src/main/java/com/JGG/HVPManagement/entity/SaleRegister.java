package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class SaleRegister {
    @Id
    private Integer id;
    private Integer ticketNumber;
    private Boolean isFormal;
    private LocalDate date;
    private LocalTime time;
    private Double cashAmount;
    private Double creditAmount;
    private Double totalAmount;
    private Boolean needsInvoice;
    public enum ServiceType {SERVICE, SELL}
    private ServiceType serviceType;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator seller;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private AccountRegister accountRegister;

    public SaleRegister() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Boolean getFormal() {
        return isFormal;
    }

    public void setFormal(Boolean formal) {
        isFormal = formal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getNeedsInvoice() {
        return needsInvoice;
    }

    public void setNeedsInvoice(Boolean needsInvoice) {
        this.needsInvoice = needsInvoice;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Collaborator getSeller() {
        return seller;
    }

    public void setSeller(Collaborator seller) {
        this.seller = seller;
    }

    public AccountRegister getAccountRegister() {
        return accountRegister;
    }

    public void setAccountRegister(AccountRegister accountRegister) {
        this.accountRegister = accountRegister;
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof SaleRegister)) return false;
        return id !=null && id.equals(((SaleRegister)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}