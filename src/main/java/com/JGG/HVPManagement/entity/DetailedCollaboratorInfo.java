package com.JGG.HVPManagement.entity;

import javax.persistence.*;


@Entity
@Table
public class DetailedCollaboratorInfo {
    @Id
    @Column
    private int id;

    @Column
    private String curpNumber;

    @Column
    private String imssNumber;

    @Column
    private String rfcNumber;

    @Column
    private String phoneNumber;

    @Column
    private String mobilePhoneNumber;

    @Column
    private String emergencyPhoneNumber;

    @Column
    private String email;

    @Column
    private String address;

    @OneToOne()
    @MapsId
    private Collaborator collaborator;

    public DetailedCollaboratorInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public void setEmergencyPhoneNumber(String emergencyNumber) {
        this.emergencyPhoneNumber = emergencyNumber;
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
