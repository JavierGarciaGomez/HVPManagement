package com.JGG.HVPManagement.entity;

import javax.persistence.*;


@Entity
public class DetailedCollaboratorInfo {
    @Id
    private Integer id;
    private String curpNumber;
    private String imssNumber;
    private String rfcNumber;
    private String phoneNumber;
    private String mobilePhoneNumber;
    private String emergencyPhoneNumber;
    private String email;
    private String address;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Collaborator collaborator;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurpNumber() {
        return curpNumber;
    }

    public void setCurpNumber(String curpNumber) {
        this.curpNumber = curpNumber;
    }

    public String getImssNumber() {
        return imssNumber;
    }

    public void setImssNumber(String imssNumber) {
        this.imssNumber = imssNumber;
    }

    public String getRfcNumber() {
        return rfcNumber;
    }

    public void setRfcNumber(String rfcNumber) {
        this.rfcNumber = rfcNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    @Override
    public String toString() {
        return "DetailedCollaboratorInfo{" +
                "id=" + id +
                ", curpNumber='" + curpNumber + '\'' +
                ", imssNumber='" + imssNumber + '\'' +
                ", rfcNumber='" + rfcNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", emergencyPhoneNumber='" + emergencyPhoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
