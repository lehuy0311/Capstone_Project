package com.project.capstone_project.model.employee;

import com.project.capstone_project.model.login.AppUser;

import javax.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "Varchar(40)")
    private String name;
    @Column(columnDefinition = "Varchar(40)")
    private String address;
    @Column(columnDefinition = "Varchar(15)")
    private String phoneNumber;
    @Column(columnDefinition = "Varchar(40)")
    private String email;
    @Column(columnDefinition = "date")
    private String dateOfBirth;
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "app_user_id", referencedColumnName = "user_id", nullable = false)
    private AppUser appUser;
}
