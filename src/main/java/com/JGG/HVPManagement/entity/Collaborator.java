package com.JGG.WeeklyScheduler.entity;

import javax.persistence.*;


@Entity
@Table(name="collaborators")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="job_position_Id")
    private String jobPositionId;

    @Column(name="weekly_working_hours")
    private int pass;

    @Column(name="is_active")
    private boolean isActive;

    public Collaborator() {
    }

}
