package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
public class Payee {
    @Id
    private Integer id;
    private LocalDate date;
    private double ammount;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator collaborator;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Supplier supplier;


    // Getters and setters
}
