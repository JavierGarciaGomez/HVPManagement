package com.JGG.HVPManagement.tests;

import com.JGG.HVPManagement.dao.CollaboratorDAO;
import com.JGG.HVPManagement.entity.Collaborator;

public class TestRandom {
    public static void main(String[] args) {
        Collaborator collaborator = CollaboratorDAO.getInstance().getCollaboratorbyId(24);
        System.out.println(collaborator);
    }
}
