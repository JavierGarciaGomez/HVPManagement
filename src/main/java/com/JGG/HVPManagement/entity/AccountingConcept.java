package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountingConcept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String desc;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private AccountingConceptClass accountingConceptClass;
    @OneToMany(mappedBy = "accountingConcept", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AccountRegister> accountRegisters;

    public AccountingConcept() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountingConceptClass getAccountingConceptClass() {
        return accountingConceptClass;
    }

    public void setAccountingConceptClass(AccountingConceptClass accountingConceptClass) {
        this.accountingConceptClass = accountingConceptClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        accountRegister.setAccountingConcept(this);
    }

    public void removeAccountRegister (AccountRegister accountRegister){
        this.accountRegisters.remove(accountRegister);
        accountRegister.setAccountingConcept(null);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AccountingConcept)) return false;
        return id !=null && id.equals(((AccountingConcept)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}