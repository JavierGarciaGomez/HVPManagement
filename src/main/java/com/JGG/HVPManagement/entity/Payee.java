package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Payee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(mappedBy = "payee", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Supplier supplier;
    @OneToOne(mappedBy = "payee", cascade = CascadeType.ALL)
    private Collaborator collaborator;
    @OneToMany(mappedBy = "payee", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<AccountRegister> accountRegisters;

    public Payee() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
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
        accountRegister.setPayee(this);
    }

    public void removeOpeningHours (AccountRegister accountRegister){
        this.accountRegisters.remove(accountRegister);
        accountRegister.setPayee(null);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Payee)) return false;
        return id !=null && id.equals(((Payee)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}