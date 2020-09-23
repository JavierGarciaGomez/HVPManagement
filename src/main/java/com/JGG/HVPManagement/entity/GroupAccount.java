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
    private List<ClassAccount> classAccounts;

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

    public List<ClassAccount> getClassAccounts() {
        return classAccounts;
    }

    public void setClassAccounts(List<ClassAccount> classAccounts) {
        this.classAccounts = classAccounts;
    }


    // LIST MANAGERS
    public void addAccountingConceptClass(ClassAccount classAccount){
        if(this.classAccounts ==null){
            this.classAccounts =new ArrayList<>();
        }
        this.classAccounts.add(classAccount);
        classAccount.setAccountingConceptGroup(this);
    }

    public void removeAccountingConceptClass (ClassAccount classAccount){
        this.classAccounts.remove(classAccount);
        classAccount.setAccountingConceptGroup(null);
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