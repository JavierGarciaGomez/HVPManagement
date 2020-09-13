package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate openingDate;
    public enum AccountType {DESK, CASH, BANK, INVESTMENT}
    private AccountType accountType;
    private boolean formal;
    private String bankName;
    private long accountNumber;
    private double balance;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;
    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AccountRegister> accountRegisters;

    public Account() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isFormal() {
        return formal;
    }

    public void setFormal(boolean formal) {
        this.formal = formal;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<AccountRegister> getAccountRegisters() {
        return accountRegisters;
    }

    public void setAccountRegisters(List<AccountRegister> accountRegisters) {
        this.accountRegisters = accountRegisters;
    }

    // LIST MANAGERS
    public void addAccountRegister(AccountRegister accountRegister){
        if(this.accountRegisters==null){
            this.accountRegisters=new ArrayList<>();
        }
        this.accountRegisters.add(accountRegister);
        accountRegister.setAccount(this);
    }

    public void removeOpeningHours (AccountRegister accountRegister){
        this.accountRegisters.remove(accountRegister);
        accountRegister.setAccount(null);
    }


    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Account)) return false;
        return id !=null && id.equals(((Account)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}