package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
public class ServiceJobPosition implements Serializable {

    @EmbeddedId
    private ServiceJobPositionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceId")
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobPositionId")
    private JobPosition jobPosition;

    private Double commissionAmount;

    public ServiceJobPosition(){}

    public ServiceJobPosition(Service service, JobPosition jobPosition) {
        this.service=service;
        this.jobPosition=jobPosition;
    }

    // Getters and setters
    public ServiceJobPositionId getId() {
        return id;
    }

    public void setId() {
        this.id = new ServiceJobPositionId(this.service.getId(), this.jobPosition.getId());
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;

        ServiceJobPosition that = (ServiceJobPosition) o;
        return Objects.equals(this.service, that.service) &&
                Objects.equals(this.jobPosition, that.jobPosition);
    }

    @Override
    public int hashCode() {
        return hash(service, jobPosition);
    }

}