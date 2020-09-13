package com.JGG.HVPManagement.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@NaturalIdCache
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Service {
    @Id
    private Integer id;
    @NaturalId
    private String name;

    public enum ServiceType {FIXED, PROPORTION}

    private Double price;
    private Double commissionPctBase;
    private Double maxPctCommission;
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceJobPosition> jobPositions = new ArrayList<>();

    public Service() {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCommissionPctBase() {
        return commissionPctBase;
    }

    public void setCommissionPctBase(Double commissionPctBase) {
        this.commissionPctBase = commissionPctBase;
    }

    public Double getMaxPctCommission() {
        return maxPctCommission;
    }

    public void setMaxPctCommission(Double maxPctCommission) {
        this.maxPctCommission = maxPctCommission;
    }

    public List<ServiceJobPosition> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<ServiceJobPosition> jobPositions) {
        this.jobPositions = jobPositions;
    }

    // LIST MANAGERS
    public void addJobPosition(JobPosition jobPosition) {
        ServiceJobPosition serviceJobPosition = new ServiceJobPosition(this, jobPosition);
        jobPositions.add(serviceJobPosition);
    }

    public void removeJobPosition(JobPosition jobPosition) {
        for (Iterator<ServiceJobPosition> iterator = jobPositions.iterator();
             iterator.hasNext(); ) {
            ServiceJobPosition serviceJobPosition = iterator.next();

            if (serviceJobPosition.getService().equals(this) &&
                    serviceJobPosition.getJobPosition().equals(jobPosition)) {
                iterator.remove();
                serviceJobPosition.setService(null);
                serviceJobPosition.setJobPosition(null);
            }
        }
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        return id != null && id.equals(((Service) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}