package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private double ammount;
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Payee payee;

    // Getters and setters
}
