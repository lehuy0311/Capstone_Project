package com.project.capstone_project.model.animal;

import javax.persistence.*;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "Varchar(40")
    private String name;
    private String description;
    private int quantity;
    private String picture;
    @Column(columnDefinition = "date")
    private String dateImport;
    @ManyToOne
    @JoinColumn(name = "animal_type_id", referencedColumnName = "id")
    private AnimalType animalType;

    public Animal() {
    }

    public Animal(Integer id, String name, String description, int quantity,
                  String picture, String dateImport, AnimalType animalType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.picture = picture;
        this.dateImport = dateImport;
        this.animalType = animalType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDateImport() {
        return dateImport;
    }

    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }
}
