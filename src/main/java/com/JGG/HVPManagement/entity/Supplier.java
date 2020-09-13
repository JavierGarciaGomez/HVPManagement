package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "supplier", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Payee> payees;


    // GETTERS AND SETTERS}
}
