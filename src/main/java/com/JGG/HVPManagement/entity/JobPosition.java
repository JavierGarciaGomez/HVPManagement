package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double positionWage;
    private double yearlyPercentageWageBonus;
    private double minimumPositionIncome;
    @OneToMany(mappedBy = "jobPosition", orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Collaborator> collaborators;

    // GETTERS AND SETTERS
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

    public double getPositionWage() {
        return positionWage;
    }

    public void setPositionWage(double positionWage) {
        this.positionWage = positionWage;
    }

    public double getYearlyPercentageWageBonus() {
        return yearlyPercentageWageBonus;
    }

    public void setYearlyPercentageWageBonus(double yearlyPercentageWageBonus) {
        this.yearlyPercentageWageBonus = yearlyPercentageWageBonus;
    }

    public double getMinimumPositionIncome() {
        return minimumPositionIncome;
    }

    public void setMinimumPositionIncome(double minimumPositionIncome) {
        this.minimumPositionIncome = minimumPositionIncome;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    // LIST MANAGERS
    public void addCollaborator(Collaborator collaborator){
        if(collaborators==null){
            collaborators=new ArrayList<>();
        }
        this.collaborators.add(collaborator);
        collaborator.setJobPosition(this);
    }

    public void removeCollaborator (Collaborator collaborator){
        this.collaborators.remove(collaborator);
        collaborator.setJobPosition(null);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof JobPosition)) return false;
        return id !=null && id.equals(((JobPosition)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JobPosition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", positionWage=" + positionWage +
                ", yearlyPercentageWageBonus=" + yearlyPercentageWageBonus +
                ", minimumPositionIncome=" + minimumPositionIncome +
                '}';
    }


}
