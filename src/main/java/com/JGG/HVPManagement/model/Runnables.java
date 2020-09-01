package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.Collaborator;

import java.time.LocalDate;

public class WorkScheduleError {
    public enum errorType {ERROR, WARNING}
    private errorType errorType;
    private LocalDate localDate;
    private String userName;
    private String desc;

    public WorkScheduleError() {
    }

    public WorkScheduleError(WorkScheduleError.errorType errorType, LocalDate localDate, String userName, String desc) {
        this.errorType = errorType;
        this.localDate = localDate;
        this.userName = userName;
        this.desc = desc;
    }

    public WorkScheduleError.errorType getErrorType() {
        return errorType;
    }

    public void setErrorType(WorkScheduleError.errorType errorType) {
        this.errorType = errorType;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "WorkScheduleError{" +
                "errorType=" + errorType +
                ", localDate=" + localDate +
                ", userName='" + userName + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}
