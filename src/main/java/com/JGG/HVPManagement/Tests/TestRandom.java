package com.JGG.HVPManagement.Tests;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class TestRandom {
    public static void main(String[] args) {
        Collaborator collaborator = CollaboratorDAO.getInstance().getCollaboratorbyId(24);
        System.out.println(collaborator);
    }
}
