package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ClassAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private GroupAccount groupAccount;
    @OneToMany(mappedBy = "classAccount", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Account> accounts;

    public ClassAccount() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupAccount getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(GroupAccount groupAccount) {
        this.groupAccount = groupAccount;
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    // LIST MANAGERS
    public void addAccountingConcept(Account account){
        if(this.accounts ==null){
            this.accounts =new ArrayList<>();
        }
        this.accounts.add(account);
        account.setClassAccount(this);
    }

    public void removeAccountingConcept (Account account){
        this.accounts.remove(account);
        account.setClassAccount(this);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof ClassAccount)) return false;
        return id !=null && id.equals(((ClassAccount)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}