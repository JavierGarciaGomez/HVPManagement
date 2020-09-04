package com.JGG.HVPManagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String branch;
    private String veterinarian;
    private String clientName;
    private String phone;
    private String petName;
    private String service;
    private String motive;
    private LocalDate date;
    private LocalTime time;

    public Appointment() {
    }

    public Appointment(String branch, String veterinarian, String clientName, String phone, String petName, String service, String motive, LocalDate date, LocalTime time) {
        this.veterinarian = veterinarian;
        this.petName = petName;
        this.clientName = clientName;
        this.branch = branch;
        this.service = service;
        this.motive = motive;
        this.phone=phone;
        this.date = date;
        this.time = time;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", branch='" + branch + '\'' +
                ", veterinarian='" + veterinarian + '\'' +
                ", clientName='" + clientName + '\'' +
                ", phone='" + phone + '\'' +
                ", petName='" + petName + '\'' +
                ", service='" + service + '\'' +
                ", motive='" + motive + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}