package com.JGG.HVPManagement.Tests;

import com.JGG.HVPManagement.entity.Collaborator;
import com.JGG.HVPManagement.entity.DetailedCollaboratorInfo;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestMappings {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        DetailedCollaboratorInfo detailedCollaboratorInfo = new DetailedCollaboratorInfo();
        detailedCollaboratorInfo.setEmail("javieron.garcia@gmail.com");
        detailedCollaboratorInfo.setAddress("Calle San Vicente");
        Collaborator collaborator = new Collaborator();
        collaborator.setFirstName("Javier");

        //collaborator.setDetailedCollaboratorInfo(detailedCollaboratorInfo);
        session.beginTransaction();
        session.save(collaborator);
        session.getTransaction().commit();
        System.out.println("Done");

        session=factory.getCurrentSession();
        session.beginTransaction();
        Collaborator collaborator1 = session.get(Collaborator.class, 7);
        detailedCollaboratorInfo=collaborator1.getDetailedCollaboratorInfo();
        if(detailedCollaboratorInfo==null){
            detailedCollaboratorInfo = new DetailedCollaboratorInfo();
            collaborator1.setDetailedCollaboratorInfo(detailedCollaboratorInfo);
        }
        detailedCollaboratorInfo.setAddress("Calle Las peras");

        session.saveOrUpdate(collaborator1);
        //session.delete(detailedCollaboratorInfo);

        session.getTransaction().commit();
        System.out.println(collaborator1);

        //
        JobPosition jobPosition = new JobPosition();
        jobPosition.addCollaborators(collaborator1);
        jobPosition.setName("Jefazo");
        System.out.println(collaborator1);

        session=factory.getCurrentSession();
        session.beginTransaction();
        session.save(jobPosition);
        session.save(collaborator1);
        session.getTransaction().commit();






    }
}
