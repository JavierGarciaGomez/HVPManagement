package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountingConceptClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String desc;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private AccountingConceptGroup accountingConceptGroup;
    @OneToMany(mappedBy = "accountingConceptClass", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AccountingConcept> accountingConcepts;

    public AccountingConceptClass() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccountingConceptGroup getAccountingConceptGroup() {
        return accountingConceptGroup;
    }

    public void setAccountingConceptGroup(AccountingConceptGroup accountingConceptGroup) {
        this.accountingConceptGroup = accountingConceptGroup;
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

    public List<AccountingConcept> getAccountingConcepts() {
        return accountingConcepts;
    }

    public void setAccountingConcepts(List<AccountingConcept> accountingConcepts) {
        this.accountingConcepts = accountingConcepts;
    }

    // LIST MANAGERS
    public void addAccountingConcept(AccountingConcept accountingConcept){
        if(this.accountingConcepts==null){
            this.accountingConcepts=new ArrayList<>();
        }
        this.accountingConcepts.add(accountingConcept);
        accountingConcept.setAccountingConceptClass(this);
    }

    public void removeAccountingConcept (AccountingConcept accountingConcept){
        this.accountingConcepts.remove(accountingConcept);
        accountingConcept.setAccountingConceptClass(this);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AccountingConceptClass)) return false;
        return id !=null && id.equals(((AccountingConceptClass)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}