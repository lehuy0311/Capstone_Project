package com.project.capstone_project.model.feedback;

import com.project.capstone_project.model.animal.Animal;

import javax.persistence.*;

@Entity
public class FeedbackDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantityComment;

    @ManyToOne
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private Feedback feedback;
    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    public FeedbackDetail() {
    }

    public FeedbackDetail(Integer id, Integer quantityComment, Feedback feedback, Animal animal) {
        this.id = id;
        this.quantityComment = quantityComment;
        this.feedback = feedback;
        this.animal = animal;
    }

    public FeedbackDetail(Integer quantityComment, Feedback feedback, Animal animal) {
        this.quantityComment = quantityComment;
        this.feedback = feedback;
        this.animal = animal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantityComment() {
        return quantityComment;
    }

    public void setQuantityComment(Integer quantityComment) {
        this.quantityComment = quantityComment;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
