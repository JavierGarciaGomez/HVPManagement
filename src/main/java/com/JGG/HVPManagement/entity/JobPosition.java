package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private double positionWage;

    @Column
    private double yearlyPercentageWageBonus;

    @Column
    private double minimumPositionIncome;

    @OneToMany(mappedBy = "jobPosition", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Collaborator> collaborators;

    // GETTERS AND SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPositionWage() {
        return positionWage;
    }

    public void setPositionWage(double positionWage) {
        this.positionWage = positionWage;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void addCollaborators(Collaborator collaborator){
        if(collaborators==null){
            collaborators=new ArrayList<>();
        }
        this.collaborators.add(collaborator);
        collaborator.setJobPosition(this);
    }

    public double getYearlyPercentageWageBonus() {
        return yearlyPercentageWageBonus;
    }

    public void setYearlyPercentageWageBonus(double wageBonus) {
        this.yearlyPercentageWageBonus = wageBonus;
    }

    public double getMinimumPositionIncome() {
        return minimumPositionIncome;
    }

    public void setMinimumPositionIncome(double minimumPositionIncome) {
        this.minimumPositionIncome = minimumPositionIncome;
    }
}
