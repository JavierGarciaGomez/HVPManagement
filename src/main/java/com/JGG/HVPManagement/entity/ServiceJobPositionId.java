package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.hash;

@Embeddable
public class ServiceJobPositionId implements Serializable {

    @Column(name="service_id")
    private Integer serviceId;
    @Column(name = "jobPosition_id")
    private Integer jobPositionId;

    public ServiceJobPositionId(){}


    public ServiceJobPositionId(Integer serviceId, Integer jobPositionId){
        this.serviceId=serviceId;
        this.jobPositionId=jobPositionId;
    }

    // Getters and setters
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getJobPositionId() {
        return jobPositionId;
    }

    public void setJobPositionId(Integer jobPositionId) {
        this.jobPositionId = jobPositionId;
    }

    // Equals, haschode and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o==null || getClass() != o.getClass()) return false;

        ServiceJobPositionId that = (ServiceJobPositionId) o;
        return Objects.equals(this.serviceId, that.serviceId) &&
                Objects.equals(this.jobPositionId, that.jobPositionId);
    }

    @Override
    public int hashCode() {
        return hash(serviceId, jobPositionId);
    }

}