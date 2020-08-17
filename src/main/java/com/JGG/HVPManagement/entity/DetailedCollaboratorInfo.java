package com.JGG.WeeklyScheduler.entity;

import javax.persistence.*;


@Entity
@Table(name="collaborators")
public class DetailedCollaboratorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="CURP_number")
    private String curpNumber;

    @Column(name="imss_number")
    private String imssNumber;

    @Column(name="rfc_number")
    private String rfcNumber;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name="emergency_number")
    private String emergencyNumber;

    @Column(name="email")
    private String email;

    @Column(name="address")
    private String address;

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

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
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

    @Override
    public String toString() {
        return "DetailedCollaboratorInfo{" +
                "id=" + id +
                ", curpNumber='" + curpNumber + '\'' +
                ", imssNumber='" + imssNumber + '\'' +
                ", rfcNumber='" + rfcNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", emergencyNumber='" + emergencyNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
