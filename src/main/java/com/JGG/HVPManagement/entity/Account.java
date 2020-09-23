package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private ClassAccount classAccount;
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

    public ClassAccount getClassAccount() {
        return classAccount;
    }

    public void setClassAccount(ClassAccount classAccount) {
        this.classAccount = classAccount;
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

    public void removeAccountRegister (AccountRegister accountRegister){
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