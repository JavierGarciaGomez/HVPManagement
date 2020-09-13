package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountingConceptGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "accountingConceptGroup", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AccountingConceptClass> accountingConceptClasses;

    public AccountingConceptGroup() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public List<AccountingConceptClass> getAccountingConceptClasses() {
        return accountingConceptClasses;
    }

    public void setAccountingConceptClasses(List<AccountingConceptClass> accountingConceptClasses) {
        this.accountingConceptClasses = accountingConceptClasses;
    }


    // LIST MANAGERS
    public void addAccountingConceptClass(AccountingConceptClass accountingConceptClass){
        if(this.accountingConceptClasses==null){
            this.accountingConceptClasses=new ArrayList<>();
        }
        this.accountingConceptClasses.add(accountingConceptClass);
        accountingConceptClass.setAccountingConceptGroup(this);
    }

    public void removeAccountingConceptClass (AccountingConceptClass accountingConceptClass){
        this.accountingConceptClasses.remove(accountingConceptClass);
        accountingConceptClass.setAccountingConceptGroup(null);
    }


    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AccountingConceptGroup)) return false;
        return id !=null && id.equals(((AccountingConceptGroup)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}