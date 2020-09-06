package com.JGG.HVPManagement.entity;

import com.JGG.HVPManagement.model.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // id, tipo, colaborador, fecha de registro, fecha ocurrida, estado, resuelto en, resuelto por
    public enum incidentTypes {ATTENDANCE_REGISTER, COMMISSIONS, ACCOUNTING, INVESTIGATION, PAYROLL, SERVICES, WORK_SCHEDULE}
    private incidentTypes incidentType;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator collaborator;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Branch branch;
    private LocalDate dateOfOccurrence;
    private String description;
    private LocalDateTime registerDate;
    private boolean solved;
    private LocalDateTime solvedDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Collaborator solvedBy;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public incidentTypes getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(incidentTypes incidentType) {
        this.incidentType = incidentType;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public LocalDate getDateOfOccurrence() {
        return dateOfOccurrence;
    }

    public void setDateOfOccurrence(LocalDate dateOfOccurrence) {
        this.dateOfOccurrence = dateOfOccurrence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public LocalDateTime getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(LocalDateTime solvedDate) {
        this.solvedDate = solvedDate;
    }

    public Collaborator getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(Collaborator solvedBy) {
        this.solvedBy = solvedBy;
    }

    // Other methods
    public String getDateAsString() {
        return Model.getInstance().DTF.format(dateOfOccurrence);
    }
    public String getDateSolvedAsString() {
        return Model.getInstance().DTF.format(solvedDate);
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Incident)) return false;
        return id !=null && id.equals(((Incident)o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "collaborator=" + collaborator +
                ", dateOfOccurrence=" + dateOfOccurrence +
                ", description='" + description + '\'' +
                '}';
    }
}
